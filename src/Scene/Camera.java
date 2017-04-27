/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scene;

import Player.PlayerControler;
import Player.Player;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;

/**
 *
 * @author Zarcos
 */
public class Camera extends AbstractAppState {

    private SimpleApplication app;
    private AppStateManager stateManager;
    private Player player;
    public ChaseCamera cam;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.stateManager = this.app.getStateManager();
        this.player = this.stateManager.getState(PlayerControler.class).player;
        initCamera();
    }

    private void initCamera() {
        app.getCamera().setFrustumFar(1500f);
        cam = new ChaseCamera(this.app.getCamera(), player.model, this.app.getInputManager());
        cam.setDefaultDistance(0.1f);
        cam.setDragToRotate(false);
        cam.setInvertVerticalAxis(true);
    }

}
