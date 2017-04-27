/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import NPC.EnemyNpcControler;
import Scene.GUI;
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
    private final float cooldownTimer = 1.9f;
    private final int regenHp = 5;
    private float regenHpTimer = 6;
    private final int damage = 34;
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

        CapsuleCollisionShape capsule = new CapsuleCollisionShape(1.2f, 4f);
        player.playerPhys = new CharacterControl(capsule, 0.5f);
        player.model.addControl(player.playerPhys);
        player.playerPhys.setPhysicsLocation(new Vector3f(0f, 3f, 0f));
        rootNode.attachChild(player.model);
        physics.getPhysicsSpace().add(player.playerPhys);
    }

    @Override
    public void update(float tpf) {
        if (cooldown > 0) {
            cooldown -= tpf;
        }

        if (regenHpTimer > 0) {
            regenHpTimer -= tpf;
        }
        if (isRegen() && player.actualHp < player.fullHp) {
            player.actualHp += regenHp;
            if (player.actualHp > player.fullHp) {
                player.actualHp = player.fullHp;
            }
            regenHpTimer = 3;
        }
        if (player.experience >= player.experiencesToLevel) {
            player.level += 1;
            player.experience = player.experience - player.experiencesToLevel;
            player.experiencesToLevel = player.level * player.nextLevelConst;
        }
        if (player.actualHp <= 0) {
            player.isDead = true;

        }
        if (player.isDead) {
            stateManager.getState(GUI.class).updateDead(tpf);
            player.isDead = false;
            player.playerPhys.setPhysicsLocation(new Vector3f(0f, 3f, 0f));
            player.actualHp = player.fullHp;
        }
    }

    public boolean isCooldown() {
        return cooldown <= 0;
    }

    public boolean isRegen() {
        return regenHpTimer <= 0;
    }

    public void attack(CollisionResults results) {

        Node temp = results.getClosestCollision().getGeometry().getParent().getParent().getParent().getParent();
        temp.setUserData("HP", (Integer) temp.getUserData("HP") - damage);
    }

    public void setCooldown() {
        cooldown = cooldownTimer;
    }

    public void addExperience(int experience) {
        player.experience += experience;
    }

    public void getDamage(int damage) {
        player.actualHp -= damage;
    }

    public float getCooldown() {
        if (cooldown > 0) {
            return cooldown;
        }
        return 0;
    }
}
