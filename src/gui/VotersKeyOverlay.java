/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import components.AComponent;
import components.BLabel;
import components.BTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.BBVotingApp;
import networking.ASocket;
import networking.Request;
import networking.Responce;
import objects.Candidate;
import org.jdom2.Document;
import org.jdom2.Element;
import toolkit.BSettings;

/**
 *
 * @author alexisvincent
 */
public class VotersKeyOverlay extends AComponent {

    private BTextField votersKeyTextArea;
    private BLabel votersKeyLabel;
    private GridBagConstraints gc;

    public VotersKeyOverlay() {
        votersKeyTextArea = new BTextField();
        votersKeyTextArea.setPreferredSize(new Dimension(300, 40));
        votersKeyTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !votersKeyTextArea.getText().equals("Voding Failed") && !votersKeyTextArea.getText().equals("Successfully Voted")) {
                    Candidate candidate = ((HomeScreen.HomeScreenPanel.CandidateListItem) MainFrame.getHomeScreen().getHomeScreenPanel().getCandidateList().getSelectedItem()).getCandidate();
                    String votersKey = votersKeyTextArea.getText();
                    votersKeyTextArea.setEditable(false);

                    Element rootElement = new Element("Request");
                    rootElement.setAttribute("RequestType", "Vote");
                    rootElement.setAttribute("From", "VotingApp");
                    rootElement.setAttribute("VoterKey", votersKey);
                    rootElement.setAttribute("CandidateID", candidate.getId());

                    Document document = new Document(rootElement);
                    ASocket socket = BBVotingApp.getNetworkingClient().getSocket();
                    Request request = new Request(document, socket);
                    Responce responce = socket.postRequest(request);

                    if (responce.getResponceCode().equals("200")) {
                        votersKeyTextArea.setText("Successfully Voted");
                    } else if (responce.getResponceCode().equals("300")) {
                        votersKeyTextArea.setText("Sorry, you have already voted");
                    } else if (responce.getResponceCode().equals("310")) {
                        votersKeyTextArea.setText("Incorrect key entered");
                    } else {
                        votersKeyTextArea.setText("Failed");
                    }

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(VotersKeyOverlay.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            votersKeyTextArea.setEditable(true);
                            votersKeyTextArea.setText("");
                            VotersKeyOverlay.this.setVisible(false);
                        }
                    });
                    t.start();

                }

            }
        });

        votersKeyLabel = new BLabel("Please enter your voters key");

        votersKeyLabel.setFont(BSettings.getFont().deriveFont(16f));
        votersKeyLabel.setPreferredSize(
                new Dimension(252, 20));

        this.setLayout(
                new GridBagLayout());
        gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;

        this.add(votersKeyLabel, gc);
        gc.gridy = 1;
        gc.insets = new Insets(20, 0, 0, 0);

        this.add(votersKeyTextArea, gc);

        this.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                VotersKeyOverlay.this.setVisible(false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(new Color(34, 34, 34, 200));
        g2d.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 15, 15);

        g2d.setPaint(new Color(34, 34, 34, 255));
        g2d.fillRoundRect(80, 145, 340, 120, 15, 15);
    }
}
