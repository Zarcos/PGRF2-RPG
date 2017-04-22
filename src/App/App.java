package App;

import Scene.Scene;
import Scene.CameraControler;
import Player.Control;
import Player.PlayerControler;
import Scene.SkyControler;
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
        this.stateManager.attach(new SkyControler());
        this.stateManager.attach(new PlayerControler());
        this.stateManager.attach(new CameraControler());
        this.stateManager.attach(new Control());
    }
}
