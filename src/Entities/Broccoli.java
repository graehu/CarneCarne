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
 * @author Graham
 */
public class Broccoli extends AIEntity
{
    private float mHopTimmer;
    private float mTimer;
    
    private enum brocState
    {
        eIdle,
        eIdleJump,
        eJump,
        eAir,
        eLand
    };
    private brocState mState;
    
    public Broccoli(iSkin _skin) 
    {
        super(_skin);
        mJumpTimer = 0;
        mMoveSpeed = 1;
        mAnimSpeed = 1;
        mTouchingTile = null;
        mMoveSpeed = 1;
        mCurrentAnimation = "broc_1";
        //setAnimation("broc_1_idle");
        
        mSkin.deactivateSubSkin("broc_1_idle");
        //mCurrentAnimation = _animation;
        mSkin.activateSubSkin("broc_1_idle", false, mAnimSpeed);
        mAnimSpeed = 1;
        mHopTimmer = 0;
        mTimer = 0;
        mState = brocState.eIdle;

        //mSkin.setIsLooping(false);
    }
    
    public void setMoveSpeed(float _moveSpeed)
    {
        mMoveSpeed = _moveSpeed;
    }
    /*public void update()
    {
        //mBody.applyLinearImpulse(new Vec2(0, -9.8f), new Vec2(0,0));
        
        mController.update();
        //subUpdate();    
    }*/
    /*public void moveRight()
    {
        if(mTimer < 0)
        {
            setAnimation("broc_2");
            mBody.applyLinearImpulse(new Vec2(0.5f ,0), mBody.getWorldCenter());
            mTimer = mHopTimmer+1;
        }
        else
        {
           mTimer -= 0.016;
           if(mTimer > mHopTimmer-0.1f-1 && mTimer < mHopTimmer+0.1f-1)
           {
               jump();
               stopJumping();
           }
        }
    }*/
    public void move(float _speed)
    {
        //setAnimation("broc_2_jump");
        //setAnimation("broc_2_air");
        //mAIEntityState.getState();
        
        AIEntityState.State silly  = mAIEntityState.getState();
        
        if(mState == brocState.eIdle)
        {
            mSkin.deactivateSubSkin("broc_1_idle");
            mCurrentAnimation = "broc_1_jump";
            mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
            mState = brocState.eIdleJump;
        }
        if(mState == brocState.eIdleJump)
        {
            if(!mSkin.isAnimating("broc_1_jump"))
            {
                mSkin.deactivateSubSkin("broc_1_jump");
                mCurrentAnimation = "broc_2_land";
                mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                mState = brocState.eJump;
            }
        }
            if(mState == brocState.eJump)
            {
                mSkin.stopAt("broc_2_land",4);
                if(!mSkin.isAnimating("broc_2_land"))
                {
                    mSkin.deactivateSubSkin("broc_2_land");
                    mCurrentAnimation = "broc_2_jump";
                    mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                    mState = brocState.eAir;
                }
            }
            else if(mState == brocState.eAir)
            {
                mSkin.stopAt("broc_2_jump",3);
                if(!mSkin.isAnimating("broc_2_jump"))
                {
                    mAIEntityState.getState();
                    jump();
                    stopJumping();
                    mBody.applyLinearImpulse(new Vec2(_speed,0), mBody.getWorldCenter());
                    //setAnimation("broc_2_air");
                    mSkin.deactivateSubSkin("broc_2_jump");
                    mCurrentAnimation = "broc_2_air";
                    mSkin.activateSubSkin(mCurrentAnimation, true, mAnimSpeed);
                    mState = brocState.eLand;
                }
            }
            else if(mState == brocState.eLand)
            {
                if(!isAirBorn())
                {
                    mSkin.stopAt("broc_2_air",4);
                    if(!mSkin.isAnimating("broc_2_air"))
                    {
                        mSkin.deactivateSubSkin("broc_2_air");
                        mCurrentAnimation = "broc_2_land";
                        mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                        mState = brocState.eJump;
                    }
                }
            }

        
        /*if(mTimer < 0)
        {
            //jump();
            //stopJumping();
            mBody.applyLinearImpulse(new Vec2(-0.5f,0), mBody.getWorldCenter());
            mTimer = mHopTimmer+1;
        }
        else
        {
            mTimer -= 0.016;
            if(mTimer < 0.112f+0.016f*2.0f)
            {
                setAnimation("broc_2");
            }
        }*/
    }
    public void moveUp()
    {
        
    }
    public void moveDown()
    {
        
    }

    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }//*/
}
