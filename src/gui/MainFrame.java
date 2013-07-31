package gui;

import components.AComponent;
import components.AFrame;

/**
 *
 * @author alexisvincent
 */
public class MainFrame extends AFrame {

    private HomeScreen homeScreen;
    private AComponent overlay;

    public MainFrame() {
        super();

        //new instances
        homeScreen = new HomeScreen();
        overlay = new Overlay();
        overlay.setVisible(false);

        //configure this damn FRAME O.o
        this.setResizable(false);
        this.setSize(400, 400);

        //begin adding components
        addPaneltoDefaultLayer(homeScreen);
        addPaneltoPaletteLayer(overlay);

        //starting animation
    }
}
