/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import org.jbox2d.dynamics.Body;
import Graphics.iSkin;
import Physics.sPhysics;
import org.jbox2d.common.Vec2;
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
    
    public void render()
    {
        Vec2 pixelPosition = sPhysics.translateToWorld(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }
}
