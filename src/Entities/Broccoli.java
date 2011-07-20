/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Particles.ParticleSysBase;
import Graphics.Particles.sParticleManager;
import Graphics.Skins.iSkin;
import World.sWorld;
import java.util.Random;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class Broccoli extends AIEntity
{
    private float mHopTimmer;
    private float mTimer;
    private float mIdleTick;
    private boolean mActive;
    
    private enum brocState
    {
        eIdle,
        eIdleOut,
        eIdleIn,
        //Hopping
        eJump,
        eAir,
        eLand,
        //Attacking
        eThreaten,
        eSubmerge,
        eExplode
    };
    private brocState mState;
    
    public Broccoli(iSkin _skin) 
    {
        super(_skin);
        mJumpTimer = 0;
        mMoveSpeed = 1;
        mIdleTick = 0;
        mAnimSpeed = 0.75f;
        mTouchingTile = null;
        mCurrentAnimation = "broc_1_idle";
        mSkin.activateSubSkin("broc_1_idle", false, mAnimSpeed);
        mHopTimmer = 0;
        mTimer = 0;
        mState = brocState.eIdle;
        mActive = false;
    }
    
    public void setMoveSpeed(float _moveSpeed)
    {
        mMoveSpeed = _moveSpeed;
    }
    public void update()
    {
        super.update();
        
        /*if((mState != brocState.eIdle) && (mState != brocState.eAir) && (mState != brocState.eJump) && (mState != brocState.eLand))
        {
             mSkin.deactivateSubSkin(mCurrentAnimation);
             mSkin.activateSubSkin("broc_1_idlein", false, mAnimSpeed);
             mState = brocState.eIdle;
        }
        if(!mSkin.isAnimating("broc_1_idlein"))
        {*/
        if(!mActive)
        {
            if(!(mState == brocState.eIdle))
            {
                mSkin.deactivateSubSkin(mCurrentAnimation);
                mCurrentAnimation = "broc_1_idlein";
                mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);         
                mState = brocState.eIdle;
                mIdleTick = -1;
            }
            else if(mState == brocState.eIdle)
            {
                // mSkin.stopAt("broc_1_idlein",3);
                 if(!mSkin.isAnimating("broc_1_idlein"))
                 {
                    mSkin.deactivateSubSkin("broc_1_idlein");
                    // mSkin.stopAt("broc_1_idle",6);
                    if(mIdleTick < 0)
                    {
                        if(!mSkin.isAnimating("broc_1_idle"))
                        {
                            mSkin.deactivateSubSkin("broc_1_idle");
                            mCurrentAnimation = "broc_1_idle";
                            mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                            Random rand = new Random();
                            mIdleTick = 1+(rand.nextInt()%6);
                        }
                    }
                    else
                    {
                        mIdleTick -= 0.016f;
                    }
                }
            }
        }
        mActive = false;
    }
    public void move(float _speed)
    {
        //setAnimation("broc_2_jump");
        //setAnimation("broc_2_air");
        //mAIEntityState.getState();
        if(mState == brocState.eIdle)
        {
            mSkin.deactivateSubSkin("broc_1_idlein");
            mSkin.deactivateSubSkin("broc_1_idle");
            mCurrentAnimation = "broc_1_idleout";
            mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
            mState = brocState.eIdleOut;
        }
        else if(mState == brocState.eIdleOut)
        {
            mSkin.stopAt("broc_1_idleout",3);
            if(!mSkin.isAnimating("broc_1_idleout"))
            {
                mSkin.deactivateSubSkin("broc_1_idlein");
                mSkin.deactivateSubSkin("broc_1_idleout");
                mCurrentAnimation = "broc_2_jump";
                mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                mState = brocState.eJump;
            }
        }
        else if(mState == brocState.eJump)
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
                jump();
                stopJumping();
                mBody.applyLinearImpulse(new Vec2(_speed,0), mBody.getWorldCenter());
                mSkin.deactivateSubSkin("broc_2_jump");
                mCurrentAnimation = "broc_2_air";
                mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
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
        mActive = true;
    }
    public void attack()
    {
        if(mState == brocState.eLand)
        {
            if(!isAirBorn())
            {
                mSkin.stopAt("broc_2_air",4);
                if(!mSkin.isAnimating("broc_2_air"))
                {
                    mSkin.deactivateSubSkin("broc_2_air");
                    mCurrentAnimation = "broc_3_start";
                    mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                    mState = brocState.eThreaten;
                }
            }
        }
        else if(mState == brocState.eJump)
        {
            mSkin.stopAt("broc_2_land",5);
            if(!mSkin.isAnimating("broc_2_land"))
            {
                mSkin.deactivateSubSkin("broc_2_land");
                mCurrentAnimation = "broc_3_start";
                mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                mState = brocState.eThreaten;
            }
        }
        else if(mState == brocState.eAir)
        {
            mSkin.stopAt("broc_2_jump",4);
            if(!mSkin.isAnimating("broc_2_jump"))
            {
                mSkin.deactivateSubSkin("broc_2_jump");
                mCurrentAnimation = "broc_3_start";
                mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                mState = brocState.eThreaten;
            }
        }
        else if(mState == brocState.eIdle)
        {
            if(mSkin.isAnimating("broc_2_land"))
            {
                mSkin.deactivateSubSkin(mCurrentAnimation);
                mSkin.deactivateSubSkin("broc_1_idlein");
                mSkin.deactivateSubSkin("broc_1_idle");
                mCurrentAnimation = "broc_1_idleout";
                mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                mState = brocState.eIdleOut;
            }
        }
        else if(mState == brocState.eIdleOut)
        {
            mSkin.stopAt("broc_1_idleout",3);
            if(!mSkin.isAnimating("broc_1_idleout"))
            {
                mSkin.deactivateSubSkin("broc_1_idlein");
                mSkin.deactivateSubSkin("broc_1_idleout");
                mCurrentAnimation = "broc_3_start";
                mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                mState = brocState.eThreaten;
            }
        }
        else if(mState == brocState.eThreaten)
        {
            mSkin.stopAt("broc_3_start",2);
            if(!mSkin.isAnimating("broc_3_start"))
            {
                mSkin.deactivateSubSkin("broc_3_start");
                mCurrentAnimation = "broc_3_mid";
                mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                mState = brocState.eSubmerge;
                sParticleManager.createSystem("MelonSDamageParticle", 
                        sWorld.translateToWorld(mBody.getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32))
                        , 1);
            }
        }
        else if(mState == brocState.eSubmerge)
        {
            mSkin.stopAt("broc_3_mid",2);
            if(!mSkin.isAnimating("broc_3_mid"))
            {
                mSkin.deactivateSubSkin("broc_3_mid");
                mCurrentAnimation = "broc_3_end";
                mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                mState = brocState.eExplode;
                
                    /*ParticleSysBase sys = sParticleManager.createSystem("broccoliExplode", 
                    sWorld.translateToWorld(mBody.getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32))
                    , 0.001f);*/
                /*ParticleSysBase sys = sParticleManager.createSystem("broccoliExplode", 
                    sWorld.translateToWorld(mBody.getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32))
                    , 1);*/
                //sys.setWind(10);
                //sys.setAngularOffset(180);
            }
        }
        else if(mState == brocState.eExplode)
        {
            if(!mSkin.isAnimating("broc_3_end"))
            { 
                mSkin.deactivateSubSkin(mCurrentAnimation);
                mSkin.activateSubSkin(mCurrentAnimation, false, mAnimSpeed);
                /*ParticleSysBase sys = sParticleManager.createSystem("MelonSDamageParticle", 
                    sWorld.translateToWorld(mBody.getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32))
                    , 1);*/
                            ;//*/
                //sys.setWind(40);
                //sys.setAngularOffset(270);
            }
            
        }
        mActive = true;        
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
