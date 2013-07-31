package gui;

import components.AFrame;

/**
 *
 * @author alexisvincent
 */
public class MainFrame extends AFrame {

    private StatsScreen statsScreen;
    private VotersKeyOverlay votersKeyOverlay ;

    public MainFrame() {
        super();

        //new instances
        statsScreen = new StatsScreen();
        votersKeyOverlay = new VotersKeyOverlay();
        votersKeyOverlay.setVisible(false);

        //configure this damn FRAME O.o
        this.setResizable(false);
        this.setSize(400, 400);

        //begin adding components
        addPaneltoDefaultLayer(statsScreen);
        addPaneltoPaletteLayer(votersKeyOverlay);

        //starting animation
    }
}
