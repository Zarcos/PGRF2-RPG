/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import NPC.EnemyNpcControler;
import Scene.Scene;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResults;
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
    private Node rootNode;
    private float cooldown;
    private float cooldownTimer = 2;
    private int damage = 50;
    public Player player;
    public EnemyNpcControler enemy;
    

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.stateManager = this.app.getStateManager();
        this.assetManager = this.app.getAssetManager();
        this.physics = this.stateManager.getState(Scene.class).physics;
        this.rootNode = this.app.getRootNode();
        initPlayerControler();
    }

    private void initPlayerControler() {
        player = new Player();
        player.model = new Node();
        
        CapsuleCollisionShape capsule = new CapsuleCollisionShape(3f, 3f);
        player.playerPhys = new CharacterControl(capsule, 0.5f);
        player.model.addControl(player.playerPhys);
        player.playerPhys.setPhysicsLocation(new Vector3f(0f, 10f, 0f));
        rootNode.attachChild(player.model);
        physics.getPhysicsSpace().add(player.playerPhys);
    }

    @Override
    public void update(float tpf) {
        if (cooldown > 0){
            cooldown -= tpf;
        }
    }
    
    public boolean isCooldown(){
        return cooldown<=0;
    }
    
    public void attack(CollisionResults results){
        results.getClosestCollision().getGeometry().getParent().setUserData(
                        "HP", (Integer)results.getClosestCollision().getGeometry().getParent().getUserData("HP")-damage);
        cooldown = cooldownTimer;
    }

}
