/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
package mygame;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Zarcos
 */
/*
public class SkyControler extends AbstractControl{
    
    private final Camera cam;
    private final ColorRGBA dayColor, eveningColor, nightColor;
    private final ColorRGBA color;
    private final SunControler sun;

    public SkyControler(SunControler sun, Camera cam) {
        
        this.cam = cam;
        this.sun = sun;
        
        color = new ColorRGBA();
        
        dayColor = new ColorRGBA(0.5f, 0.5f, 1f, 1f);
        eveningColor = new ColorRGBA(1f, 0.7f, 0.5f, 1f);
        nightColor = new ColorRGBA(0.1f, 0.1f, 0.2f, 1f);
    }
    

    @Override
    protected void controlUpdate(float tpf) {
        
        spatial.setLocalTranslation(cam.getLocation());
        
        if(sun.getHeight() > 0) {
            
            color.interpolateLocal(eveningColor, dayColor, FastMath.pow(sun.getHeight(), 2));
            
        } else {
 
            color.interpolateLocal(eveningColor, nightColor, FastMath.pow(sun.getHeight(), 2));
            
        }
        
        ((Geometry)spatial).getMaterial().setColor("Color", color);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }

    public ColorRGBA getColor() {
        return color;
    }
    
}*/
