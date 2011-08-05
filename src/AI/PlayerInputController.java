/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Events.MouseMoveEvent;
import Entities.AIEntity;
import Entities.Entity.CauseOfDeath;
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
import Sound.SoundScape.Sound;
import Sound.sSound;
import World.sWorld;
import java.util.HashMap;
import java.util.Random;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class PlayerInputController extends iAIController implements iEventListener
{
    int mStunTimer;
    public boolean isSwinging()
    {
        return mTongueState.isSwinging();
    }
    public Vec2 getTongueDir()
    {
        return mTongueState.getTongueDir();
    }

    public void stun()
    {
        mStunTimer = 120;
        mTongueState.tongueRelease(mPlayerDir);
        mTongueState.hammerRelease(mPlayerDir);
        mTongueState.spitRelease(mPlayerDir);
    }
    
    public void hammerHit()
    {
        mTongueState.hammerHit();
    }

    public void kill()
    {
        mTongueState.kill();
    }
    //constants
    final static float root2 = (float) Math.sqrt(2);
    
    //protected to allow TongueStateMachine access
    private TongueStateMachine mTongueState;
    protected String mFaceDirAnim;
    public Vec2 mPlayerDir = new Vec2(1,0);
    public int mPlayer;
    int actionTimer = 0, actionDelay = 10;
    int idleTimer = 0, idleDelay = 0;
    Random rand = new Random();
    
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
        sEvents.subscribeToEvent("KeyDownEvent"+'r'+_player, this);
        sEvents.subscribeToEvent("MapClickEvent" + "Tongue"+mPlayer, this);
        sEvents.subscribeToEvent("MapClickEvent" + "Hammer"+mPlayer, this);
        sEvents.subscribeToEvent("MapClickEvent" + "Spit"+mPlayer, this);
        sEvents.subscribeToEvent("MapClickEvent" + "TongueHammer"+mPlayer, this);
        sEvents.subscribeToEvent("MapClickReleaseEvent" + "Tongue" +mPlayer, this);
        sEvents.subscribeToEvent("MapClickReleaseEvent" + "Hammer" +mPlayer, this);
        sEvents.subscribeToEvent("MapClickReleaseEvent" + "Spit" +mPlayer, this);
        sEvents.subscribeToEvent("MapClickReleaseEvent" + "TongueHammer" +mPlayer, this);
        sEvents.subscribeToEvent("MouseMoveEvent"+_player, this);
        sEvents.subscribeToEvent("MouseDragEvent"+_player, this);
        sEvents.subscribeToEvent("AnalogueStickEvent"+_player, this);
        sEvents.subscribeToEvent("RightStickEvent"+_player, this);
        mTongueState = new TongueStateMachine(this);
    }
    @Override
    public void destroy() /// FIXME more memory leaks to clean up in here
    {
        sEvents.blockListener(this);
        sEvents.unsubscribeToEvent("KeyDownEvent"+'w'+mPlayer, this);
        sEvents.unsubscribeToEvent("KeyUpEvent"+'w'+mPlayer, this);
        sEvents.unsubscribeToEvent("KeyDownEvent"+'a'+mPlayer, this);
        sEvents.unsubscribeToEvent("KeyDownEvent"+'s'+mPlayer, this);
        sEvents.unsubscribeToEvent("KeyDownEvent"+'d'+mPlayer, this);
        sEvents.unsubscribeToEvent("KeyDownEvent"+' '+mPlayer, this);
        sEvents.unsubscribeToEvent("KeyDownEvent"+'r'+mPlayer, this);
        sEvents.unsubscribeToEvent("MapClickEvent" + "Tongue"+mPlayer, this);
        sEvents.unsubscribeToEvent("MapClickEvent" + "Hammer"+mPlayer, this);
        sEvents.unsubscribeToEvent("MapClickEvent" + "Spit"+mPlayer, this);
        sEvents.unsubscribeToEvent("MapClickEvent" + "TongueHammer"+mPlayer, this);
        sEvents.unsubscribeToEvent("MapClickReleaseEvent" + "Tongue" +mPlayer, this);
        sEvents.unsubscribeToEvent("MapClickReleaseEvent" + "Hammer" +mPlayer, this);
        sEvents.unsubscribeToEvent("MapClickReleaseEvent" + "Spit" +mPlayer, this);
        sEvents.unsubscribeToEvent("MapClickReleaseEvent" + "TongueHammer" +mPlayer, this);
        sEvents.unsubscribeToEvent("MouseMoveEvent"+mPlayer, this);
        sEvents.unsubscribeToEvent("MouseDragEvent"+mPlayer, this);
        sEvents.unsubscribeToEvent("AnalogueStickEvent"+mPlayer, this);
        sEvents.unsubscribeToEvent("RightStickEvent"+mPlayer, this);
        super.destroy();
    }
    
    public void update()
    {       
        if(idleTimer == -1)
            mEntity.stopIdle();
        if (mStunTimer != 0)
            mStunTimer--;
        actionTimer++;
        mTongueState.tick(mEntity);
        ((PlayerEntity)mEntity).setDirection(mPlayerDir);
        idleTimer++;
        if(mEntity.isIdle())
        {
            if(idleTimer >= idleDelay)
            {
                if(mPlayerDir.x < 0) //looking left
                {
                    if(!mEntity.mSkin.isAnimating("idle_burp2"))
                    {
                        //ensure other animations are deactivated
                        mEntity.mSkin.deactivateSubSkin("face_"+mFaceDirAnim);
                        mEntity.mSkin.deactivateSubSkin("hat_"+mFaceDirAnim);
                        mEntity.mSkin.deactivateSubSkin("idle_burp2");
                        mEntity.mSkin.activateSubSkin("idle_burp2", false, 1.0f);
                    }
                }
                else
                {
                    if(!mEntity.mSkin.isAnimating("idle_burp2_right"))
                    {
                        //ensure other animations are deactivated
                        mEntity.mSkin.deactivateSubSkin("face_"+mFaceDirAnim);
                        mEntity.mSkin.deactivateSubSkin("hat_"+mFaceDirAnim);
                        mEntity.mSkin.deactivateSubSkin("idle_burp2_right");
                        mEntity.mSkin.activateSubSkin("idle_burp2_right", false, 1.0f);
                    }
                }
                idleDelay = rand.nextInt(360) + 180; //min 5 second
                idleTimer = 0;
            }
        }
        else //idle animation overrides all others
        {
            idleTimer = 0;
            mEntity.mSkin.deactivateSubSkin("idle_burp2");
            mEntity.mSkin.deactivateSubSkin("idle_burp2_right");
            if(mEntity.isDead())
            {
                mEntity.mSkin.deactivateSubSkin("hat_"+mFaceDirAnim); //only allow hat when not dead
                mEntity.mSkin.activateSubSkin("carne_fly", true, 1.0f);
            }
            else
            {
                mEntity.mSkin.activateSubSkin("hat_"+mFaceDirAnim, false, 0.0f); 
                mEntity.mSkin.deactivateSubSkin("carne_fly");
            }
            if(mTongueState.mIsTongueActive)
            {
                look(mTongueState.getTongueDir()); //updates look direction while swinging
                mEntity.mSkin.activateSubSkin("hat_"+mFaceDirAnim, false, 0.0f); //activate hat because look deactivates it
                mEntity.mSkin.activateSubSkin("faceOpen_"+mFaceDirAnim, false, 0.0f);
            }
            else
            {
                mEntity.mSkin.activateSubSkin("face_"+mFaceDirAnim, false, 0.0f);
            }
        }
    }
    
    public Tile grabBlock(final Vec2 _position)
    {
        return sWorld.eatTiles(mEntity.getBody().getPosition(),_position);
    }
    
    public boolean hammer(final Vec2 _position)
    {
        return sWorld.smashTiles(mEntity.getBody().getPosition(),_position, mPlayer);
    }
    
    public void layBlock(final Tile _tile)
    {
        /*
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
            Vec2 playerPos = mEntity.getBody().getPosition().add(new Vec2(0.5f,0.5f)); //offset to center
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
        }    */ 
    }
    
    public float getWeight()
    {
        return mTongueState.getWeight();
    }
    public void spitBlock(final Tile _tile)
    {
        sSound.playPositional(Sound.eSpitBlock, mEntity.getBody().getPosition(), _tile.getTileType());
        HashMap parameters = new HashMap();
        //intialise velocity relative to carne's
        parameters.put("velocity", mPlayerDir.mul(20.0f).add(mEntity.getBody().getLinearVelocityFromLocalPoint(new Vec2(0,0))));
        parameters.put("position", mEntity.getBody().getPosition().add(mPlayerDir));
        parameters.put("tileType",_tile.getTileType());
        parameters.put("rootId",_tile.getRootId());
        parameters.put("rotation", (float)Math.atan2(mPlayerDir.y, mPlayerDir.x));
        sEntityFactory.create("SpatBlock", parameters); 
    }
    public void breathFire()
    {
        sSound.playPositional(Sound.eLaunchFireball, mEntity.getBody().getPosition());
        HashMap parameters = new HashMap();
        //intialise velocity relative to carne's
        parameters.put("velocity", mPlayerDir.mul(20.0f));//.add(mEntity.getBody().getLinearVelocity()));
        parameters.put("position", mEntity.getBody().getPosition().add(mPlayerDir));
        parameters.put("owner", mEntity);
        sEntityFactory.create("FireParticle", parameters);
        sParticleManager.createSystem("DragonBreath", mEntity.getBody().getPosition().add(new Vec2(0.5f,0.5f)).add(mPlayerDir.mul(0.5f)).mul(64.0f), 1f)
                .setAngularOffset(((float)Math.atan2(mPlayerDir.y, mPlayerDir.x) * 180.0f/(float)Math.PI)-270.0f);
    }

    public boolean trigger(final iEvent _event)
    {
        if(mEntity.isIdle())
            ((PlayerEntity)mEntity).stopIdle();
        if (mStunTimer == 0)
        {
            mEntity.mSkin.deactivateSubSkin("pea_stun_large");
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
                ((PlayerEntity)mEntity).mReticle.setScreenPosition(event.getScreenPosition());
                mPlayerDir = ((PlayerEntity)mEntity).mReticle.getPlayerDirection();
                //mPlayerDir = event.getPhysicsPosition().sub(mEntity.mBody.getPosition().add(new Vec2(0.5f,0.5f))); //offset by half the width and height
                //mPlayerDir.normalize();
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
                    case 'r':
                    {
                        mEntity.kill(CauseOfDeath.eMundane, null);
                        break;
                    }
                }
            }
            else if (_event.getType().equals("AnalogueStickEvent"))
            {
                AnalogueStickEvent event = (AnalogueStickEvent)_event;
                mEntity.walk(event.getHValue());
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
            else if (_event.getType().equals("MapClickReleaseEvent"))
            {
                MapClickReleaseEvent event = (MapClickReleaseEvent)_event;
                if (event.getName().equals("MapClickReleaseEvent" + "Spit" + mPlayer))
                {
                    mTongueState.spitRelease(event.getPosition());
                }
                else if (event.getName().equals("MapClickReleaseEvent" + "Hammer" + mPlayer))
                {
                    mTongueState.hammerRelease(event.getPosition());
                }
                else if (event.getName().equals("MapClickReleaseEvent" + "Tongue" + mPlayer))
                {
                    mTongueState.tongueRelease(event.getPosition());
                }
                else if (event.getName().equals("MapClickReleaseEvent" + "TongueHammer" + mPlayer))
                {
                    mTongueState.tongueRelease(event.getPosition());
                    mTongueState.hammerRelease(event.getPosition());
                }
            }
            else if (_event.getType().equals("MapClickEvent"))
            {
                if(actionTimer < actionDelay)
                    return true;
                else
                    actionTimer = 0;
                MapClickEvent event = (MapClickEvent)_event;
                if (event.getName().equals("MapClickEvent" + "Spit" + mPlayer))
                {
                    mTongueState.spitClick(event.getPosition());
                }
                else if (event.getName().equals("MapClickEvent" + "Hammer" + mPlayer))
                {
                    mTongueState.hammerClick(event.getPosition());
                }
                else if (event.getName().equals("MapClickEvent" + "Tongue" + mPlayer))
                {
                    mTongueState.tongueClick(event.getPosition());
                }
                else if (event.getName().equals("MapClickEvent" + "TongueHammer" + mPlayer))
                {
                    mTongueState.hammerClick(event.getPosition());
                    mTongueState.tongueClick(event.getPosition());
                }
            }
            else throw new UnsupportedOperationException();
        }
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
        mEntity.mSkin.deactivateSubSkin("face_"+mFaceDirAnim);
        mEntity.mSkin.deactivateSubSkin("hat_"+mFaceDirAnim); //hat animation
        mEntity.mSkin.deactivateSubSkin("faceOpen_"+mFaceDirAnim); //mouth animation
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
