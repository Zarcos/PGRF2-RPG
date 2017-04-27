/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.scene.Node;

/**
 *
 * @author Zarcos
 */
public class Player extends Node {

    public CharacterControl playerPhys;
    public Node model;
    public int fullHp = 100;
    public int actualHp = 100;
    public int experience = 0;
    public int level = 1;
    public int nextLevelConst = 20;
    public int experiencesToLevel = nextLevelConst;
    public boolean isDead = false;
}
