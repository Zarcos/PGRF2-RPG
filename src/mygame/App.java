package mygame;

import com.jme3.app.SimpleApplication;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class App extends SimpleApplication{

    public static void main(String[] args) {
        App app = new App();
        app.start();
        
    }

    @Override
    public void simpleInitApp() {
        this.getFlyByCamera().setEnabled(false);
        this.stateManager.attach(new Scene());
        this.stateManager.attach(new PlayerControler());
        this.stateManager.attach(new CameraControler());
        this.stateManager.attach(new Control());
    }
}
