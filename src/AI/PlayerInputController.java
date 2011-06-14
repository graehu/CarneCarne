/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.AIEntity;
import Events.KeyDownEvent;
import Events.KeyUpEvent;
import Events.iEvent;
import Events.iEventListener;

/**
 *
 * @author A203946
 */
public class PlayerInputController implements iAIController, iEventListener{

    private AIEntity mEntity;
    public PlayerInputController(AIEntity _entity)
    {
        mEntity = _entity;
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
        else
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
    }
}
