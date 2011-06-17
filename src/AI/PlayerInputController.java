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
            Vec2 direction = event.getPhysicsPosition().sub(mEntity.mBody.getPosition().add(new Vec2(0.5f,0.5f)));
            direction.normalize();
            float angle = (float)Math.acos(Vec2.dot(new Vec2(0,-1), direction));
            
            float halfSeg = (float) (Math.PI/16.0f);
            //if statement splits left from right for efficiency
            //could further split into quadrents
            //each segment is the sum of half segments either side of each compass direction
            mEntity.mSkin.stopAnim(mouthAnimation);
            if(direction.x >= 0)
            {
                if(angle < halfSeg)
                    mouthAnimation = "n";
                else if(angle >= halfSeg && angle < 3*halfSeg)
                    mouthAnimation = "nne";
                else if(angle >= 3*halfSeg && angle < 5*halfSeg)
                    mouthAnimation = "ne";
                else if(angle >= 5*halfSeg && angle < 7*halfSeg)
                    mouthAnimation = "nee";
                else if(angle >= 7*halfSeg && angle < 9*halfSeg)
                    mouthAnimation = "e";
                else if(angle >= 9*halfSeg && angle < 11*halfSeg)
                    mouthAnimation = "see";
                else if(angle >= 11*halfSeg && angle < 13*halfSeg)
                    mouthAnimation = "se";
                else if(angle >= 13*halfSeg && angle < 15*halfSeg)
                    mouthAnimation = "sse";
                else if(angle >= 15*halfSeg && angle < 16*halfSeg)
                    mouthAnimation = "s";
            }
            else //angle < 0
            {
                if(angle < halfSeg)
                    mouthAnimation = "n";
                else if(angle >= halfSeg && angle < 3*halfSeg)
                    mouthAnimation = "nnw";
                else if(angle >= 3*halfSeg && angle < 5*halfSeg)
                    mouthAnimation = "nw";
                else if(angle >= 5*halfSeg && angle < 7*halfSeg)
                    mouthAnimation = "nww";
                else if(angle >= 7*halfSeg && angle < 9*halfSeg)
                    mouthAnimation = "w";
                else if(angle >= 9*halfSeg && angle < 11*halfSeg)
                    mouthAnimation = "sww";
                else if(angle >= 11*halfSeg && angle < 13*halfSeg)
                    mouthAnimation = "sw";
                else if(angle >= 13*halfSeg && angle < 15*halfSeg)
                    mouthAnimation = "ssw";
                else if(angle >= 15*halfSeg && angle < 16*halfSeg)
                    mouthAnimation = "s";
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
