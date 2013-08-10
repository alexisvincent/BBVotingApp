package gui;

import components.AFrame;

/**
 *
 * @author alexisvincent
 */
public class MainFrame extends AFrame {

    private static HomeScreen homeScreen;
    private static VotersKeyOverlay votersKeyOverlay ;

    public MainFrame() {
        super();

        //new instances
        homeScreen = new HomeScreen();
        votersKeyOverlay = new VotersKeyOverlay();
        votersKeyOverlay.setVisible(false);

        //configure this damn FRAME O.o
        this.setResizable(false);
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);

        //begin adding components
        addPaneltoDefaultLayer(homeScreen);
        addPaneltoPaletteLayer(votersKeyOverlay);

        //starting animation
    }
    
    public static HomeScreen getHomeScreen() {
        return homeScreen;
    }

    public static VotersKeyOverlay getVotersKeyOverlay() {
        return votersKeyOverlay;
    }
    
    
}
