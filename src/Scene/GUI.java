/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scene;

import App.App;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

/**
 *
 * @author Zarcos
 */
public class GUI extends AbstractAppState{
    
    private SimpleApplication app;
    private Node guiNode;
    private BitmapFont guiFont;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private final AppSettings setting;
    private BitmapText crossHair;

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
        initCrossHair();
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
    
}
