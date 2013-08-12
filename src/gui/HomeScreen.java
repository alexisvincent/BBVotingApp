package gui;

import components.AColor;
import components.AComponent;
import components.AList;
import components.AListItem;
import components.AListModel;
import components.BButton;
import components.BFooter;
import components.BMenuBar;
import components.BPanel;
import components.BTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.Timer;
import main.BBVotingApp;
import networking.ASocket;
import networking.Request;
import networking.Responce;
import objects.Candidate;
import org.jdom2.Document;
import org.jdom2.Element;
import toolkit.BSettings;
import toolkit.BToolkit;
import toolkit.ResourceManager;
import toolkit.UIToolkit;

/**
 *
 * @author alexisvincent
 */
public class HomeScreen extends BPanel {
    //declare components

    private BMenuBar menubar;
    private HomeScreen.HomeScreenPanel homeScreenPanel;
    private BFooter footer;
    private JComponent logoPane;

    public HomeScreen() {
        //init components
        menubar = new BMenuBar();
        homeScreenPanel = new HomeScreen.HomeScreenPanel();
        footer = new BFooter();
        logoPane = new JComponent() {
            Image logo = BToolkit.getImage("logo");
            double logoEnlargement = 1.5;
            Point pt = new Point(250 - (int) (logo.getWidth(null) * logoEnlargement) / 2, 200 - (int) (logo.getHeight(null) * logoEnlargement) / 2);

            @Override
            public void paint(Graphics g) {
                Graphics2D g2d = UIToolkit.getPrettyGraphics(g);
                g2d.setComposite(UIToolkit.makeComposite(50));
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
        this.add(homeScreenPanel, gc);

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

    public HomeScreen.HomeScreenPanel getHomeScreenPanel() {
        return homeScreenPanel;
    }

    class HomeScreenPanel extends JComponent {

        private GridBagConstraints gc;
        private int panelOpacity;
        private AList candidateList;
        private BButton voteButton;
        private HomeScreen.HomeScreenPanel.CandidateDisplayPane candidateDisplayPane;

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

            candidateList = new AList(new HomeScreen.HomeScreenPanel.CandidatesListModel());
            candidateList.setPreferredSize(new Dimension(180, 310));

            Timer updater = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        updateList();
                    } catch (Exception ex) {
                        System.out.println(e);
                    }
                }
            });
            updater.start();

            candidateDisplayPane = new HomeScreen.HomeScreenPanel.CandidateDisplayPane();
            candidateDisplayPane.setPreferredSize(new Dimension(300, 345));

            candidateList.getModel().addSelectionListener(candidateList.getModel().new SelectionListener() {
                @Override
                public void itemSelected(AListItem item) {
                    candidateDisplayPane.setCandidate(((HomeScreen.HomeScreenPanel.CandidateListItem) item).getCandidate());
                }
            });

            if (!candidateList.getItems().isEmpty()) {
                candidateList.getItems().get(0).setSelected(true);
            }

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
            gc.insets = new Insets(2, 0, 0, 0);
            gc.fill = GridBagConstraints.NONE;
            gc.anchor = GridBagConstraints.CENTER;
            this.add(candidateList, gc);

            gc.gridy = 1;
            gc.insets = new Insets(8, 0, 10, 0);
            gc.anchor = GridBagConstraints.CENTER;
            this.add(voteButton, gc);

