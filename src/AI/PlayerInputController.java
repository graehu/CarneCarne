/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Events.MouseMoveEvent;
import Entities.AIEntity;
import Entities.PlayerEntity;
import Entities.sEntityFactory;
import Events.AnalogueStickEvent;
import Events.KeyDownEvent;
import Events.KeyUpEvent;
import Events.MapClickEvent;
import Events.MapClickReleaseEvent;
import Events.MouseDragEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Events.RightStickEvent;
import Level.Tile;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class PlayerInputController extends iAIController implements iEventListener {
    enum Controls
    {
        eTongue,
        eSpit,
        eControlsMax
    }
    //constants
    final static float root2 = (float) Math.sqrt(2);
    
    //protected to allow TongueStateMachine access
    private TongueStateMachine mTongueState;
    protected String mFaceDirAnim;
    protected Vec2 mPlayerDir = new Vec2(1,0);
    private int mPlayer;
    
    public PlayerInputController(AIEntity _entity, int _player)
    {
        super(_entity);
        mPlayer = _player;
        mFaceDirAnim = "e";
        sEvents.subscribeToEvent("KeyDownEvent"+'w'+_player, this);
        sEvents.subscribeToEvent("KeyDownEvent"+'a'+_player, this);
        sEvents.subscribeToEvent("KeyDownEvent"+'s'+_player, this);
        sEvents.subscribeToEvent("KeyDownEvent"+'d'+_player, this);
        sEvents.subscribeToEvent("KeyDownEvent"+' '+_player, this);
        sEvents.subscribeToEvent("MapClickEvent"+_player, this);
        sEvents.subscribeToEvent("MapClickReleaseEvent"+_player, this);
        sEvents.subscribeToEvent("MouseMoveEvent"+_player, this);
        sEvents.subscribeToEvent("MouseDragEvent"+_player, this);
        sEvents.subscribeToEvent("AnalogueStickEvent"+_player, this);
        sEvents.subscribeToEvent("RightStickEvent"+_player, this);
        mTongueState = new TongueStateMachine(this);
    }
    
    public void update()
    {        
        mTongueState.tick(mEntity);
        ((PlayerEntity)mEntity).setDirection(mPlayerDir);
    
        if(mTongueState.mState != TongueStateMachine.State.eSwinging)
        {
            if(mEntity.mBody.getLinearVelocity().x > 0) //moving right
                mEntity.mBody.setLinearVelocity(new Vec2(Math.min(4,mEntity.mBody.getLinearVelocity().x),mEntity.mBody.getLinearVelocity().y));
            else //moving left
                mEntity.mBody.setLinearVelocity(new Vec2(Math.max(-4,mEntity.mBody.getLinearVelocity().x),mEntity.mBody.getLinearVelocity().y));
        }
        if(mTongueState.mIsTongueActive)
        {
            look(mTongueState.mTongueDir);
            mEntity.mSkin.stopAnim(mFaceDirAnim);
            mEntity.mSkin.stopAnim("h"+mFaceDirAnim); //hat animation
            mEntity.mSkin.startAnim("m"+mFaceDirAnim, false, 0.0f); //mouth animation
            mEntity.mSkin.startAnim("mh"+mFaceDirAnim, false, 0.0f); //mouthHat animation
        }
        else
        {
            mEntity.mSkin.startAnim(mFaceDirAnim, false, 0.0f);
            mEntity.mSkin.startAnim("h"+mFaceDirAnim, false, 0.0f); //hat animation
            mEntity.mSkin.stopAnim("m"+mFaceDirAnim); //mouth animation
            mEntity.mSkin.stopAnim("mh"+mFaceDirAnim); //mouthHat animation
        }
    }
    
    public Tile grabBlock(final Vec2 _position)
    {
        return sWorld.eatTiles(mEntity.mBody.getPosition(),_position);
    }
    
    public boolean hammer(final Vec2 _position)
    {
        return sWorld.smashTiles(mEntity.mBody.getPosition(),_position);
    }
    
    public void layBlock(final Tile _tile)
    {
        //determine tile to grow block
        mPlayerDir.normalize(); //ensure it's normalised
        int dir = -1; //N:0 E:1 S:2 W:3
        //determine direction by quadrants
        if(mPlayerDir.x >= 0)
        {
            if(mPlayerDir.y >= 1/root2)
                dir = 0;                        //north
            else if(mPlayerDir.y > -1/root2)
                dir = 1;                        //east
            else //if(mPlayerDir.y > -1)
                dir = 2;                        //south
        }
        else //if(mPlayerDir < 0)
        {
            if(mPlayerDir.y >= 1/root2)
                dir = 0;                        //north
            else if(mPlayerDir.y > -1/root2)
                dir = 3;                        //west
            else //if(mPlayerDir.y > -1)
                dir = 2;                        //south
        }
        if(dir != -1)
        {
            Vec2 playerPos = mEntity.mBody.getPosition().add(new Vec2(0.5f,0.5f)); //offset to center
            int playerTileX = (int)playerPos.x; //casting floors value
            int playerTileY = (int)playerPos.y;
            switch(dir)
            {
                case 0: //north facing
                    _tile.getTileGrid().placeTile(playerTileX, playerTileY+1, _tile.getRootId());
                    break;
                case 1: //east facing
                    _tile.getTileGrid().placeTile(playerTileX+1, playerTileY, _tile.getRootId());                 
                    break;
                case 2: //south facing
                    _tile.getTileGrid().placeTile(playerTileX, playerTileY-1, _tile.getRootId());
                    break;
                case 3: //west facing
                    _tile.getTileGrid().placeTile(playerTileX-1, playerTileY, _tile.getRootId());
                    break;
            }
        }     
    }
    
    public void spitBlock(final Tile _tile)
    {
        HashMap parameters = new HashMap();
        //intialise velocity relative to carne's
        parameters.put("velocity", mPlayerDir.mul(10.0f).add(mEntity.mBody.getLinearVelocityFromLocalPoint(new Vec2(0,0))));
        parameters.put("position", mEntity.mBody.getPosition());
        parameters.put("tileType",_tile.getTileType());
        parameters.put("rootId",_tile.getRootId());
        sEntityFactory.create("SpatBlock", parameters); 
    }
    public void breathFire()
    {
        HashMap parameters = new HashMap();
        //intialise velocity relative to carne's
        parameters.put("velocity", mPlayerDir.mul(10.0f).add(mEntity.mBody.getLinearVelocityFromLocalPoint(new Vec2(0,0))));
        parameters.put("position", mEntity.mBody.getPosition());
        sEntityFactory.create("FireParticle", parameters);
    }

    public void trigger(final iEvent _event)
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
                case ' ':
                {
                    mTongueState.layBlock();
                    break;
                }
            }
        }
        else if (_event.getType().equals("AnalogueStickEvent"))
        {
            AnalogueStickEvent event = (AnalogueStickEvent)_event;
            mEntity.walk(event.getValue());
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
        else if (_event.getType().equals("RightStickEvent"))
        {
            RightStickEvent event = (RightStickEvent)_event;
            mPlayerDir = event.getDirection();
            mPlayerDir.normalize();
            if(mTongueState.mIsTongueActive == false)
            {
                look(mPlayerDir);
            }
        }
        else if (_event.getType().equals("MouseMoveEvent"))
        {
            MouseMoveEvent event = (MouseMoveEvent)_event;
            mPlayerDir = event.getPhysicsPosition().sub(mEntity.mBody.getPosition().add(new Vec2(0.5f,0.5f))); //offset by half the width and height
            mPlayerDir.normalize();
            if(mTongueState.mIsTongueActive == false)
            {
                look(mPlayerDir);
            }    
        }
        else if (_event.getType().equals("MouseDragEvent"))
        {
            MouseDragEvent event = (MouseDragEvent)_event;
            mPlayerDir = event.getPhysicsPosition().sub(mEntity.mBody.getPosition().add(new Vec2(0.5f,0.5f))); //offset by half the width and height
            mPlayerDir.normalize();
            if(mTongueState.mIsTongueActive == false)
            {
                look(mPlayerDir);
            }    
        }
        else if (_event.getType().equals("MapClickReleaseEvent"+mPlayer))
        {
            MapClickReleaseEvent event = (MapClickReleaseEvent)_event;
            if (event.leftbutton())
            {
                mTongueState.leftRelease(event.getPosition());
            }
            else
            {
                mTongueState.rightRelease(event.getPosition());
            }            
        }
        else //assume MapClick
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
    private void look(final Vec2 _direction)
    {
        Vec2 direction = _direction.clone();
        direction.normalize();
        float angle = (float)Math.acos(Vec2.dot(new Vec2(0,-1), direction));
        if(direction.x < 0)
            angle = (float) ((2*Math.PI) - angle);
        float halfSeg = (float) (Math.PI/16.0f);
        //if statement splits left from right for efficiency
        //could further split into quadrents
        //each segment is the sum of half segments either side of each compass direction
        mEntity.mSkin.stopAnim(mFaceDirAnim);
        mEntity.mSkin.stopAnim("h"+mFaceDirAnim); //hat animation
        mEntity.mSkin.stopAnim("m"+mFaceDirAnim); //mouth animation
        mEntity.mSkin.stopAnim("mh"+mFaceDirAnim);
        if(angle < Math.PI)
        {
            if(angle < halfSeg)
                mFaceDirAnim = "n";
            else if(angle >= halfSeg && angle < 2*halfSeg)
                mFaceDirAnim = "nbe";
            else if(angle >= 2*halfSeg && angle < 3*halfSeg)
                mFaceDirAnim = "nne";
            else if(angle >= 3*halfSeg && angle < 4*halfSeg)
                mFaceDirAnim = "nebn";
            else if(angle >= 4*halfSeg && angle < 5*halfSeg)
                mFaceDirAnim = "ne";
            else if(angle >= 5*halfSeg && angle < 6*halfSeg)
                mFaceDirAnim = "nebe";
            else if(angle >= 6*halfSeg && angle < 7*halfSeg)
                mFaceDirAnim = "ene";
            else if(angle >= 7*halfSeg && angle < 8*halfSeg)
                mFaceDirAnim = "ebn";
            else if(angle >= 8*halfSeg && angle < 9*halfSeg)
                mFaceDirAnim = "e";
            else if(angle >= 9*halfSeg && angle < 10*halfSeg)
                mFaceDirAnim = "ebs";
            else if(angle >= 10*halfSeg && angle < 11*halfSeg)
                mFaceDirAnim = "ese";
            else if(angle >= 11*halfSeg && angle < 12*halfSeg)
                mFaceDirAnim = "sebe";
            else if(angle >= 12*halfSeg && angle < 13*halfSeg)
                mFaceDirAnim = "se";
            else if(angle >= 13*halfSeg && angle < 14*halfSeg)
                mFaceDirAnim = "sebs";
            else if(angle >= 14*halfSeg && angle < 15*halfSeg)
                mFaceDirAnim = "sse";
            else if(angle >= 15*halfSeg && angle < 16*halfSeg)
                mFaceDirAnim = "sbe";
            else if(angle >= 16*halfSeg && angle < 17*halfSeg)
                mFaceDirAnim = "s";
        }
        else //angle < 0
        {
            if(angle < 17*halfSeg)
                mFaceDirAnim = "s";
            else if(angle >= 17*halfSeg && angle < 18*halfSeg)
                mFaceDirAnim = "sbw";
            else if(angle >= 18*halfSeg && angle < 19*halfSeg)
                mFaceDirAnim = "ssw";
            else if(angle >= 19*halfSeg && angle < 20*halfSeg)
                mFaceDirAnim = "swbs";
            else if(angle >= 20*halfSeg && angle < 21*halfSeg)
                mFaceDirAnim = "sw";
            else if(angle >= 21*halfSeg && angle < 22*halfSeg)
                mFaceDirAnim = "swbw";
            else if(angle >= 22*halfSeg && angle < 23*halfSeg)
                mFaceDirAnim = "wsw";
            else if(angle >= 23*halfSeg && angle < 24*halfSeg)
                mFaceDirAnim = "wbs";
            else if(angle >= 24*halfSeg && angle < 25*halfSeg)
                mFaceDirAnim = "w";
            else if(angle >= 25*halfSeg && angle < 26*halfSeg)
                mFaceDirAnim = "wbn";
            else if(angle >= 26*halfSeg && angle < 27*halfSeg)
                mFaceDirAnim = "wnw";
            else if(angle >= 27*halfSeg && angle < 28*halfSeg)
                mFaceDirAnim = "nwbw";
            else if(angle >= 28*halfSeg && angle < 29*halfSeg)
                mFaceDirAnim = "nw";
            else if(angle >= 29*halfSeg && angle < 30*halfSeg)
                mFaceDirAnim = "nwbn";
            else if(angle >= 30*halfSeg && angle < 31*halfSeg)
                mFaceDirAnim = "nnw";
            else if(angle >= 31*halfSeg && angle < 32*halfSeg)
                mFaceDirAnim = "nbw";
        }
    }
}
