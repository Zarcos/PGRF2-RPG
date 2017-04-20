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
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.water.WaterFilter;

/**
 *
 * @author Zarcos
 */
public class Scene extends AbstractAppState{
    
    private SimpleApplication app;
    private AssetManager assetManager;
    private Node rootNode;
    private TerrainQuad terrain;
    private ViewPort viewPort;
    private DirectionalLight sun;
    private FilterPostProcessor fpp;
    private DirectionalLightShadowRenderer shadow;
    //private SunControler sunControler;
    private AmbientLight ambient;
    //private SkyControler skyControler;
    
    
    public Node scene;
    public BulletAppState physics;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
        this.rootNode = this.app.getRootNode();
        this.physics = new BulletAppState();
        this.viewPort = this.app.getViewPort();
        this.fpp = new FilterPostProcessor(assetManager);
        stateManager.attach(physics);
        
        initScene();
    }

    private void initScene() {
        
       initMap();
       
       scene = new Node();
       Node vesnice =(Node) assetManager.loadModel("Models/mesto/vesnice.j3o");
       Node misc = (Node) assetManager.loadModel("Models/mesto/misc.j3o");
       
       initLight();
       initShadow();
       initSunAndSky();
       initWater();
       
       vesnice.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
       misc.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
       terrain.setShadowMode(RenderQueue.ShadowMode.Receive);
       scene.attachChild(vesnice);
       scene.attachChild(misc);
       scene.attachChild(terrain);
       CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(scene);
       RigidBodyControl sceneBody = new RigidBodyControl(sceneShape, 0f);
       scene.addControl(sceneBody);
       rootNode.attachChild(scene);
       physics.getPhysicsSpace().add(sceneBody);
       viewPort.addProcessor(fpp);
       
       
    }

    private void initMap() {
        int patchSize = 65;
        int mapSize = 257;
        
        Material matTerrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        matTerrain.setBoolean("useTriPlanarMapping", false);
        matTerrain.setFloat("Shininess", 0.0f);
        
        matTerrain.setTexture("AlphaMap", assetManager.loadTexture("Textures/Terrain/base_G1_S3.png"));
        matTerrain.setTexture("AlphaMap_1", assetManager.loadTexture("Textures/Terrain/S_G_G2.png"));
        matTerrain.setTexture("AlphaMap_2", assetManager.loadTexture("Textures/Terrain/S2_R_C.png"));
        
        
        Texture base = assetManager.loadTexture("Textures/Terrain/tex_base.png");
        base.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap", base);
        matTerrain.setFloat("DiffuseMap_0_scale", 1024);
        
        Texture g1 = assetManager.loadTexture("Textures/Terrain/tex_G1.png");
        g1.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_1", g1);
        matTerrain.setFloat("DiffuseMap_1_scale", 1024);
        
        Texture s3 = assetManager.loadTexture("Textures/Terrain/tex_S3.png");
        s3.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_2", s3);
        matTerrain.setFloat("DiffuseMap_2_scale", 1024);
        
        Texture s = assetManager.loadTexture("Textures/Terrain/tex_S.png");
        s.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_4", s);
        matTerrain.setFloat("DiffuseMap_4_scale", 1024);
        
        Texture g = assetManager.loadTexture("Textures/Terrain/tex_G.png");
        g.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_5", g);
        matTerrain.setFloat("DiffuseMap_5_scale", 1024);
        
        Texture g2 = assetManager.loadTexture("Textures/Terrain/tex_G2.png");
        g2.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_6", g2);
        matTerrain.setFloat("DiffuseMap_6_scale", 1024);
        
        Texture s2 = assetManager.loadTexture("Textures/Terrain/tex_S2.png");
        s2.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_8", s2);
        matTerrain.setFloat("DiffuseMap_8_scale", 1024);
        
        Texture r = assetManager.loadTexture("Textures/Terrain/tex_R.png");
        r.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_9", r);
        matTerrain.setFloat("DiffuseMap_9_scale", 1024);
        
        Texture c = assetManager.loadTexture("Textures/Terrain/tex_C.png");
        c.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_10", c);
        matTerrain.setFloat("DiffuseMap_10_scale", 1024);
        
        TextureKey hmKey = new TextureKey("Textures/Terrain/terrain.png", false);
        Texture heightMapImage = assetManager.loadTexture(hmKey);
        
        AbstractHeightMap heightMap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightMap.load();
        heightMap.smooth(0.9f, 1);
        
        terrain = new TerrainQuad("terrain", patchSize, mapSize, heightMap.getHeightMap());
        TerrainLodControl control = new TerrainLodControl(terrain, this.app.getCamera());
        control.setLodCalculator(new DistanceLodCalculator(patchSize, 2.7f));
        terrain.addControl(control);
        terrain.setMaterial(matTerrain);
        terrain.setLocalTranslation(425.0f, -572.6f, 475.0f);   
        terrain.setLocalScale(12.8f, 7.6f, 12.8f);
    }

    private void initLight() {
        
        
        /*sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-1, -1, 0).normalize());
        rootNode.addLight(sun);
       
        ambient = new AmbientLight(new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
        rootNode.addLight(ambient);*/
      
    }
    
    private void initShadow() {
        
        
        
        /*shadow = new DirectionalLightShadowRenderer(assetManager, 4096, 4);
        shadow.setLight(sun);
        shadow.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        shadow.setShadowZExtend(1200f);
        viewPort.addProcessor(shadow);*/

    }

    private void initSunAndSky() {
        
        //SkyControler sky = new SkyControler(ambient, sun);
        
        
        /*
        sunControler = new SunControler(sun, app.getCamera());
        skyControler = new SkyControler(sunControler, app.getCamera());
        
        Geometry sunQuad = new Geometry("Sun", new Quad(1.5f, 1.5f));
        sunQuad.setQueueBucket(RenderQueue.Bucket.Sky);
        sunQuad.setCullHint(Spatial.CullHint.Never);
        sunQuad.setShadowMode(RenderQueue.ShadowMode.Off);
        
        Material sunMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        sunMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        sunMat.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/sun.png"));
        sunQuad.setMaterial(sunMat);
        sunQuad.addControl(sunControler);
        
        Geometry sky = new Geometry("sky", new Box(10f, 10f, 10f));
        sky.setQueueBucket(RenderQueue.Bucket.Sky);
        sky.setCullHint(Spatial.CullHint.Never);
        sky.setShadowMode(RenderQueue.ShadowMode.Off);
        Material skyMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        skyMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        sky.setMaterial(skyMat);
        sky.addControl(skyControler);
        rootNode.attachChild(sky);
        rootNode.attachChild(sunQuad);
        */
    }

    private void initWater() {
        /*WaterFilter water = new WaterFilter(rootNode, sun.getDirection());
        water.setWaterHeight(-4f);
        fpp.addFilter(water);*/
    }

    /*@Override
    public void update(float tpf) {
        
        float amb = (sunControler.getHeight()+1)/2;
        float shw = sunControler.getHeight();
        if(shw>0.7){
            shadow.setShadowIntensity(0.7f);
        }
        if(shw<=0.7 && shw>=0){
            shadow.setShadowIntensity(shw);
        }
        if(amb>0.1){
            ambient.setColor(new ColorRGBA(amb, amb, amb, 1f));
        }
    }*/
    
}
