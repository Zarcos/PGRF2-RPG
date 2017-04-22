/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scene;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import jme3utilities.TimeOfDay;
import jme3utilities.sky.SkyControl;

/**
 *
 * @author Zarcos
 */
public class Sky extends AbstractAppState {

    private SkyControl skyControl;
    private TimeOfDay timeOfDay;
    private SimpleApplication app;
    private Camera cam;
    private AssetManager assetManager;
    private Node rootNode;
    private ViewPort viewPort;
    private AmbientLight ambient;
    private DirectionalLight sun;
    private DirectionalLightShadowRenderer shadow;
    private final boolean starMotion = true;
    private final boolean bottomDome = true;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        this.app = (SimpleApplication) app;
        this.cam = this.app.getCamera();
        this.assetManager = this.app.getAssetManager();
        this.rootNode = this.app.getRootNode();
        this.viewPort = this.app.getViewPort();
        this.sun = this.app.getStateManager().getState(Scene.class).sun;
        this.ambient = this.app.getStateManager().getState(Scene.class).ambient;
        this.shadow = this.app.getStateManager().getState(Scene.class).shadow;

        skyControl = new SkyControl(assetManager, cam, 0.9f, starMotion, bottomDome);
        timeOfDay = new TimeOfDay(15);

        stateManager.attach(timeOfDay);
        timeOfDay.setRate(20f);
        rootNode.addControl(skyControl);

        skyControl.getUpdater().setMainLight(sun);
        skyControl.getUpdater().setAmbientLight(ambient);
        skyControl.getUpdater().addShadowRenderer(shadow);
        skyControl.getUpdater().addViewPort(viewPort);
        skyControl.getSunAndStars().setObserverLatitude(0.9f);
        skyControl.setCloudiness(0.8f);
        skyControl.setEnabled(true);
    }

    @Override
    public void update(float tpf) {
        float hour = timeOfDay.hour();
        skyControl.getSunAndStars().setHour(hour);
        skyControl.setCloudsRate(hour);
    }
}
