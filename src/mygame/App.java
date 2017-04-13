package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.CompareMode;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.shadow.PssmShadowFilter;
import com.jme3.shadow.PssmShadowRenderer;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class App extends SimpleApplication implements ActionListener{
    
    private boolean left = false, right = false, up = false, down = false;
    private Spatial map;
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private BetterCharacterControl player;
    private final Vector3f walkDirection = new Vector3f();
    private Geometry playerModel;
    private final Vector3f camDir = new Vector3f();
    private final Vector3f camLeft = new Vector3f();
    private float playerSpeed;
    private float strafeSpeed;
    private float headHeight;


    public static void main(String[] args) {
        App app = new App();
        app.start();
        
    }

    @Override
    public void simpleInitApp() {
        
        playerSpeed = 30f;
        strafeSpeed = 20f;
        headHeight = 3f;
        
        
        
        /** Player Model */
        Box box1 = new Box(1f, 1f, 1f);
        playerModel = new Geometry("Box", box1);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Gray);
        playerModel.setMaterial(mat);
        playerModel.setLocalTranslation(new Vector3f(0, 6, 0));
        rootNode.attachChild(playerModel);
        
        
        cam.setFrustumPerspective(45f,(float) cam.getWidth()/cam.getHeight(), 0.01f, 2000);
        cam.setLocation(new Vector3f(0, 6f, 0));
        
        bulletAppState = new BulletAppState();
        bulletAppState.setDebugEnabled(false);
        stateManager.attach(bulletAppState);
        
        setUpKeys();
        setUplight();
        
        map = assetManager.loadModel("Scenes/island.j3o");
        
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node)map);
        landscape = new RigidBodyControl(sceneShape, 0);
        map.addControl(landscape);
        
        player = new BetterCharacterControl(1f, 7f, 0f);
        player.setPhysicsDamping(1);
        player.setJumpForce(new Vector3f(0, 5, 0));
        player.setGravity(new Vector3f(0, 10, 0));
        player.warp(new Vector3f(0, 6, 0));
        
        rootNode.attachChild(map);
        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);
        
        playerModel.addControl(player);
    }

    @Override
    public void simpleUpdate(float tpf) {
        camDir.set(cam.getDirection()).multLocal(playerSpeed, 0, playerSpeed);
        camLeft.set(cam.getLeft()).multLocal(strafeSpeed);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        player.setWalkDirection(walkDirection);
        
        cam.setLocation(new Vector3f(
                playerModel.getLocalTranslation().x,
                playerModel.getLocalTranslation().y + headHeight,
                playerModel.getLocalTranslation().z)
        );
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
     private void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
  }

    private void setUplight() {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-1, -1, 0).normalize());
        rootNode.addLight(sun);
        
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.2f, 0.2f, 0.2f, 1f));
        rootNode.addLight(ambient);
        
        
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 2048, 3);
        dlsr.setLight(sun);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        dlsr.setShadowCompareMode(CompareMode.Software);
        viewPort.addProcessor(dlsr);
        
        //Nastavit lepší hodnoty
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        SSAOFilter ssaoFilter = new SSAOFilter(2.94f, 11.92f, 0.33f, 0.61f);
        fpp.addFilter(ssaoFilter);
        viewPort.addProcessor(fpp);
    }

    @Override
    public void onAction(String binding, boolean isPressed, float tpf) {
        switch (binding) {
            case "Left":
                left = isPressed;
                break;
            case "Right":
                right= isPressed;
                break;
            case "Up":
                up = isPressed;
                break;
            case "Down":
                down = isPressed;
                break;
            case "Jump":
                if (isPressed) { player.jump(); }
                break;
            default:
                break;
        }
    }
}
