/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

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
            if (mWaterHeight < 1.0f)
                changeState(State.eWaterJumping);
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
            if (mWaterHeight < 1.0f)
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
                if(mTimer > 180) //3 seconds
                {
                    changeState(State.eIdle);
                }
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
                if(mTimer > 180) //3 seconds
                {
                    changeState(State.eIdle);
                }
                if (mTarCount == 0)
                {
                    changeState(State.eStillCoveredInTar);
                    mTimer = 0;
                }
                else if (mWaterTiles != 0)
                {
                    changeState(State.eSwimming);
                }
                break;
            }
            case eStillCoveredInTar:
            {
                if (mTarCount != 0)
                {
                    changeState(State.eStandingOnTar);
                }
                else
                {
                    if (mTimer == tarStickingTimer)
                    {
                        changeState(State.eFalling);
                        update();
                    }
                }
                break;
            }
            case eIce:
            {
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
                if(mEntity.mBody.getLinearVelocity().lengthSquared() > 0.01f)
                {
                    changeState(State.eStanding);
                    mTimer = 0;
                    update();
                }
                break;
            }
        }
    }
    private void changeState(State _newState)
    {
        mEntity.mSkin.setAlpha(1f);
        switch(_newState)
        {
            case eDead:
            {
                mEntity.mSkin.setAlpha(0.5f);
                break;
            }       
            case eJumping:
            {
                //Sound.play(mJumpSound);
            }
            case eSwimming:
            {
                int magic = 0;
            }
        }
        mState = _newState;
    }
}
