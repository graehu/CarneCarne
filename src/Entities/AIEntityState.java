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
    static private final int jumpReload = 15;
    static private final int jumpBoostTimer = 10;
    static private final int mDoubleJumpTimer = 600000;
    enum State
    {
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
        eJumpTransistion, /// Between jumping and falling, this is where the button held increased jump is determined
        eStatesMax,
    }
    private State mState;
    private int mTarCount, mIceCount, mContactCount;
    private int mWaterHeight;
    private int mTimer;
    private AIEntity mEntity;
    
    AIEntityState(AIEntity _entity)
    {
        mEntity = _entity;
        mState = State.eFalling;
        mTarCount = mIceCount = mWaterHeight = mContactCount = mTimer = 0;
    }
    
    State getState()
    {
        return mState;
    }
    
    void update(int _tarCount, int _iceCount, int _waterCount, int _contactCount)
    {
        mTarCount = _tarCount;
        mIceCount = _iceCount;
        mWaterHeight = _waterCount;
        mContactCount = _contactCount;
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
        else if (mState.equals(State.eJumpTransistion))
        {
            //changeState(State.eJumping);
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
    public float canJump(float _currentVelocity)
    {
        if (mState.equals(State.eJumping) || mState.equals(State.eFallingDoubleJumped))
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
        if (mState.equals(State.eJumpTransistion))
        {
            return _currentVelocity-3.0f;
        }
        return -5.0f;
        
    }
    public int getWaterHeight()
    {
        return mWaterHeight;
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
                /*if (mTimer == jumpBoostTimer)
                {
                    changeState(State.eJumpTransistion);
                }*/
                break;
            }
            case eJumping:
            {
                if (mTimer == jumpBoostTimer)
                {
                    changeState(State.eJumpTransistion);
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
            case eJumpTransistion:
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
                else if (mWaterHeight != 0)
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
                if (mWaterHeight == 0)
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
                else if (mWaterHeight != 0)
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
                else if (mWaterHeight != 0)
                {
                    changeState(State.eSwimming);
                }
                else if (mTarCount != 0)
                {
                    changeState(State.eStandingOnTar);
                }
                break;
            }
            case eDead:
            {
                break;
            }
        }
    }
    private void changeState(State _newState)
    {
        switch(_newState)
        {
            case eDead:
            {
                mEntity.mSkin.setAlpha(0.5f);
                break;
            }
            default:
            {
                mEntity.mSkin.setAlpha(1f);
                break;
            }
                    
        }
        mState = _newState;
    }
}
