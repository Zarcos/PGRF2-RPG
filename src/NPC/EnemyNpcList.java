/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NPC;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;

/**
 *
 * @author Zarcos
 */
public class EnemyNpcList extends AbstractAppState {

    private SimpleApplication app;
    private AppStateManager stateManager;

    //NPC = HP, Position, Experience, Damage
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication) app;
        this.stateManager = this.app.getStateManager();
        this.stateManager.attach(new EnemyNpcControler(100, new Vector3f(-60, 10, -60), 20, 14));
        this.stateManager.attach(new EnemyNpcControler(100, new Vector3f(-70, 10, -60), 20, 18));
    }
}
