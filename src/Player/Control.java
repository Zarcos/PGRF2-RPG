/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;

/**
 *
 * @author Zarcos
 */
public class Control extends AbstractAppState implements ActionListener {

    private SimpleApplication app;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private InputManager inputManager;
    private Player player;
    private final Vector3f walkDirection;
    private final Vector3f camDir;
    private final Vector3f camLeft;
    private final float playerSpeed;
    private final float strafeSpeed;
    public boolean left = false, right = false, foward = false, backward = false, jump = false;

    public Control() {
        this.walkDirection = new Vector3f();
        this.camDir = new Vector3f();
        this.camLeft = new Vector3f();
        this.playerSpeed = 20;
        this.strafeSpeed = 20;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.stateManager = this.app.getStateManager();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.player = this.stateManager.getState(PlayerControler.class).player;
        setUpKeys();
    }

    @Override
    public void update(float tpf) {
        camDir.set(this.app.getCamera().getDirection()).multLocal(playerSpeed, 0.0f, playerSpeed);
        camLeft.set(this.app.getCamera().getLeft().multLocal(strafeSpeed));
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (foward) {
            walkDirection.addLocal(camDir);
        }
        if (backward) {
            walkDirection.addLocal(camDir.negate());
        }
        if (jump) {
            player.playerPhys.jump();
        }

        player.playerPhys.setWalkDirection(walkDirection.multLocal(1));
        player.playerPhys.setViewDirection(camDir);
    }

    private void setUpKeys() {
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Space");
    }

    @Override
    public void onAction(String binding, boolean isPressed, float tpf) {
        switch (binding) {
            case "Left":
                left = isPressed;
                break;
            case "Right":
                right = isPressed;
                break;
            case "Down":
                backward = isPressed;
                break;
            case "Up":
                foward = isPressed;
                break;
            case "Space":
                jump = isPressed;
                break;
            default:
                break;
        }
    }

}
