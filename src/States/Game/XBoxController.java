/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.AnalogueStickEvent;
import Events.KeyDownEvent;
import Events.KeyUpEvent;
import Events.MapClickEvent;
import Events.MapClickReleaseEvent;
import Events.RightStickEvent;
import Events.sEvents;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Input;

/**
 *
 * @author alasdair
 */
/* XBox Controls
 * 0 - A
 * 1 - B
 * 2 - X
 * 3 - Y
 * 4 - L1
 * 5 - R1
 * 6 - Back
 * 7 - Start
 * 8 - L3
 * 9 - R3
 */
//// Clean up this class with arrays and shit, so the input can be pluggable FIXME
public class XBoxController 
{
    static private float stickEpsilon = 0.3f; /// TWEAK
    static private float shoulderButtonEpsilon = 0.3f;
    private boolean xAxisHit = false;
    private boolean mJumpButton = false, mRightShoulderButton = false;
    //private boolean yAxisHit = false;
    private enum TriggerState
    {
        eStart, // This is the value for the controller sending out -1 continously because its stupid
        eNotPressed,
        eRightPressed,
        eLeftPressed,
        eTriggerStateMax,
    }
    private TriggerState mTriggerState = TriggerState.eStart;
    int mPlayer;
    public XBoxController(int _player)
    {
        mPlayer = _player;
    }
    
    public boolean update(Input _input)
    {
        if(_input.getAxisCount(mPlayer) < 2)
            return false;
        
        //get player direction
        Vec2 rightStick = new Vec2(_input.getAxisValue(mPlayer, 3),_input.getAxisValue(mPlayer, 2));
        
        if (rightStick.length() < 0.2f)
        {
            rightStick = new Vec2(_input.getAxisValue(mPlayer, 1),_input.getAxisValue(mPlayer, 0));
        }
        if (rightStick.length() > 0.2f)
        {
            sEvents.triggerEvent(new RightStickEvent(rightStick, mPlayer));
        }
        
        //handle start and back buttons
        if(_input.isButtonPressed(7, mPlayer)) //start
        {
            sEvents.triggerEvent(new KeyDownEvent('Q', 0)); //menu
        }
        if(_input.isButtonPressed(6, mPlayer)) //back
        {
            sEvents.triggerEvent(new KeyDownEvent('r', mPlayer)); //menu
        }
        if(_input.isButtonPressed(4, mPlayer)) 
        {
            sEvents.triggerEvent(new MapClickEvent(rightStick,"Hammer", mPlayer));
        }
        //handle shoulder buttons
        if(_input.isButtonPressed(5, mPlayer))
        {
            sEvents.triggerEvent(new MapClickEvent(rightStick,"Tongue", mPlayer));
            mRightShoulderButton = true;
        }
        else if(mRightShoulderButton)//on release
        {
            mRightShoulderButton = false;
            sEvents.triggerEvent(new MapClickReleaseEvent(rightStick,"Tongue", mPlayer));
        }
        if(_input.isButtonPressed(0, mPlayer)) //jump
        {
            sEvents.triggerEvent(new KeyDownEvent('w', mPlayer));
            mJumpButton = true;
        }
        else if(mJumpButton)//on release
        {
            sEvents.triggerEvent(new KeyUpEvent('w', mPlayer));
            mJumpButton = false;
            
        }
        
        //handle shoulder triggers
        float xAxis = _input.getAxisValue(mPlayer, 1);
        float xStickEpsilon = 0.5f;
        if (xAxisHit)
        {
            if (xAxis > xStickEpsilon || xAxis < -xStickEpsilon)
            {
                sEvents.triggerEvent(new AnalogueStickEvent(xAxis, mPlayer)); //move
            }
        }
        else if (xAxis != -1.0f)
        {
            xAxisHit = true;
        }
        
        float shoulderButtons =_input.getAxisValue(mPlayer,4);
        if (mTriggerState != TriggerState.eStart || shoulderButtons != -1.0f)
        {
            /*if (shoulderButtons > shoulderButtonEpsilon) //left trigger
            {
                changeState(TriggerState.eLeftPressed, rightStick);
            }
            else */if (shoulderButtons < -shoulderButtonEpsilon) //right trigger
            {
                changeState(TriggerState.eRightPressed, rightStick);
            }
            else
            {
                changeState(TriggerState.eNotPressed, rightStick);
            }
        }     
        
        return true;
    }
    //statemachine required to determine releases for triggers
    private void changeState(TriggerState _newState, Vec2 _rightStick)
    {
        switch (mTriggerState) /// Old
        {
            case eStart:
            case eNotPressed:
            {
                break;
            }
            case eRightPressed:
            {
                if (_newState != TriggerState.eRightPressed)
                {
                    sEvents.triggerEvent(new MapClickReleaseEvent(_rightStick,"Spit", mPlayer)); //spit release
                }
                break;
            }
            case eLeftPressed:
            {
                if (_newState != TriggerState.eLeftPressed)
                {
                    
                }
                break;
            }
            
        }
        switch (_newState) /// New
        {
            case eNotPressed:
            {
                break;
            }
            case eRightPressed:
            {
                sEvents.triggerEvent(new MapClickEvent(_rightStick,"Spit", mPlayer)); //spit
                break;
            }
            case eLeftPressed:
            {
                sEvents.triggerEvent(new KeyDownEvent(' ', mPlayer)); //lay
                break;
            }
            
        }
        mTriggerState = _newState;
    }
}
