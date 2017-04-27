/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scene;

import Player.Player;
import Player.PlayerControler;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

/**
 *
 * @author Zarcos
 */
public class GUI extends AbstractAppState {

    private SimpleApplication app;
    private Node guiNode;
    private BitmapFont guiFont;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private final AppSettings setting;
    private BitmapText crossHair;
    private BitmapText playerStats;
    private BitmapText cooldown;
    private BitmapText playerDead;
    private Player player;
    private float deadTimer = 0;

    public GUI(BitmapFont guiFont, AppSettings setting) {
        this.guiFont = guiFont;
        this.setting = setting;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication) app;
        this.guiNode = this.app.getGuiNode();
        this.stateManager = this.app.getStateManager();
        this.assetManager = this.app.getAssetManager();
        this.player = this.stateManager.getState(PlayerControler.class).player;
        initCrossHair();
        initPlayerStats();
        initCooldown();
        initDeadPlayer();
        guiNode.detachAllChildren();
    }

    @Override
    public void update(float tpf) {
        updatePlayerStats();
        updateCooldown();
        updateDead(tpf);
    }

    private void initCrossHair() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        crossHair = new BitmapText(guiFont, false);
        crossHair.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        crossHair.setText("+");
        crossHair.setLocalTranslation(
                setting.getWidth() / 2 - crossHair.getLineWidth() / 2,
                setting.getHeight() / 2 + crossHair.getLineHeight() / 2, 0);
        guiNode.attachChild(crossHair);
    }

    private void initPlayerStats() {
        playerStats = new BitmapText(guiFont, false);
        playerStats.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        playerStats.setColor(ColorRGBA.Green);
        updatePlayerStats();
        playerStats.setLocalTranslation(10, setting.getHeight() - 10, 0);
        guiNode.attachChild(playerStats);
    }

    private void initCooldown() {
        cooldown = new BitmapText(guiFont, false);
        cooldown.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        updateCooldown();
        cooldown.setLocalTranslation(
                setting.getWidth() / 2 - crossHair.getLineWidth(),
                setting.getHeight() / 2 + crossHair.getLineHeight(), 0);
        guiNode.attachChild(cooldown);
    }

    private void initDeadPlayer() {
        playerDead = new BitmapText(guiFont, false);
        playerDead.setSize(guiFont.getCharSet().getRenderedSize() * 3);
        playerDead.setText("You are dead!! ... respawn");
        playerDead.setLocalTranslation(
                setting.getWidth() / 3 - crossHair.getLineWidth() / 2f,
                setting.getHeight() / 2.1f + crossHair.getLineHeight() / 2, 0);
        playerDead.setColor(ColorRGBA.Red);

    }

    private void updatePlayerStats() {
        playerStats.setText("LvL: " + player.level + "\r\n"
                + "HP:  " + player.actualHp + " / " + player.fullHp + "\r\n"
                + "XP:  " + player.experience + " / " + player.experiencesToLevel);
    }

    private void updateCooldown() {
        float tempCooldown = stateManager.getState(PlayerControler.class).getCooldown();
        if (tempCooldown <= 0) {
            guiNode.detachChild(cooldown);
        } else {
            String text = String.format("%.1f", tempCooldown);
            cooldown.setText(text);
            if (!(guiNode.hasChild(cooldown))) {
                guiNode.attachChild(cooldown);
            }
        }
    }

    public void updateDead(float tpf) {
        if (player.isDead) {
            deadTimer = 3;
            if (!guiNode.hasChild(playerDead)) {
                guiNode.attachChild(playerDead);
            }
        }
        if (deadTimer > 0) {
            deadTimer -= tpf;
        }
        if (deadTimer <= 0 && guiNode.hasChild(playerDead)) {
            guiNode.detachChild(playerDead);
        }
    }

}
