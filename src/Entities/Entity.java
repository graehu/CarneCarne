/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import org.jbox2d.dynamics.Body;
import Graphics.iSkin;
/**
 *
 * @author alasdair
 */
abstract public class Entity {
    
    public Body mBody;
    protected iSkin mSkin;
    
    public Entity(iSkin _skin)
    {
        mSkin = _skin;
    }
    
    abstract public void update();
    abstract public void render();
}
