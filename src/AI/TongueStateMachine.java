/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import org.jbox2d.common.Vec2;

/**
 *
 * @author A203946
 */
public class TongueStateMachine {
    
    static int tongueFiringTimeout = 60;
    static int hammeringTimeout = 60;
    static int idleAnimationTrigger = 60;
    static int tongueRetractWithBlockTime = 60;
    
    Vec2 position; /// FIXME unneccessary
    String mBlockMaterial;
    enum State
    {
        eStart,
        eFiringTongue,
        eRetractingTongue,
        eStuckToBlock,
        eRetractingWithBlock,
        eFoodInMouth,
        eFiringHammer,
        eRetractingHammer,
        eSpittingBlock,
        eSpitting,
        eIdleAnimation,
        eStatesMax
    }
    State mState;
    int currentStateTimer;
    PlayerInputController mAIController;
    public TongueStateMachine(PlayerInputController _aIController)
    {
        mAIController = _aIController;
        mState = State.eStart;
        currentStateTimer = 0;
        mBlockMaterial = "SomeSoftMaterial";
    }
    private boolean grabBlock()
    {
        return mAIController.grabBlock(position);
    }
    private boolean hammerCollide()
    {
        return false;
    }
    private boolean hasFood()
    {
        return false;
    }
    private int setAnimation(String _name)
    {
        return 60;
    }
    public void tick()
    {
        switch (mState)
        {
            case eStart:
            {
                break;
            }
            case eFiringTongue:
            {
                currentStateTimer++;
                if (grabBlock())
                {
                    changeState(State.eStuckToBlock);
                }
                else if (currentStateTimer > tongueFiringTimeout)
                {
                    changeState(State.eRetractingTongue);
                }
                break;
            }
            case eRetractingTongue:
            {
                currentStateTimer--;
                if (currentStateTimer == 0)
                {
                    changeState(State.eStart);
                }
                break;
            }
            case eStuckToBlock:
            {
                if (mBlockMaterial.equals("SomeSoftMaterial"))
                {
                    changeState(State.eRetractingWithBlock);
                }
                break;
            }
            case eRetractingWithBlock:
            {
                currentStateTimer--;
                if (currentStateTimer == 0)
                {
                    changeState(State.eFoodInMouth);
                }
                break;
            }
            case eFoodInMouth:
            {
                break;
            }
            case eFiringHammer:
            {
                currentStateTimer++;
                if (hammerCollide())
                {
                    changeState(State.eRetractingHammer);
                }
                else if (currentStateTimer == hammeringTimeout)
                {
                    changeState(State.eRetractingHammer);
                }
                break;
            }
            case eRetractingHammer:
            {
                currentStateTimer--;
                if (currentStateTimer == 0)
                {
                    changeState(State.eFoodInMouth);
                }
                break;
            }
            case eSpittingBlock:
            {
                currentStateTimer--;
                if (currentStateTimer == 0)
                {
                    if (hasFood())
                    {
                        changeState(State.eFoodInMouth);
                    }
                    else
                    {
                        changeState(State.eStart);
                    }
                }
                break;
            }
            case eSpitting:
            {
                currentStateTimer--;
                if (currentStateTimer == 0)
                {
                    changeState(State.eStart);
                }
                break;
            }
            case eIdleAnimation:
            {
                currentStateTimer--;
                if (currentStateTimer == 0)
                {
                    changeState(State.eStart);
                }
                break;
            }
        }
    }
    public void leftClick(Vec2 _position)
    {
        position = _position;
        switch (mState)
        {
            case eStart:
            {
                changeState(State.eFiringTongue);
                break;
            }
            case eFiringTongue:
            {
                /// assert(false);
                break;
            }
            case eRetractingTongue:
            {
                break;
            }
            case eStuckToBlock:
            {
                /// assert(false);
                break;
            }
            case eRetractingWithBlock:
            {
                break;
            }
            case eFoodInMouth:
            {
                changeState(State.eFiringHammer);
                break;
            }
            case eFiringHammer:
            {
                break;
            }
            case eRetractingHammer:
            {
                break;
            }
            case eSpittingBlock:
            {
                break;
            }
            case eSpitting:
            {
                break;
            }
            case eIdleAnimation:
            {
                break;
            }
        }
        
    }
    public void rightClick()
    {
        switch (mState)
        {
            case eStart:
            {
                changeState(State.eSpitting);
                break;
            }
            case eFiringTongue:
            {
                /// Do something here? 
                break;
            }
            case eRetractingTongue:
            {
                break;
            }
            case eStuckToBlock:
            {
                break;
            }
            case eRetractingWithBlock:
            {
                break;
            }
            case eFoodInMouth:
            {
                changeState(State.eSpittingBlock);
                break;
            }
            case eFiringHammer:
            {
                break;
            }
            case eRetractingHammer:
            {
                break;
            }
            case eSpittingBlock:
            {
                break;
            }
            case eSpitting:
            {
                break;
            }
            case eIdleAnimation:
            {
                break;
            }
        }
    }
    public void leftRelease()
    {
        switch (mState)
        {
            case eStart:
            {
                /// assert(false);
                break;
            }
            case eFiringTongue:
            {
                changeState(State.eRetractingTongue);
                break;
            }
            case eRetractingTongue:
            {
                break;
            }
            case eStuckToBlock:
            {
                changeState(State.eRetractingTongue);
                break;
            }
            case eRetractingWithBlock:
            {
                break;
            }
            case eFoodInMouth:
            {
                break;
            }
            case eFiringHammer:
            {
                break;
            }
            case eRetractingHammer:
            {
                break;
            }
            case eSpittingBlock:
            {
                break;
            }
            case eSpitting:
            {
                break;
            }
            case eIdleAnimation:
            {
                break;
            }
        }
    }
    public void rightRelease()
    {
        switch (mState)
        {
            case eStart:
            {
                /// assert(false);
                break;
            }
            case eFiringTongue:
            {
                break;
            }
            case eRetractingTongue:
            {
                break;
            }
            case eStuckToBlock:
            {
                break;
            }
            case eRetractingWithBlock:
            {
                break;
            }
            case eFoodInMouth:
            {
                break;
            }
            case eFiringHammer:
            {
                break;
            }
            case eRetractingHammer:
            {
                break;
            }
            case eSpittingBlock:
            {
                break;
            }
            case eSpitting:
            {
                break;
            }
            case eIdleAnimation:
            {
                break;
            }
        }
    }
    private void changeState(State _state)
    {
        switch (_state)
        {
            case eStart:
            {
                currentStateTimer = 0;
                break;
            }
            case eFiringTongue:
            {
                break;
            }
            case eRetractingTongue:
            {
                break;
            }
            case eStuckToBlock:
            {
                break;
            }
            case eRetractingWithBlock:
            {
                currentStateTimer = tongueRetractWithBlockTime;
                break;
            }
            case eFoodInMouth:
            {
                currentStateTimer = 0;
                break;
            }
            case eFiringHammer:
            {
                currentStateTimer = 0;
                break;
            }
            case eRetractingHammer:
            {
                break;
            }
            case eSpittingBlock:
            {
                currentStateTimer = setAnimation("SpittingBlock");
                break;
            }
            case eSpitting:
            {
                currentStateTimer = setAnimation("Spitting");
                break;
            }
            case eIdleAnimation:
            {
                currentStateTimer = setAnimation("Idle");
                break;
            }
        }
        mState = _state;
    }
}
