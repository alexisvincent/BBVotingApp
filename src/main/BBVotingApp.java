package main;

import components.BMenuBar;
import gui.MainFrame;
import gui.SplashScreen;


/**
 *
 * @author alexisvincent
 */
public class BBVotingApp {

    private SplashScreen splashScreen;
    private static BBVotingApp INSTANCE;
    private static MainFrame mainFrame;
    
    public BBVotingApp() {
        splashScreen = new SplashScreen();
        mainFrame = new MainFrame();
        splashScreen.setVisible(false);
        mainFrame.setVisible(true);
        
        
        BMenuBar.setMainFrame(mainFrame);
    }

    public static void main(String[] args) {
        INSTANCE = new BBVotingApp();
    }
   
}
