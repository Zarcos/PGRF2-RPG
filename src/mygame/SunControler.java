/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Zarcos
 */
public class SunControler extends AbstractControl{
    
    private float time = 1;
    private final float timeFactor;
    private final Vector3f position;
    private final DirectionalLight directionalLight;
    private final Camera cam;
    private final ColorRGBA dayColor, eveningColor;
    private final ColorRGBA sunColor;
    private float height;

    public SunControler(DirectionalLight directionalLight, Camera cam) {
        
        
        this.directionalLight = directionalLight;
        this.cam = cam;
        timeFactor = 0.1f;
        position = new Vector3f();
        dayColor = new ColorRGBA(ColorRGBA.White);
        eveningColor = new ColorRGBA(ColorRGBA.Orange);
        sunColor = new ColorRGBA();
    }
    

    @Override
    protected void controlUpdate(float tpf) {
        
        float x = FastMath.cos(time) * 10f;
        float z = FastMath.sin(time) * 10f;
        float y = FastMath.sin(time) * 5f;
        position.set(x, y, z);
        spatial.lookAt(cam.getLocation(), Vector3f.UNIT_Y);
        spatial.setLocalTranslation(cam.getLocation().add(position));
        directionalLight.setDirection(position.negate());
        height = y/5f;
        if(height>0){
            directionalLight.setColor(ColorRGBA.White);
        } else {
            directionalLight.setColor(new ColorRGBA().interpolateLocal(ColorRGBA.White, ColorRGBA.Black, FastMath.pow(height, 2)));
        }
        sunColor.interpolateLocal(eveningColor, dayColor, FastMath.pow(height, 2));
        
        ((Geometry)spatial).getMaterial().setColor("Color", sunColor);
        
        time += tpf * timeFactor;
        time = time % FastMath.TWO_PI;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    
    public float getHeight(){
        return height;
    }
    
}
