/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Events.MouseMoveEvent;
import Entities.AIEntity;
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
import Level.sLevel;
import Events.RightStickEvent;
import HUD.Reticle;
import Level.Tile;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class PlayerInputController extends iAIController implements iEventListener {
    //constants
    final static float root2 = (float) Math.sqrt(2);
    //protected to allow TongueStateMachine access
    private TongueStateMachine mTongueState;
    protected String mFaceDirAnim;
    private float mTongueAngle = 0;
    protected Vec2 mPlayerDir = new Vec2(1,0);
    private int mPlayer;
    private Reticle mReticle;
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
        mReticle = new Reticle(_entity);
    }
    
    public void update()
    {        
        mTongueState.tick(mEntity);
        mReticle.updateDirection(mPlayerDir);
    
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
    
    public Tile grabBlock(Vec2 _position)
    {
        return sWorld.eatTiles(mEntity.mBody.getPosition(),_position);
    }
    
    public boolean hammer(Vec2 _position)
    {
        return sWorld.smashTiles(mEntity.mBody.getPosition(),_position);
    }
    
    public void layBlock(Tile _tile)
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
                    sLevel.placeTile(playerTileX, playerTileY+1, _tile.getRootId());
                    break;
                case 1: //east facing
                    sLevel.placeTile(playerTileX+1, playerTileY, _tile.getRootId());                 
                    break;
                case 2: //south facing
                    sLevel.placeTile(playerTileX, playerTileY-1, _tile.getRootId());
                    break;
                case 3: //west facing
                    sLevel.placeTile(playerTileX-1, playerTileY, _tile.getRootId());
                    break;
            }
        }     
    }
    
    public void spitBlock(Vec2 _position, sLevel.TileType _tileType)
    {
        HashMap parameters = new HashMap();
        Vec2 direction = _position.sub( mEntity.mBody.getPosition());
        direction.normalize();
        //intialise velocity relative to carne's
        parameters.put("velocity", direction.mul(10.0f).add(mEntity.mBody.getLinearVelocityFromLocalPoint(new Vec2(0,0))));
        parameters.put("position", mEntity.mBody.getPosition());
        parameters.put("tileType",_tileType);
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
            if(mTongueState.mIsTongueActive == false)
            {
                look(mPlayerDir);
            }
        }
        else if (_event.getType().equals("MouseMoveEvent"))
        {
            MouseMoveEvent event = (MouseMoveEvent)_event;
            mPlayerDir = event.getPhysicsPosition().sub(mEntity.mBody.getPosition().add(new Vec2(0.5f,0.5f))); //offset by half the width and height
            mReticle.updateDirection(mPlayerDir);
            if(mTongueState.mIsTongueActive == false)
            {
                look(mPlayerDir);
            }    
        }
        else if (_event.getType().equals("MouseDragEvent"))
        {
            MouseDragEvent event = (MouseDragEvent)_event;
            mPlayerDir = event.getPhysicsPosition().sub(mEntity.mBody.getPosition().add(new Vec2(0.5f,0.5f))); //offset by half the width and height
            mReticle.updateDirection(mPlayerDir);
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
    private void look(Vec2 _direction)
    {
        Vec2 direction = _direction.clone();
        direction.normalize();
        //assumes 64x64 sprite
        mEntity.mSkin.setOffset("tng", new Vec2(32,32).add(direction.mul(0.4f*64)));
        mTongueAngle = (float)Math.acos(Vec2.dot(new Vec2(0,-1), direction));
        if(direction.x < 0)
            mTongueAngle = (float) ((2*Math.PI) - mTongueAngle);
        float halfSeg = (float) (Math.PI/16.0f);
        //if statement splits left from right for efficiency
        //could further split into quadrents
        //each segment is the sum of half segments either side of each compass direction
        mEntity.mSkin.stopAnim(mFaceDirAnim);
        mEntity.mSkin.stopAnim("h"+mFaceDirAnim); //hat animation
        mEntity.mSkin.stopAnim("m"+mFaceDirAnim); //mouth animation
        mEntity.mSkin.stopAnim("mh"+mFaceDirAnim);
        if(mTongueAngle < Math.PI)
        {
            if(mTongueAngle < halfSeg)
                mFaceDirAnim = "n";
            else if(mTongueAngle >= halfSeg && mTongueAngle < 2*halfSeg)
                mFaceDirAnim = "nbe";
            else if(mTongueAngle >= 2*halfSeg && mTongueAngle < 3*halfSeg)
                mFaceDirAnim = "nne";
            else if(mTongueAngle >= 3*halfSeg && mTongueAngle < 4*halfSeg)
                mFaceDirAnim = "nebn";
            else if(mTongueAngle >= 4*halfSeg && mTongueAngle < 5*halfSeg)
                mFaceDirAnim = "ne";
            else if(mTongueAngle >= 5*halfSeg && mTongueAngle < 6*halfSeg)
                mFaceDirAnim = "nebe";
            else if(mTongueAngle >= 6*halfSeg && mTongueAngle < 7*halfSeg)
                mFaceDirAnim = "ene";
            else if(mTongueAngle >= 7*halfSeg && mTongueAngle < 8*halfSeg)
                mFaceDirAnim = "ebn";
            else if(mTongueAngle >= 8*halfSeg && mTongueAngle < 9*halfSeg)
                mFaceDirAnim = "e";
            else if(mTongueAngle >= 9*halfSeg && mTongueAngle < 10*halfSeg)
                mFaceDirAnim = "ebs";
            else if(mTongueAngle >= 10*halfSeg && mTongueAngle < 11*halfSeg)
                mFaceDirAnim = "ese";
            else if(mTongueAngle >= 11*halfSeg && mTongueAngle < 12*halfSeg)
                mFaceDirAnim = "sebe";
            else if(mTongueAngle >= 12*halfSeg && mTongueAngle < 13*halfSeg)
                mFaceDirAnim = "se";
            else if(mTongueAngle >= 13*halfSeg && mTongueAngle < 14*halfSeg)
                mFaceDirAnim = "sebs";
            else if(mTongueAngle >= 14*halfSeg && mTongueAngle < 15*halfSeg)
                mFaceDirAnim = "sse";
            else if(mTongueAngle >= 15*halfSeg && mTongueAngle < 16*halfSeg)
                mFaceDirAnim = "sbe";
            else if(mTongueAngle >= 16*halfSeg && mTongueAngle < 17*halfSeg)
                mFaceDirAnim = "s";
        }
        else //angle < 0
        {
            if(mTongueAngle < 17*halfSeg)
                mFaceDirAnim = "s";
            else if(mTongueAngle >= 17*halfSeg && mTongueAngle < 18*halfSeg)
                mFaceDirAnim = "sbw";
            else if(mTongueAngle >= 18*halfSeg && mTongueAngle < 19*halfSeg)
                mFaceDirAnim = "ssw";
            else if(mTongueAngle >= 19*halfSeg && mTongueAngle < 20*halfSeg)
                mFaceDirAnim = "swbs";
            else if(mTongueAngle >= 20*halfSeg && mTongueAngle < 21*halfSeg)
                mFaceDirAnim = "sw";
            else if(mTongueAngle >= 21*halfSeg && mTongueAngle < 22*halfSeg)
                mFaceDirAnim = "swbw";
            else if(mTongueAngle >= 22*halfSeg && mTongueAngle < 23*halfSeg)
                mFaceDirAnim = "wsw";
            else if(mTongueAngle >= 23*halfSeg && mTongueAngle < 24*halfSeg)
                mFaceDirAnim = "wbs";
            else if(mTongueAngle >= 24*halfSeg && mTongueAngle < 25*halfSeg)
                mFaceDirAnim = "w";
            else if(mTongueAngle >= 25*halfSeg && mTongueAngle < 26*halfSeg)
                mFaceDirAnim = "wbn";
            else if(mTongueAngle >= 26*halfSeg && mTongueAngle < 27*halfSeg)
                mFaceDirAnim = "wnw";
            else if(mTongueAngle >= 27*halfSeg && mTongueAngle < 28*halfSeg)
                mFaceDirAnim = "nwbw";
            else if(mTongueAngle >= 28*halfSeg && mTongueAngle < 29*halfSeg)
                mFaceDirAnim = "nw";
            else if(mTongueAngle >= 29*halfSeg && mTongueAngle < 30*halfSeg)
                mFaceDirAnim = "nwbn";
            else if(mTongueAngle >= 30*halfSeg && mTongueAngle < 31*halfSeg)
                mFaceDirAnim = "nnw";
            else if(mTongueAngle >= 31*halfSeg && mTongueAngle < 32*halfSeg)
                mFaceDirAnim = "nbw";
        }
    }
}
