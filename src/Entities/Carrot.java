/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.CarrotController;
import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class Carrot  extends FlyingAI
{
    public Carrot(iSkin _skin) 
    {
        super(_skin);
        mMoveSpeed = 0.5f;
        mCurrentAnimation = "car_fly";
    }
    
    @Override
    public void setMoveSpeed(float _moveSpeed)
    {
        mMoveSpeed = _moveSpeed;
    }
    @Override
    public void update()
    {      
        //mBody.setLinearVelocity(new Vec2(0,0));

        //mSkin.setRotation(mBody.getInertia());
        //mBody.get
        
        mController.update();
        //mBody.applyLinearImpulse(new Vec2(0, -9.8f/mBody.m_mass), new Vec2(0,0));
        //mBody.setAngularDamping(root2);
        //float speed = mBody.getLinearVelocity().x;
        //mSkin.setRotation(speed);//mBody.getAngle()*(180/(float)Math.PI));
    } 
    
    @Override
    public void stun()
    {
        ((CarrotController)mController).stun();
    }
    @Override
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(getBody().getPosition());
        mSkin.setRotation(mBody.getAngle()*180.0f/(float)Math.PI);
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }
}
