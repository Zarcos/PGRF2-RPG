package App;

import NPC.EnemyNpcControler;
import Scene.Scene;
import Scene.Camera;
import Player.Control;
import Player.PlayerControler;
import Scene.GUI;
import Scene.Sky;
import com.jme3.app.SimpleApplication;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class App extends SimpleApplication {

    public static void main(String[] args) {
        App app = new App();
        app.start();

    }

    @Override
    public void simpleInitApp() {

        this.getFlyByCamera().setEnabled(false);
        this.stateManager.attach(new Scene());
        this.stateManager.attach(new Sky());
        this.stateManager.attach(new PlayerControler());
        this.stateManager.attach(new Camera());
        this.stateManager.attach(new Control());
        this.stateManager.attach(new EnemyNpcControler("Golem"));
        this.stateManager.attach(new GUI(guiFont, settings));
    }
}
