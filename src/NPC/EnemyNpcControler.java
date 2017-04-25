/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NPC;

import Player.Player;
import Player.PlayerControler;
import Scene.Scene;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;

/**
 *
 * @author Zarcos
 */
public class EnemyNpcControler extends AbstractAppState {

    private SimpleApplication app;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private BulletAppState physics;
    private Node npcNode;
    private boolean isDead = false;
    private float respawn;
    public EnemyNPC npc;
    public Player player;
    private int hp;
    private final Vector3f position;
    private int oldHP = hp;

    public EnemyNpcControler(int hp, Vector3f position) {
        this.hp = hp;
        this.position = position;
    }
    
    

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.stateManager = this.app.getStateManager();
        this.assetManager = this.app.getAssetManager();
        this.physics = this.stateManager.getState(Scene.class).physics;
        this.npcNode = this.stateManager.getState(Scene.class).npcNode;
        this.player = this.stateManager.getState(PlayerControler.class).player;
        initNpc();
    }

    @Override
    public void update(float tpf) {
        
        if(!(oldHP==(Integer)npc.model.getUserData("HP"))){
            npc.npcPhys.jump();
            oldHP = (Integer) npc.model.getUserData("HP");
        }
        
        
        if ((Integer) npc.model.getUserData("HP") <= 0 && !isDead) {
            npcNode.detachChild(npc.model);
            isDead = true;
            respawn = 3;
        }

        if (isDead) {
            respawn -= tpf;
            if (respawn <= 0) {
                npcNode.attachChild(npc.model);
                npc.model.setUserData("HP", 100);
                isDead = false;
            }
        }

        Vector3f playerLoc = player.playerPhys.getPhysicsLocation();
        float distance = playerLoc.distance(npc.npcPhys.getPhysicsLocation());

        float x = playerLoc.x - npc.npcPhys.getPhysicsLocation().x;
        float z = playerLoc.z - npc.npcPhys.getPhysicsLocation().z;
        playerLoc.y = 0;
        if (distance < 50) {
            npc.npcPhys.setViewDirection(new Vector3f(x, 0, z));
        }

        if (distance < 30 && distance > 10) {
            npc.npcPhys.setWalkDirection(new Vector3f(x / 100, 0, z / 100));
        } else if (distance < 8) {
            npc.npcPhys.setWalkDirection(new Vector3f(-x / 100, 0, -z / 100));
        } else {
            npc.npcPhys.setWalkDirection(new Vector3f(Vector3f.ZERO));
        }
    }

    private void initNpc() {
        npc = new EnemyNPC(hp, position);
        npc.model = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        npc.model.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        npc.model.setUserData("HP", npc.hp);
        npc.model.scale(0.3f);

        CapsuleCollisionShape capsule = new CapsuleCollisionShape(1f, 1.2f);
        npc.npcPhys = new CharacterControl(capsule, 0.1f);
        npc.model.addControl(npc.npcPhys);
        npc.npcPhys.setPhysicsLocation(npc.position);
        npc.npcPhys.setJumpSpeed(5f);
        npc.npcPhys.setMaxSlope(45f);
        npcNode.attachChild(npc.model);
        physics.getPhysicsSpace().add(npc.npcPhys);
    }
}
