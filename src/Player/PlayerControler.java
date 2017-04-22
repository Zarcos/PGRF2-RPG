/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import Scene.Scene;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Zarcos
 */
public class PlayerControler extends AbstractAppState {

    private SimpleApplication app;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private BulletAppState physics;
    public Player player;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.stateManager = this.app.getStateManager();
        this.assetManager = this.app.getAssetManager();
        this.physics = this.stateManager.getState(Scene.class).physics;
        initPlayerControler();
    }

    private void initPlayerControler() {
        player = new Player();
        player.model = new Node();

        player.playerPhys = new BetterCharacterControl(1f, 5f, 2f);
        player.playerPhys.setJumpForce(new Vector3f(0f, 5f, 0f));
        physics.setDebugEnabled(false);

        player.attachChild(player.model);
        player.addControl(player.playerPhys);
        this.app.getRootNode().attachChild(player);
        player.model.setLocalTranslation(0f, 2.5f, 0f);
        physics.getPhysicsSpace().add(player.playerPhys);
        player.playerPhys.warp(new Vector3f(0f, 15f, 0f));
    }

}
