/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author g203947
 */
public class Broccoli extends AIEntity
{
    private float mMoveSpeed;
    public Broccoli(iSkin _skin) 
    {
        super(_skin);
        mMoveSpeed = 1;
        //mSkin.setIsLooping(false);
    }
    
    public void setMoveSpeed(float _moveSpeed)
    {
        mMoveSpeed = _moveSpeed;
    }
    public void update()
    {
        //mBody.applyLinearImpulse(new Vec2(0, -9.8f), new Vec2(0,0));
        //mController.update();
        //subUpdate();    
    }
    public void moveRight()
    {
        
    }
    public void moveLeft()
    {
        
    }
    public void moveUp()
    {
        
    }
    public void moveDown()
    {
        
    }
    /*public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }*/
}
