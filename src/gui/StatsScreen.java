package gui;

import components.AList;
import components.AListItem;
import components.AListModel;
import components.BFooter;
import components.BMenuBar;
import components.BPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JComponent;
import toolkit.BToolkit;

/**
 *
 * @author alexisvincent
 */
public class StatsScreen extends BPanel {
    //declare components

    private BMenuBar menubar;
    private StatsScreenPanel statsScreenPanel;
    private BFooter footer;
    private JComponent logoPane;

    public StatsScreen() {
        //init components
        menubar = new BMenuBar();
        statsScreenPanel = new StatsScreenPanel();
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

    public StatsScreenPanel getHomeScreenPanel() {
        return statsScreenPanel;
    }

    class StatsScreenPanel extends JComponent {

        private GridBagConstraints gc;
        private int panelOpacity;
        private AList stats;

        public StatsScreenPanel() {

            //variabili
            panelOpacity = 255;

            //setup le variabili
            stats = new AList(new CandidatesListModel());
            stats.setPreferredSize(new Dimension(350, 300));

            //getCandidates
            ArrayList<AListItem> items = new ArrayList<>();
            items.add(new CandidateListItem("La di Dah"));
            stats.setItems(items);

            //begin adding le variabili
            this.setLayout(new GridBagLayout());
            gc = new GridBagConstraints();
            gc.gridx = 0;
            gc.gridy = 0;
            gc.gridwidth = 1;
            gc.gridheight = 1;
            gc.weightx = 1;
            gc.weighty = 1;
            gc.ipadx = 0;
            gc.ipady = 0;
            gc.insets = new Insets(0, 0, 0, 0);
            gc.fill = GridBagConstraints.NONE;
            gc.anchor = GridBagConstraints.CENTER;
            this.add(stats, gc);
        }

        public void animate(String action) {
            switch (action) {
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(BToolkit.makeComposite(panelOpacity));
        }

        public class CandidatesListModel extends AListModel {

            public CandidatesListModel() {
            }

            @Override
            public void setItems(ArrayList<AListItem> items) {
                if (!items.isEmpty() && items.get(0) instanceof CandidateListItem) {
                    //ArrayList<CandidateListItem> candidateItems = new ArrayList<>();

                    setItems(items);

//                    for (AListItem item : items) {
//                        candidateItems.add((CandidateItem)item);
//                    }


                } else {
                    System.out.println("Invalid Items");
                }
            }
        }

        public class CandidateListItem extends AListItem {

            private CandidateListItem(String displayName) {
                super(displayName);
            }

            public CandidateListItem() {
            }
            
            @Override
            protected void paintComponent(Graphics g) {
            }
        }
    }
}