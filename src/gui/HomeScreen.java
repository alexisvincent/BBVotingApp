package gui;

import components.AComponent;
import components.AList;
import components.AListItem;
import components.AListModel;
import components.BButton;
import components.BFooter;
import components.BMenuBar;
import components.BPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JComponent;
import main.BBVotingApp;
import networking.ASocket;
import networking.Request;
import networking.Responce;
import objects.Candidate;
import org.jdom2.Document;
import org.jdom2.Element;
import toolkit.BSettings;
import toolkit.BToolkit;

/**
 *
 * @author alexisvincent
 */
public class HomeScreen extends BPanel {
    //declare components

    private BMenuBar menubar;
    private HomeScreenPanel statsScreenPanel;
    private BFooter footer;
    private JComponent logoPane;

    public HomeScreen() {
        //init components
        menubar = new BMenuBar();
        statsScreenPanel = new HomeScreenPanel();
        footer = new BFooter();
        logoPane = new JComponent() {
            Image logo = BToolkit.getImage("logo");
            double logoEnlargement = 1.5;
            Point pt = new Point(200 - (int) (logo.getWidth(null) * logoEnlargement) / 2, 200 - (int) (logo.getHeight(null) * logoEnlargement) / 2);

            @Override
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(BToolkit.makeComposite(50));
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                g2d.drawImage(logo, pt.x, pt.y, (int) (logo.getWidth(null) * logoEnlargement), (int) (logo.getHeight(null) * logoEnlargement), this);
            }
        };
        //set ResultScreen properties
        //add components to ResultScreen
        GridBagConstraints gc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.CENTER;
        this.add(menubar, gc);

        gc.gridy = 1;
        gc.weighty = 1;
        this.add(statsScreenPanel, gc);

        gc.gridy = 2;
        gc.weighty = 0;
        //this.add(footer, gc);

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 3;
        gc.weightx = 1;
        gc.weighty = 1;
        this.add(logoPane, gc);

    }

    public HomeScreenPanel getHomeScreenPanel() {
        return statsScreenPanel;
    }
    
    class HomeScreenPanel extends JComponent {

        private GridBagConstraints gc;
        private int panelOpacity;
        private AList candidateList;
        private BButton voteButton;
        private CandidateDisplayPane candidateDisplayPane;

        public HomeScreenPanel() {

            //variabili
            panelOpacity = 255;

            //setup le variabili
            voteButton = new BButton("VOTE");
            voteButton.setPreferredSize(new Dimension(160, 30));
            voteButton.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseReleased(MouseEvent e) {
                    MainFrame.getVotersKeyOverlay().setVisible(true);
                }
                
            });

            candidateList = new AList(new CandidatesListModel());
            candidateList.setPreferredSize(new Dimension(180, 275));

            //getCandidates
            ArrayList<AListItem> items = new ArrayList<>();
            Element rootElement = new Element("Request");
            rootElement.setAttribute("RequestType", "Candidates");
            rootElement.setAttribute("From", "VotingApp");

            Document document = new Document(rootElement);
            ASocket socket = BBVotingApp.getNetworkingClient().getSocket();
            Request request = new Request(document, socket);
            Responce responce = socket.postRequest(request);

            if (responce.getResponceCode().equals("200")) {

                String id = "";
                String name = "";
                String info = "";
                Image image = null;

                rootElement = responce.getRootElement();
                ArrayList<Candidate> candidates = new ArrayList<>();
                int candidateCount = Integer.parseInt(rootElement.getAttributeValue("CandidatesCount"));

                for (int i = 1; i < candidateCount + 1; i++) {
                    Element candidateElement = rootElement.getChild("Candidate" + i);
                    id = candidateElement.getAttributeValue("id");
                    name = candidateElement.getAttributeValue("name");
                    info = candidateElement.getAttributeValue("info");
                    image = null;

                    candidates.add(new Candidate(id, name, info, image));
                }

                for (Candidate candidate : candidates) {
                    items.add(new CandidateListItem(candidate));
                }

            } else {
                System.out.println("Could not get Candidates from server");
            }

            candidateList.setItems(items);
            candidateList.getItems().get(0).setSelected(true);

            candidateDisplayPane = new CandidateDisplayPane();
            candidateDisplayPane.setPreferredSize(new Dimension(300, 300));

            candidateList.getModel().addSelectionListener(candidateList.getModel().new SelectionListener() {
                @Override
                public void itemSelected(AListItem item) {
                    candidateDisplayPane.setName(((CandidateListItem) item).getCandidate().getName());
                    candidateDisplayPane.setInfo(((CandidateListItem) item).getCandidate().getInfo());
                    candidateDisplayPane.setImage(((CandidateListItem) item).getCandidate().getImage());
                }
            });

            //begin adding le variabili
            this.setLayout(new GridBagLayout());
            gc = new GridBagConstraints();
            gc.gridx = 0;
            gc.gridy = 0;
            gc.gridwidth = 1;
            gc.gridheight = 1;
            gc.weightx = 0;
            gc.weighty = 0;
            gc.ipadx = 0;
            gc.ipady = 0;
            gc.insets = new Insets(40, 0, 0, 0);
            gc.fill = GridBagConstraints.NONE;
            gc.anchor = GridBagConstraints.SOUTHEAST;
            this.add(candidateList, gc);

            gc.gridy = 1;
            gc.insets = new Insets(0, 0, 0, 10);
            gc.anchor = GridBagConstraints.NORTHEAST;
            this.add(voteButton, gc);

            gc.gridx = 1;
            gc.gridy = 0;
            gc.gridheight = 2;
            gc.insets = new Insets(40, 0, 0, 0);
            gc.anchor = GridBagConstraints.WEST;
            this.add(candidateDisplayPane, gc);

        }

        public void animate(String action) {
            switch (action) {
            }
        }

        public AList getCandidateList() {
            return candidateList;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(BToolkit.makeComposite(panelOpacity));
        }

        public class CandidatesListModel extends AListModel {

            public CandidatesListModel() {
                super();
            }

            @Override
            public void setItems(ArrayList<AListItem> items) {
                if (!items.isEmpty() && items.get(0) instanceof CandidateListItem) {
                    super.setItems(items);
                } else {
                    System.out.println("Invalid Items: CandidateListItems required");
                }
            }
        }

        public class CandidateDisplayPane extends AComponent {

            private String name;
            private String info;
            private Image image;

            public CandidateDisplayPane() {
                name = ((CandidateListItem)HomeScreenPanel.this.candidateList.getSelectedItem()).getCandidate().getName();
                info = ((CandidateListItem)HomeScreenPanel.this.candidateList.getSelectedItem()).getCandidate().getInfo();
                image = ((CandidateListItem)HomeScreenPanel.this.candidateList.getSelectedItem()).getCandidate().getImage();
            }

            public void setName(String name) {
                this.name = name;
                repaint();
            }

            public void setInfo(String info) {
                this.info = info;
                repaint();
            }

            public void setImage(Image image) {
                this.image = image;
                repaint();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new Color(23, 23, 23, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setPaint(new Color(100, 0, 40));
                g2d.setFont(BSettings.getFont().deriveFont(21f));
                g2d.drawString(name, 100, 25);
            }
        }

        public class CandidateListItem extends AListItem {

            private Candidate candidate;

            private CandidateListItem(Candidate candidate) {
                super(candidate.getName());
                this.candidate = candidate;
            }

            public Candidate getCandidate() {
                return candidate;
            }

            public void setCandidate(Candidate candidate) {
                this.candidate = candidate;
            }
        }
    }
}