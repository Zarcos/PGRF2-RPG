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
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jme3utilities.sky.SkyControl;

/**
 *
 * @author Zarcos
 */
public class SkyControler extends AbstractAppState{
    
    private SkyControl skyControl;
    private SimpleApplication app;
    private Camera cam;
    private AssetManager assetManager;
    private Node rootNode;
    private AmbientLight ambient;
    private DirectionalLight sun;
    private final boolean starMotion = true;
    private final boolean bottomDome = true;
    
    

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        
        this.app = (SimpleApplication) app;
        this.cam = this.app.getCamera();
        this.assetManager = this.app.getAssetManager();
        this.rootNode = this.app.getRootNode();
        
        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        
        ambient = new AmbientLight(new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
        rootNode.addLight(ambient);
        
        skyControl = new SkyControl(assetManager, cam, 0.9f, starMotion, bottomDome);
        rootNode.addControl(skyControl);
        skyControl.getUpdater().setMainLight(sun);
        skyControl.getUpdater().setAmbientLight(ambient);
        skyControl.getSunAndStars().setHour(12);
        skyControl.getSunAndStars().setObserverLatitude(0.9f);
        skyControl.setCloudiness(0.8f);
        skyControl.setEnabled(true);
        
    }

    @Override
    public void update(float tpf) {
        
    }
    
    
    
}
