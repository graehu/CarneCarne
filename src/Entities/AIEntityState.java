/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Particles.sParticleManager;
import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class AIEntityState
{
    static private final int tarStickingTimer = 60;
    static private final int jumpReload = 13;
    static private final int jumpBoostTimer = 10;
    static private final int stunTimer = 120;
    static private final int mDoubleJumpTimer = 600000;

    void stun()
    {
        changeState(State.eStunned);
        mTimer = 0;
    }
    enum State
    {
        eIdle,
        eFalling,
        eFallingDoubleJumped,
        eStanding,
        eSwimming,
        eStandingOnTar,
        eStillCoveredInTar,
        eIce,
        eDead,
        eRestartingRace,
        eJumping,
        eJumpTransition, /// Between jumping and falling, this is where the button held increased jump is determined
        eStunned,
        eWaterJumping,
        eStatesMax,
    }
    private State mState;
    private int mTarCount, mIceCount, mContactCount;
    private int mWaterTiles;
    private float mWaterHeight;
    private int mTimer;
    private AIEntity mEntity;
    private String mJumpSound = "jump";
    
    AIEntityState(AIEntity _entity)
    {
        mEntity = _entity;
        mState = State.eFalling;
        mTarCount = mIceCount = mWaterTiles = mContactCount = mTimer = 0;
    }
    
    void destroy()
    {
        mEntity = null;
    }
    
    State getState()
    {
        return mState;
    }
    
    void update(int _tarCount, int _iceCount, int _waterCount, int _contactCount, float _waterHeight)
    {
        mTarCount = _tarCount;
        mIceCount = _iceCount;
        mWaterTiles = _waterCount;
        mContactCount = _contactCount;
        mWaterHeight = _waterHeight;
        mTimer++;
        update();
    }
    void restartingRace()
    {
        assert(mState.equals(State.eDead));
        changeState(State.eRestartingRace);
    }
    void kill()
    {
        if (!mState.equals(State.eRestartingRace))
        {
            changeState(State.eDead);
            mTimer = 0;
        }
    }
    void unkill()
    {
        changeState(State.eFalling);
        update();
    }
    void jump()
    {
        if (mState.equals(State.eFalling))
        {
            changeState(State.eFallingDoubleJumped);
        }
        else if (mState.equals(State.eJumpTransition))
        {
            //changeState(State.eJumping);
        }
        else if (mState.equals(State.eSwimming))
        {
            //if (mWaterHeight < 1.0f)
            //    changeState(State.eWaterJumping);
        }
        else
        {
            changeState(State.eJumping);
            mTimer = 0;
        }
    }
    void stopJumping()
    {
        changeState(State.eFalling);
        update();
    }
    void stopIdle()
    {
        mTimer = 0;
        changeState(State.eStanding);
        update();
    }
    //returns -ve for initialial jump, 0.0f while falling and +ve during transition
    public float canJump(float _currentVelocity)
    {
        if (mState.equals(State.eJumping) || mState.equals(State.eFallingDoubleJumped)||
                mState.equals(State.eStunned) || mState.equals(State.eWaterJumping))
        {
            return 0.0f;
        }
        if (mState.equals(State.eFalling))
        {
            if (mTimer < mDoubleJumpTimer)
            {
                return 0.0f;
            }
        }
        if (mState.equals(State.eJumpTransition))
        {
            return _currentVelocity-5.3f;
        }
        if (mState.equals(State.eSwimming))
        {
            if (mWaterHeight < 1.0f && _currentVelocity < 0.0f)
                return -7.2f;
            else
                return _currentVelocity-0.2f;
        }
        return -7.2f;
        
    }
    public int getWaterHeight()
    {
        return mWaterTiles;
    }
    
    private void update()
    {
        switch (mState)
        {
            case eFalling:
            case eFallingDoubleJumped:
            {
                if (mContactCount != 0)
                {
                    changeState(State.eStanding);
                    update();
                }
                else if (mWaterTiles != 0)
                {
                    changeState(State.eSwimming);
                }
                /*else if (mTarCount != 0)
                {
                    changeState(State.eStandingOnTar);
                }
                else if (mIceCount != 0)
                {
                    changeState(State.eIce);
                }*/
                /*if (mTimer == jumpBoostTimer)
                {
                    changeState(State.eJumpTransistion);
                }*/
                break;
            }
            case eWaterJumping:
            {
                if (mWaterTiles == 0)
                {
                    changeState(State.eFalling);
                    update();
                }
                else if (mTimer == jumpBoostTimer-1)
                {
                    changeState(State.eSwimming);
                    update();
                }
                break;
            }
            case eJumping:
            {
                if (mTimer == jumpBoostTimer)
                {
                    changeState(State.eJumpTransition);
                }
                /*else if (mContactCount == 0)
                {
                    changeState(State.eStanding);
                    update();
                }
                else if (mTimer == jumpReload)
                {
                    changeState(State.eStanding);
                    update();
                }*/
                break;
            }
            case eJumpTransition:
            {
                if (mTimer == jumpBoostTimer+1)
                {
                    changeState(State.eStanding);
                    update();
                }
                break;
            }
            case eStanding:
            {
                if (mContactCount == 0)
                {
                    changeState(State.eFalling);
                    update();
                }
                else if (mWaterTiles != 0)
                {
                    changeState(State.eSwimming);
                }
                else if (mTarCount != 0)
                {
                    changeState(State.eStandingOnTar);
                }
                else if (mIceCount != 0)
                {
                    changeState(State.eIce);
                }
                else if(mTimer > 180) //3 seconds
                {
                    changeState(State.eIdle);
                    update();
                }
                break;
            }
            case eSwimming:
            {
                if (mWaterTiles == 0)
                {
                    changeState(State.eFalling);
                    update();
                }
                break;
            }
            case eStandingOnTar:
            {
                if (mTarCount == 0)
                {
                    changeState(State.eStillCoveredInTar);
                    mTimer = 0;
                }
                else if (mWaterTiles != 0)
                {
                    changeState(State.eSwimming);
                }
                else if(mTimer > 180) //3 seconds
                {
                    changeState(State.eIdle);
                    update();
                }
                break;
            }
            case eStillCoveredInTar:
            {
                if (mTarCount != 0)
                {
                    changeState(State.eStandingOnTar);
                }
                else if(mTimer <= 180) //3 seconds
                {
                    if (mTimer == tarStickingTimer)
                    {
                        changeState(State.eFalling);
                        update();
                    }
                }
                else 
                {
                    changeState(State.eIdle);
                    update();
                }
                break;
            }
            case eIce:
            {
                if(mTimer > 180) //3 seconds
                {
                    changeState(State.eIdle);
                    update();
                }
                if (mIceCount == 0)
                {
                    changeState(State.eFalling);
                    update();
                }
                else if (mWaterTiles != 0)
                {
                    changeState(State.eSwimming);
                }
                else if (mTarCount != 0)
                {
                    changeState(State.eStandingOnTar);
                }
                break;
            }
            case eStunned:
            {
                if (mTimer == stunTimer)
                {
                    changeState(State.eFalling);
                    update();
                }
            }
            case eDead:
            {
                break;
            }
            case eIdle:
            {
                mTimer = 0;
                if(mEntity.mBody.getLinearVelocity().lengthSquared() > 0.000001f)
                {
                    changeState(State.eStanding);
                    update();
                }
                break;
            }
        }
    }
    private void changeState(State _newState)
    {
        if(mState.equals(_newState))
            return;
        mEntity.mSkin.setAlpha(1f);
        switch(mState) //old
        {
            case eSwimming:
            {
                //on jump
                if(_newState.equals(State.eFalling) || _newState.equals(State.eJumping))
                    sParticleManager.createSystem(  "WaterJumpSmall",
                                                    sWorld.translateToWorld(mEntity.getBody().getPosition())
                                                        .sub(sWorld.getPixelTranslation())
                                                        .add(new Vec2(32,64)), /*Offset to bottom of carne*/
                                                    3.0f);
            }
        }
        switch(_newState)
        {
            case eRestartingRace:
            case eDead:
            {
                mEntity.mSkin.setAlpha(0.5f);
                mEntity.mSkin.setAlpha("carne_fly", 1.0f);
                break;
            }       
            case eJumping:
            {
                //Sound.play(mJumpSound);
            }
            case eSwimming:
            {
                //on hit water
                if(mState.equals(State.eFalling))
                {
                    String particle = "WaterJumpSmall";
                    if(mEntity.getBody().getLinearVelocity().lengthSquared() > 80)
                    {
                        particle = "WaterJumpBig";
                    }
                    sParticleManager.createSystem(  particle,
                                                    sWorld.translateToWorld(mEntity.getBody().getPosition())
                                                        .sub(sWorld.getPixelTranslation())
                                                        .add(new Vec2(32,64)), /*Offset to bottom of carne*/
                                                    3.0f);
                }
            }
        }
        mState = _newState;
    }
}
