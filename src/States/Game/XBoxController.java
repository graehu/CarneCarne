/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.AnalogueStickEvent;
import Events.KeyDownEvent;
import Events.MapClickReleaseEvent;
import Events.RightStickEvent;
import Events.ShoulderButtonEvent;
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
 * 6 - Select
 * 8 - L3
 * 9 - R3
 * Thinking about sitting and mapping the rest of these? Get a job!
 */
public class XBoxController
{
    static private float stickEpsilon = 0.1f; /// TWEAK
    static private float shoulderButtonEpsilon = 0.3f;
    private boolean xAxisHit = false;
    private boolean mLeftShoulderButton = false, mRightShoulderButton = false;
    //private boolean yAxisHit = false;
    private enum TriggerState
    {
        eStart, // This is the value for the controller sending out -1 continously because its gay in the butt for gay men
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
    
    public void update(Input _input)
    {
        //get player direction
        Vec2 rightStick = new Vec2(_input.getAxisValue(mPlayer, 3),_input.getAxisValue(mPlayer, 2));
        
        //handle ABXY buttons
        if(_input.isButtonPressed(0, mPlayer)) //jump
            sEvents.triggerEvent(new KeyDownEvent('w', mPlayer));
        
        //handle shoulder buttons
        if(_input.isButtonPressed(5, mPlayer)) //tongue 
        {
            sEvents.triggerEvent(new ShoulderButtonEvent(rightStick,true, mPlayer));
            mRightShoulderButton = true;
        }
        else if(mRightShoulderButton)//on release
        {
            mRightShoulderButton = false;
            sEvents.triggerEvent(new MapClickReleaseEvent(rightStick,true, mPlayer));
        }
        if(_input.isButtonPressed(4, mPlayer)) //spit
        {
            sEvents.triggerEvent(new ShoulderButtonEvent(rightStick,false, mPlayer));
            mLeftShoulderButton = true;
        }
        else if(mLeftShoulderButton)//on release
        {
            mLeftShoulderButton = false;
            sEvents.triggerEvent(new MapClickReleaseEvent(rightStick,false, mPlayer)); 
        }
        
        //handle shoulder triggers
        float xAxis = _input.getAxisValue(mPlayer, 1);
        if (xAxisHit)
        {
            if (xAxis > stickEpsilon || xAxis < -stickEpsilon)
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
            if (shoulderButtons > shoulderButtonEpsilon) //right trigger
            {
                changeState(TriggerState.eRightPressed, rightStick);
            }
            else if (shoulderButtons < -shoulderButtonEpsilon) //left trigger
            {
                changeState(TriggerState.eLeftPressed, rightStick);
            }
            else
            {
                changeState(TriggerState.eNotPressed, rightStick);
            }
        }
        
        if (rightStick.length() > 0.2f)
        {
            sEvents.triggerEvent(new RightStickEvent(rightStick, mPlayer));
        }        
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
                   // sEvents.triggerEvent(new MapClickReleaseEvent(_rightStick,true, mPlayer)); //tongue
                }
                break;
            }
            case eLeftPressed:
            {
                if (_newState != TriggerState.eLeftPressed)
                {
                    sEvents.triggerEvent(new KeyDownEvent(' ', mPlayer)); //lay
                   // sEvents.triggerEvent(new MapClickReleaseEvent(_rightStick,false, mPlayer)); //spit
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
                //sEvents.triggerEvent(new ShoulderButtonEvent(_rightStick,true, mPlayer));
                break;
            }
            case eLeftPressed:
            {
                //sEvents.triggerEvent(new ShoulderButtonEvent(_rightStick,false, mPlayer));
                break;
            }
            
        }
        mTriggerState = _newState;
    }
}
