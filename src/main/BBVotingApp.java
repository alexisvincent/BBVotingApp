package main;

import components.BMenuBar;
import gui.MainFrame;
import gui.SplashScreen;
import networking.ElectionProfile;
import networking.NetworkingClient;
import settingsEngine.ProfileEngine;

/**
 *
 * @author alexisvincent
 */
public class BBVotingApp {

    private SplashScreen splashScreen;
    private static BBVotingApp INSTANCE;
    private static MainFrame mainFrame;
    //Engines
    private static ProfileEngine profileEngine;
    private static NetworkingClient networkingClient;
    private static ElectionProfile electionProfile;

    public BBVotingApp() {
        splashScreen = new SplashScreen();
        
        profileEngine = new ProfileEngine();
        setElectionProfile(profileEngine.getFirstProfile());
        
        mainFrame = new MainFrame();
        splashScreen.setVisible(false);
        BMenuBar.setMainFrame(mainFrame);
        mainFrame.setVisible(true);
    }

    public ElectionProfile getElectionProfile() {
        return electionProfile;
    }

    public void setElectionProfile(ElectionProfile electionProfile) {
        this.electionProfile = electionProfile;
        this.networkingClient = new NetworkingClient(electionProfile.getServer());
    }

    public static BBVotingApp getINSTANCE() {
        return INSTANCE;
    }

    public static void setINSTANCE(BBVotingApp INSTANCE) {
        BBVotingApp.INSTANCE = INSTANCE;
    }

    public static MainFrame getMainFrame() {
        return mainFrame;
    }

    public static void setMainFrame(MainFrame mainFrame) {
        BBVotingApp.mainFrame = mainFrame;
    }

    public static NetworkingClient getNetworkingClient() {
        return networkingClient;
    }

    public static void setNetworkingClient(NetworkingClient networkingClient) {
        BBVotingApp.networkingClient = networkingClient;
    }
    

    public static void main(String[] args) {
        INSTANCE = new BBVotingApp();
    }
}
