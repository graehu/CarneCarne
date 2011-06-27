/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import org.jbox2d.common.Vec2;

/**
 *
 * @author G203947
 */
public class Carrot  extends Entity
{
    private float mMoveSpeed;

    public Carrot(iSkin _skin) 
    {
        super(_skin);
        mMoveSpeed = 1;
    }
    
    public void setMoveSpeed(float _moveSpeed)
    {
        mMoveSpeed = _moveSpeed;
    }
    public void update()
    {
        mBody.applyLinearImpulse(new Vec2(0, -9.8f), new Vec2(0,0));
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
    
        
}
