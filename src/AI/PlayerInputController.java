/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.AIEntity;
import Events.KeyDownEvent;
import Events.KeyUpEvent;
import Events.MapClickEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Physics.sPhysics;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class PlayerInputController implements iAIController, iEventListener{

    private AIEntity mEntity;
    private TongueStateMachine mTongueState;
    public PlayerInputController(AIEntity _entity)
    {
        mEntity = _entity;
        sEvents.subscribeToEvent("KeyDownEvent"+'w', this);
        sEvents.subscribeToEvent("KeyDownEvent"+'a', this);
        sEvents.subscribeToEvent("KeyDownEvent"+'s', this);
        sEvents.subscribeToEvent("KeyDownEvent"+'d', this);
        sEvents.subscribeToEvent("MapClickEvent", this);
        mTongueState = new TongueStateMachine(this);
    }
    
    public void update()
    {
        mTongueState.tick();
    }
    
    public boolean grabBlock(Vec2 _position)
    {
        return sPhysics.rayCastTiles(mEntity.mBody.getPosition(),_position);
    }

    public void trigger(iEvent _event)
    {
        if (_event.getType().equals("KeyDownEvent"))
        {
            KeyDownEvent event = (KeyDownEvent)_event;
            switch (event.getKey())
            {
                case 'w':
                {
                    mEntity.jump();
                    break;
                }
                case 'a':
                {
                    mEntity.walkLeft();
                    break;
                }
                case 's':
                {
                    mEntity.crouch();
                    break;
                }
                case 'd':
                {
                    mEntity.walkRight();
                    break;
                }
            }
        }
        else if (_event.getType().equals("KeyUpEvent"))
        {
            KeyUpEvent event = (KeyUpEvent)_event;
            switch (event.getKey())
            {
                case 'w':
                {
                    break;
                }
                case 'a':
                {
                    break;
                }
                case 's':
                {
                    break;
                }
                case 'd':
                {
                    break;
                }
            }
        }
        else
        {
            MapClickEvent event = (MapClickEvent)_event;
            if (event.leftbutton())
            {
                mTongueState.leftClick(event.getPosition());
            }
            else
            {
                mTongueState.rightClick();
            }
        }
    }
}
