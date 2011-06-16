/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Events.MouseMoveEvent;
import Entities.AIEntity;
import Entities.sEntityFactory;
import Events.KeyDownEvent;
import Events.KeyUpEvent;
import Events.MapClickEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Physics.sPhysics;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class PlayerInputController implements iAIController, iEventListener{

    private AIEntity mEntity;
    private TongueStateMachine mTongueState;
    private String mouthAnimation;
    public PlayerInputController(AIEntity _entity)
    {
        mEntity = _entity;
        mouthAnimation = "e";
        sEvents.subscribeToEvent("KeyDownEvent"+'w', this);
        sEvents.subscribeToEvent("KeyDownEvent"+'a', this);
        sEvents.subscribeToEvent("KeyDownEvent"+'s', this);
        sEvents.subscribeToEvent("KeyDownEvent"+'d', this);
        sEvents.subscribeToEvent("MapClickEvent", this);
        sEvents.subscribeToEvent("MouseMoveEvent", this);
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
    public void spitBlock(Vec2 _position)
    {
        HashMap parameters = new HashMap();
        Vec2 direction = _position.sub( mEntity.mBody.getPosition());
        direction.normalize();;
        parameters.put("velocity", direction.mul(10.0f));
        parameters.put("position", mEntity.mBody.getPosition());
        sEntityFactory.create("SpatBlock", parameters); 
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
        else if (_event.getType().equals("MouseMoveEvent"))
        {
            MouseMoveEvent event = (MouseMoveEvent)_event;
            Vec2 direction = event.getPhysicsPosition().sub(mEntity.mBody.getPosition());
            direction.normalize();
            float angle = (float)Math.acos(Vec2.dot(new Vec2(0,-1), direction));
            angle *= 8;
            angle /= Math.PI;
            angle -= 0.5f;
            int sector = (int) (angle);
            mEntity.mSkin.stopAnim(mouthAnimation);
            if (direction.x > 0.0f)
            {
                switch (sector)
                {
                    case 0:
                    {
                        mouthAnimation = "nne";
                        break;
                    }
                    case 1:
                    {
                        mouthAnimation = "ne";
                        break;
                    }
                    case 2:
                    {
                        mouthAnimation = "nee";
                        break;
                    }
                    case 3:
                    {
                        mouthAnimation = "e";
                        break;
                    }
                    case 4:
                    {
                        mouthAnimation = "see";
                        break;
                    }
                    case 5:
                    {
                        mouthAnimation = "se";
                        break;
                    }
                    case 6:
                    {
                        mouthAnimation = "sse";
                        break;
                    }
                    case 7:
                    {
                        mouthAnimation = "s";
                        break;
                    }
                }
            }
            else
            {
                switch (sector)
                {
                    case 0:
                    {
                        mouthAnimation = "n";
                        break;
                    }
                    case 1:
                    {
                        mouthAnimation = "nnw";
                        break;
                    }
                    case 2:
                    {
                        mouthAnimation = "nw";
                        break;
                    }
                    case 3:
                    {
                        mouthAnimation = "nww";
                        break;
                    }
                    case 4:
                    {
                        mouthAnimation = "w";
                        break;
                    }
                    case 5:
                    {
                        mouthAnimation = "sww";
                        break;
                    }
                    case 6:
                    {
                        mouthAnimation = "sw";
                        break;
                    }
                    case 7:
                    {
                        mouthAnimation = "ssw";
                        break;
                    }
                }
                
            }
            mEntity.mSkin.startAnim(mouthAnimation, false, 0.0f);
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
                mTongueState.rightClick(event.getPosition());
            }
        }
    }
}
