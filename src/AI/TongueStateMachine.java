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
    
    static int tongueFiringTimeout = 100;
    static int idleAnimationTrigger = 10000;
    Vec2 position;
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
    }
    private boolean grabBlock()
    {
        return mAIController.grabBlock(position);
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
    public void rightClick()
    {
        switch (mState)
        {
            case eStart:
            {
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
    public void leftRelease()
    {
        switch (mState)
        {
            case eStart:
            {
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
    public void rightRelease()
    {
        switch (mState)
        {
            case eStart:
            {
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
        mState = _state;
        currentStateTimer = 0;
    }
}
