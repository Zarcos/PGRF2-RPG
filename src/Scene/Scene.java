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
import com.jme3.asset.AssetEventListener;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowFilter;
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
public class Scene extends AbstractAppState {

    private SimpleApplication app;
    private AssetManager assetManager;
    private Node rootNode;
    private TerrainQuad terrain;
    private ViewPort viewPort;
    private LightScatteringFilter scattering;
    public FilterPostProcessor fpp;
    public DirectionalLight sun;
    public AmbientLight ambient;
    public DirectionalLightShadowFilter shadow;

    public Node scene;
    public Node npcNode;
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
        this.npcNode = new Node();
        rootNode.attachChild(npcNode);
        stateManager.attach(physics);

        AssetEventListener asl = new AssetEventListener() {
            @Override
            public void assetLoaded(AssetKey key) {

            }

            @Override
            public void assetRequested(AssetKey key) {
                if (key.getExtension().equals("png") || key.getExtension().equals("tga")) {

                    TextureKey tkey = (TextureKey) key;
                    tkey.setAnisotropy(16);
                }
            }

            @Override
            public void assetDependencyNotFound(AssetKey parentKey, AssetKey dependentAssetKey) {

            }
        };

        assetManager.addAssetEventListener(asl);
        initScene();
    }

    private void initScene() {

        initMap();

        scene = new Node();
        Node vesnice = (Node) assetManager.loadModel("Models/vesnice.j3o");
        Node tree = (Node) assetManager.loadModel("Models/tree.j3o");

        initLight();
        initShadow();
        initWater();

        terrain.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        vesnice.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        tree.setShadowMode(RenderQueue.ShadowMode.Cast);
        scene.attachChild(vesnice);
        scene.attachChild(tree);
        scene.attachChild(terrain);

        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(scene);
        RigidBodyControl sceneBody = new RigidBodyControl(sceneShape, 0.0f);
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
        heightMap.smooth(1f);

        terrain = new TerrainQuad("terrain", patchSize, mapSize, heightMap.getHeightMap());
        TerrainLodControl control = new TerrainLodControl(terrain, this.app.getCamera());
        control.setLodCalculator(new DistanceLodCalculator(patchSize, 2.7f));
        terrain.addControl(control);
        terrain.setMaterial(matTerrain);
        terrain.setLocalTranslation(425.0f, -572.6f, 475.0f);
        terrain.setLocalScale(12.8f, 7.6f, 12.8f);
    }

    private void initLight() {

        sun = new DirectionalLight();
        rootNode.addLight(sun);

        ambient = new AmbientLight();
        rootNode.addLight(ambient);

        scattering = new LightScatteringFilter();
        scattering.setLightDensity(0.8f);
        fpp.addFilter(scattering);
    }

    private void initShadow() {

        shadow = new DirectionalLightShadowFilter(assetManager, 1500, 2);
        shadow.setLight(sun);
        shadow.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        fpp.addFilter(shadow);

        DepthOfFieldFilter dof = new DepthOfFieldFilter();
        dof.setFocusDistance(0f);
        dof.setFocusRange(500f);
        dof.setBlurScale(1.2f);
        fpp.addFilter(dof);

    }

    private void initWater() {
        WaterFilter water = new WaterFilter(rootNode, sun.getDirection());
        water.setWaterHeight(-4.1f);
        fpp.addFilter(water);

    }

    @Override
    public void update(float tpf) {
        scattering.setLightPosition(sun.getDirection().mult(-200000f));
    }

}
