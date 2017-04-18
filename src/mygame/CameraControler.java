/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;

/**
 *
 * @author Zarcos
 */
public class CameraControler extends AbstractAppState{
    
    private SimpleApplication app;
    private AppStateManager stateManager;
    private Player player;
    private ChaseCamera cam;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.stateManager = this.app.getStateManager();
        this.player = this.stateManager.getState(PlayerControler.class).player;
        initCamera();
    }

    private void initCamera() {
        this.app.getCamera().setFrustumFar(2000f);
        cam = new ChaseCamera(this.app.getCamera(), player.model, this.app.getInputManager());
        cam.setMinDistance(1);
        cam.setMaxDistance(1);
        cam.setDragToRotate(false);
        cam.setInvertVerticalAxis(true);
    }
    
}
