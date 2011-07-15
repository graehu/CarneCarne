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
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Events.RightStickEvent;
import Graphics.Particles.sParticleManager;
import Level.Tile;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class PlayerInputController extends iAIController implements iEventListener {

    public boolean isSwinging()
    {
        return mTongueState.isSwinging();
    }
    public Vec2 getTongueDir()
    {
        return mTongueState.getTongueDir();
    }
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
    public Vec2 mPlayerDir = new Vec2(1,0);
    public int mPlayer;
    int actionTimer = 0, actionDelay = 10;
    
    public PlayerInputController(AIEntity _entity, int _player)
    {
        super(_entity);
        mPlayer = _player;
        mFaceDirAnim = "e";
        sEvents.subscribeToEvent("KeyDownEvent"+'w'+_player, this);
        sEvents.subscribeToEvent("KeyUpEvent"+'w'+_player, this);
        sEvents.subscribeToEvent("KeyDownEvent"+'a'+_player, this);
        sEvents.subscribeToEvent("KeyDownEvent"+'s'+_player, this);
        sEvents.subscribeToEvent("KeyDownEvent"+'d'+_player, this);
        sEvents.subscribeToEvent("KeyDownEvent"+' '+_player, this);
        sEvents.subscribeToEvent("MapClickEventL"+_player, this);
        sEvents.subscribeToEvent("MapClickEventR"+_player, this);
        sEvents.subscribeToEvent("MapClickReleaseEvent"+_player, this);
        sEvents.subscribeToEvent("MouseMoveEvent"+_player, this);
        sEvents.subscribeToEvent("MouseDragEvent"+_player, this);
        sEvents.subscribeToEvent("AnalogueStickEvent"+_player, this);
        sEvents.subscribeToEvent("RightStickEvent"+_player, this);
        mTongueState = new TongueStateMachine(this);
    }
    @Override
    public void destroy() /// FIXME more memory leaks to clean up in here
    {
        sEvents.unsubscribeToEvent("KeyDownEvent"+'w'+mPlayer, this);
        sEvents.unsubscribeToEvent("KeyDownEvent"+'a'+mPlayer, this);
        sEvents.unsubscribeToEvent("KeyDownEvent"+'s'+mPlayer, this);
        sEvents.unsubscribeToEvent("KeyDownEvent"+'d'+mPlayer, this);
        sEvents.unsubscribeToEvent("KeyDownEvent"+' '+mPlayer, this);
        sEvents.unsubscribeToEvent("MapClickEventL"+mPlayer, this);
        sEvents.unsubscribeToEvent("MapClickEventR"+mPlayer, this);
        sEvents.unsubscribeToEvent("MapClickReleaseEvent"+mPlayer, this);
        sEvents.unsubscribeToEvent("MouseMoveEvent"+mPlayer, this);
        sEvents.unsubscribeToEvent("MouseDragEvent"+mPlayer, this);
        sEvents.unsubscribeToEvent("AnalogueStickEvent"+mPlayer, this);
        sEvents.unsubscribeToEvent("RightStickEvent"+mPlayer, this);
    }
    
    public void update()
    {       
        actionTimer++;
        mTongueState.tick(mEntity);
        ((PlayerEntity)mEntity).setDirection(mPlayerDir);
    
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
        parameters.put("position", mEntity.mBody.getPosition().add(mPlayerDir));
        parameters.put("tileType",_tile.getTileType());
        parameters.put("rootId",_tile.getRootId());
        sEntityFactory.create("SpatBlock", parameters); 
    }
    public void breathFire()
    {
        HashMap parameters = new HashMap();
        //intialise velocity relative to carne's
        parameters.put("velocity", mPlayerDir.mul(20.0f));
        parameters.put("position", mEntity.mBody.getPosition().add(mPlayerDir));
        sEntityFactory.create("FireParticle", parameters);
        sParticleManager.createSystem("DragonBreath", mEntity.mBody.getPosition().add(new Vec2(0.5f,0.5f)).add(mPlayerDir.mul(0.5f)).mul(64.0f), 1f)
                .setAngularOffset(((float)Math.atan2(mPlayerDir.y, mPlayerDir.x) * 180.0f/(float)Math.PI)-270.0f);
    }

    public boolean trigger(final iEvent _event)
    {
        if (_event.getType().equals("RightStickEvent"))
        {
            RightStickEvent event = (RightStickEvent)_event;
            mPlayerDir = event.getDirection();
            mPlayerDir.normalize();
            ((PlayerEntity)mEntity).mReticle.updateDirection(mPlayerDir);
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
            ((PlayerEntity)mEntity).mReticle.setWorldPosition(event.getWorldPosition());
            if(mTongueState.mIsTongueActive == false)
            {
                look(mPlayerDir);
            }    
        }
        //-------------------------------------------------------
        else if(mEntity.isDead())
            return true;// if dead accept only look input
        //-------------------------------------------------------
        else if (_event.getType().equals("KeyDownEvent"))
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
                    mEntity.stopJumping();
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
        else if (_event.getType().equals("MapClickEventL"+mPlayer))
        {
            if(actionTimer < actionDelay)
                return true;
            else
                actionTimer = 0;
            MapClickEvent event = (MapClickEvent)_event;
            mTongueState.leftClick(event.getPosition());
        }
        else if (_event.getType().equals("MapClickEventR"+mPlayer))
        {
            if(actionTimer < actionDelay)
                return true;
            else
                actionTimer = 0;
            MapClickEvent event = (MapClickEvent)_event;
            mTongueState.rightClick(event.getPosition());
        }
        else throw new UnsupportedOperationException();
        return true;
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
            else if(angle >= halfSeg && angle < 1.5*halfSeg)
                mFaceDirAnim = "nnbe";
            else if(angle >= 1.5*halfSeg && angle < 2*halfSeg)
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
            else if(angle >= 15*halfSeg && angle < 15.5*halfSeg)
                mFaceDirAnim = "sbe";
            else if(angle >= 15.5*halfSeg && angle <16*halfSeg)
                mFaceDirAnim = "ssbe";
        }
        else //angle < 0
        {
            if(angle < 17*halfSeg)
                mFaceDirAnim = "s";
            else if(angle >= 17*halfSeg && angle <17.5*halfSeg)
                mFaceDirAnim = "ssbw";
            else if(angle >= 17.5*halfSeg && angle < 18*halfSeg)
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
            else if(angle >= 31*halfSeg && angle < 31.5*halfSeg)
                mFaceDirAnim = "nbw";
            else if(angle >= 31.5*halfSeg && angle <32*halfSeg)
                mFaceDirAnim = "nnbw";
        }
    }
}
