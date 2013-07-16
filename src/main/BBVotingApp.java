package main;

import gui.SplashScreen;


/**
 *
 * @author alexisvincent
 */
public class BBVotingApp {

    private SplashScreen splashScreen;
    private static BBVotingApp INSTANCE;
    
    public BBVotingApp() {
        splashScreen = new SplashScreen();
    }

    public static void main(String[] args) {
        INSTANCE = new BBVotingApp();
        
        
    }
}