            gc.gridx = 1;
            gc.gridy = 0;
            gc.gridheight = 2;
            gc.insets = new Insets(8, 0, 8, 8);
            gc.anchor = GridBagConstraints.CENTER;
            this.add(candidateDisplayPane, gc);

        }

        public void updateList() {
            //getCandidates
            ArrayList<AListItem> items = new ArrayList<>();
            Element rootElement = new Element("Request");
            rootElement.setAttribute("RequestType", "Candidates");
            rootElement.setAttribute("From", "VotingApp");

            Document document = new Document(rootElement);
            ASocket socket = BBVotingApp.getNetworkingClient().getSocket();
            Request request = new Request(document, socket);
            Responce responce = null;

            try {
                responce = socket.postRequest(request);
            } catch (Exception e) {
                System.out.println(e);
            }

            if (responce != null && responce.getResponceCode().equals("200")) {

                rootElement = responce.getRootElement();
                ArrayList<Candidate> candidates = new ArrayList<>();
                int candidateCount = Integer.parseInt(rootElement.getAttributeValue("CandidatesCount"));

                for (int i = 1; i < candidateCount + 1; i++) {
                    Element candidateElement = rootElement.getChild("Candidate" + i);
                    String id = candidateElement.getAttributeValue("ID");
                    String name = candidateElement.getAttributeValue("Name");
                    String info = candidateElement.getAttributeValue("Info");
                    String image = candidateElement.getAttributeValue("Image");

                    candidates.add(new Candidate(id, name, info, image));
                }

                for (Candidate candidate : candidates) {
                    items.add(new HomeScreen.HomeScreenPanel.CandidateListItem(candidate));
                }

            } else {
                items.add(new HomeScreen.HomeScreenPanel.CandidateListItem(new Candidate("", "Server Offline", "", "")));
                System.out.println("Could not get Candidates from server");
            }


            AListItem selectedItem = null;
            String previouslySelectedIndex = null;

            try {
                previouslySelectedIndex = ((CandidateListItem) candidateList.getSelectedItem()).getCandidate().getId();
            } catch (Exception e) {
            }

            for (AListItem aListItem : items) {
                if (((CandidateListItem) aListItem).getCandidate().getId().equals(previouslySelectedIndex)) {
                    selectedItem = aListItem;
                }
            }

            candidateList.setItems(items);

            revalidate();
            repaint();

            if (selectedItem != null) {
                candidateList.setSelectedItem(selectedItem);
            }


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
            Graphics2D g2d = UIToolkit.getPrettyGraphics(g);
            g2d.setComposite(UIToolkit.makeComposite(panelOpacity));
        }

        public class CandidatesListModel extends AListModel {

            public CandidatesListModel() {
                super();
            }

            @Override
            public void setItems(ArrayList<AListItem> items) {
                if (!items.isEmpty() && items.get(0) instanceof HomeScreen.HomeScreenPanel.CandidateListItem) {
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
            private JTextArea infoField;

            public CandidateDisplayPane() {

                name = "";
                info = "";
                image = null;

                infoField = new JTextArea() {

                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                    }
                    
                };
                infoField.setBackground(new Color(0,0,0,0));
                infoField.setSize(new Dimension(100, 100));
                infoField.setEditable(false);
                infoField.setFont(ResourceManager.getFont("Sax Mono").deriveFont(14f));
                infoField.setForeground(AColor.fancyDarkBlue);

                this.setLayout(new GridBagLayout());
                GridBagConstraints gc = new GridBagConstraints();
                gc.gridx = 0;
                gc.gridy = 0;
                gc.gridwidth = 1;
                gc.gridheight = 1;
                gc.weightx = 1;
                gc.weighty = 1;
                gc.ipadx = 0;
                gc.ipady = 0;
                gc.insets = new Insets(70, 20, 50, 20);
                gc.fill = GridBagConstraints.BOTH;
                gc.anchor = GridBagConstraints.CENTER;

                this.add(infoField, gc);
            }

            public void setCandidate(Candidate candidate) {
                setName(candidate.getName());
                setInfo(candidate.getInfo());
                infoField.setText(info);
                //setImage(candidate.getImage());
            }

            @Override
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
                Graphics2D g2d = UIToolkit.getPrettyGraphics(g);
                g2d.setPaint(new Color(23, 23, 23, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setPaint(AColor.fancyDarkBlue);

                g2d.setFont(ResourceManager.getFont("Sax Mono").deriveFont(18f));
                if (name.length() > 25) {
                    g2d.drawString(name.substring(0, 23) + "...", 20, 40);
                } else {
                    g2d.drawString(name, 20, 40);
                }

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