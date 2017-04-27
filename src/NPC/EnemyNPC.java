/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NPC;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Zarcos
 */
public class EnemyNPC extends Node {

    public CharacterControl npcPhys;
    public Node model;
    public int actualHp;
    public int fullHp;
    public int experience;
    public int damage;
    public Vector3f position;

    public EnemyNPC(int hp, Vector3f position, int experience, int damage) {
        this.fullHp = hp;
        this.actualHp = hp;
        this.position = position;
        this.experience = experience;
        this.damage = damage;
    }

}
