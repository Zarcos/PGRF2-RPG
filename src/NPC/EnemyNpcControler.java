/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NPC;

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
    private Node enemyNode;
    public EnemyNPC npc;
    private String name;

    public EnemyNpcControler(String name) {
        this.name = name;
    }
    
    

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.stateManager = this.app.getStateManager();
        this.assetManager = this.app.getAssetManager();
        this.physics = this.stateManager.getState(Scene.class).physics;
        this.npcNode = this.stateManager.getState(Scene.class).npcNode;
        
        initNpc();
    }

    private void initNpc() {
        npc = new EnemyNPC();
        npc.model = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        npc.model.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
        
        CapsuleCollisionShape capsule = new CapsuleCollisionShape(2f, 6f);
        npc.npcPhys = new CharacterControl(capsule, 0.1f);
        npc.model.addControl(npc.npcPhys);
        npc.npcPhys.setPhysicsLocation(new Vector3f(-10f, 10f, -10));
        npcNode.attachChild(npc.model);
        physics.getPhysicsSpace().add(npc.npcPhys);
    }
}
