/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;

/**
 *
 * @author Graham
 */
public class Carrot  extends FlyingAI
{
    //private float mMoveSpeed;

    public Carrot(iSkin _skin) 
    {
        super(_skin);
        mMoveSpeed = 0.5f;
    }
    
    public void setMoveSpeed(float _moveSpeed)
    {
        mMoveSpeed = _moveSpeed;
    }
    public void update()
    {      
        //mBody.setLinearVelocity(new Vec2(0,0));
        mController.update();
        //mBody.applyLinearImpulse(new Vec2(0, -9.8f/mBody.m_mass), new Vec2(0,0));
    } 
}
