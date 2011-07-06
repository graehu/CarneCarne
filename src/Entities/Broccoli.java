/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iPathFinding.Command;
import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class Broccoli extends AIEntity
{
    private float mMoveSpeed;
    private String mCurrentAnimation;
    private float mAnimSpeed;
    private float mHopTimmer;
    private float mTimer;
    public Broccoli(iSkin _skin) 
    {
        super(_skin);
        mMoveSpeed = 1;
        mCurrentAnimation = "broc_1";
        mAnimSpeed = 0;
        mHopTimmer = 0;
        mTimer = 0;
        //mSkin.setIsLooping(false);
    }
    
    public void setMoveSpeed(float _moveSpeed)
    {
        mMoveSpeed = _moveSpeed;
    }
    public void update()
    {
        //mBody.applyLinearImpulse(new Vec2(0, -9.8f), new Vec2(0,0));
        mController.update();
        //subUpdate();    
    }
    public void moveRight()
    {
        if(mTimer > 0)
        {
            setAnimation("broc_3");
            //fly(Command.eMoveRight);
            mTimer -= 0.016;
        }
        else
        {
            mBody.applyLinearImpulse(new Vec2(5f,-5f),mBody.getWorldCenter());
            mTimer = mHopTimmer;
        }
    }
    public void moveLeft()
    {
        setAnimation("broc_2");
        //fly(Command.eMoveLeft);
    }
    public void moveUp()
    {
        
    }
    public void moveDown()
    {
        
    }
    public void setAnimation(String _animation)
    {
        if(_animation != mCurrentAnimation)
        {
            mSkin.stopAnim(mCurrentAnimation);
            mCurrentAnimation = _animation;
            mHopTimmer = mSkin.startAnim(_animation, true, mAnimSpeed);
        }
    }
    /*public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }*/
}
