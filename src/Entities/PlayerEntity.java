/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.PlayerInputController;
import Entities.AIEntityState.State;
import Events.AreaEvents.CheckPointZone;
import Events.MapClickReleaseEvent;
import GUI.GUIManager;
import Graphics.Skins.iSkin;
import Graphics.Sprites.iSprite;
import Graphics.Sprites.sSpriteFactory;
import Graphics.sGraphicsManager;
import HUD.Reticle;
import HUD.Revolver;
import Level.sLevel.TileType;
import Score.RaceScoreTracker;
import Score.ScoreTracker;
import States.Game.Tutorial.IntroSection;
import World.sWorld;
import java.util.HashMap;
import java.util.HashSet;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.Joint;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author alasair
 */
public class PlayerEntity extends AIEntity
{
    String mBodyType = "bdy";
    private CheckPointZone mOriginalSpawnPoint;
    private CheckPointZone mCheckPoint;
    private Vec2 mCheckPointPosition;
    public IntroSection mIntroSection;
    private Joint mDeathJoint;
    private Vec2 mDirection;
    public Reticle mReticle;
    protected Revolver mRevolver;
    private iSprite mArrowSprite;
    private Rectangle mViewPort;
    private int mRaceTimer;
    private int mDeaths;
    public ScoreTracker mScoreTracker;
    //private ParticleSys mParticleSys;
    public PlayerEntity(iSkin _skin, CheckPointZone _spawnPoint)
    {
        super(_skin);
        mScoreTracker = new RaceScoreTracker();
        mOriginalSpawnPoint = mCheckPoint = _spawnPoint;
        if (mCheckPoint != null)
            mCheckPointPosition = mCheckPoint.getPosition();
        mReticle = new Reticle(this);
        mRevolver = new Revolver("ui/revolver.png", new Vector2f(200,200));
        //GUIManager.get().addRootComponent(mRevolver);
        mDeaths = mRaceTimer = 0;
        HashMap params = new HashMap();
        try
        {
            params.put("img", new Image("assets/Arrow.png"));
        }
        catch (SlickException e)
        {
            assert(false);
        }
        mArrowSprite = sSpriteFactory.create("simple", params, false);
    }
    public void destroy() /// FIXME more memory leaks to cleanup in here
    {
        /// Purposefully not destroying the body
        mController.destroy();
    }
    public void setClip(Rectangle _viewPort)
    {
        mViewPort = _viewPort;
    }
    public CheckPointZone getCheckPoint()
    {
        return mCheckPoint;
    }
    @Override
    protected float calculateArea()
    {
        return super.calculateArea() * 4.0f;
    }
    private boolean mWasIReallyKilled = true;
    public void resetRace()
    {
        if (mDeathJoint != null)
        {
            sWorld.destroyMouseJoint(mDeathJoint);
            mDeathJoint = null;
            Fixture fixture = getBody().getFixtureList();
            while (fixture != null)
            {
                fixture.setSensor(false);
                fixture = fixture.getNext();
            }
            mAIEntityState.unkill();
        }
        mCheckPoint = mOriginalSpawnPoint;
        mCheckPointPosition = mCheckPoint.getPosition();
        mRaceTimer = 0;
        mWasIReallyKilled = false;
        kill();
        mWasIReallyKilled = true;
        mAIEntityState.restartingRace();
        mScoreTracker.raceEnded();
    }
    public void getToStartingZone()
    {
        mCheckPoint = mOriginalSpawnPoint;
    }
    public void placeCheckPoint(CheckPointZone _checkPoint)
    {
        System.out.println(mCheckPoint.getCheckpointNumber() + " " + _checkPoint.getCheckpointNumber());
        if (mDeathJoint == null)
        {
            if (mCheckPoint == mOriginalSpawnPoint)
            {
                if (_checkPoint.getCheckpointNumber() == mCheckPoint.getCheckpointNumber()+1)
                {
                    mCheckPoint = _checkPoint;
                    mCheckPointPosition = getBody().getPosition().clone();
                    //sEvents.triggerDelayedEvent(new ShowDirectionEvent(this));
                }
            }
            else if (_checkPoint.getCheckpointNumber() > mCheckPoint.getCheckpointNumber())
            {
                mCheckPoint = _checkPoint;
                mCheckPointPosition = getBody().getPosition().clone();
                //sEvents.triggerDelayedEvent(new ShowDirectionEvent(this));
            }
        }
        System.out.println(mCheckPoint.getCheckpointNumber() + " " + _checkPoint.getCheckpointNumber());
        System.out.println();
    }
    /*public int getScore()
    {
        return mScore;
    }*/
    @Override
    public void kill()
    {
        if (mDeathJoint == null)
        {
            if (mWasIReallyKilled)
            {
                mDeaths++;
                mScoreTracker.score(ScoreTracker.ScoreEvent.eDied);
            }
            Fixture fixture = getBody().getFixtureList();
            while (fixture != null)
            {
                fixture.setSensor(true);
                fixture = fixture.getNext();
            }
            mDeathJoint = sWorld.createMouseJoint(mCheckPointPosition, getBody());
            ((PlayerInputController)mController).kill();
        }
        mAIEntityState.kill();
    }
    boolean compareFloat(float a, float b, float epsilon)
    {
        return (a < b + epsilon && a > b - epsilon);
    }
    HashSet<CheckPointZone> checkpointSet = new HashSet<CheckPointZone>();
    @Override
    protected void subUpdate()
    {
        if (mCheckPoint != null && mCheckPoint.incrementRaceTimer()) /// FIXME - put this null check in as a quick fix for IntroMode
        {
            mRaceTimer++;
        }
        mReticle.update();
        
        HashSet<CheckPointZone> newCheckPoints = new HashSet<CheckPointZone>();
        ContactEdge contact = mBody.getContactList();
        while (contact != null)
        {
            if (contact.other.getFixtureList().getFilterData().categoryBits == (1 << sWorld.BodyCategories.eCheckPoint.ordinal()))
            {
                contact.contact.setEnabled(false);
                if (!checkpointSet.contains((CheckPointZone)contact.other.getUserData()))
                {
                    ((CheckPointZone)contact.other.getUserData()).enter(this);
                }
                newCheckPoints.add((CheckPointZone)contact.other.getUserData());
                //checkpointSet.add((CheckPointZone)contact.other.getUserData());
                //placeCheckPoint();
            }
            contact = contact.next;
        }
        for (CheckPointZone checkPoint: checkpointSet)
        {
            if (!newCheckPoints.contains(checkPoint))
            {
                checkPoint.leave(this);
            }
        }
        checkpointSet = newCheckPoints;
        if (mDeathJoint != null)
        {//when player is within half a tile of checkpoint destroy joint
            if (compareFloat(getBody().getPosition().x, mCheckPointPosition.x, 0.5f) && compareFloat(getBody().getPosition().y, mCheckPointPosition.y, 0.5f))
            {
                sWorld.destroyMouseJoint(mDeathJoint);
                mDeathJoint = null;
                Fixture fixture = getBody().getFixtureList();
                while (fixture != null)
                {
                    fixture.setSensor(false);
                    fixture = fixture.getNext();
                }
                mAIEntityState.unkill();
            }
        }
        else if (mAIEntityState.getState().equals(State.eSwimming))
        {
            buoyancy();
        }
    }

    @Override
    public void stun(Vec2 _direction)
    {
        super.stun(_direction);
    }
    @Override
    void stun()
    {
        super.stun();
        ((PlayerInputController)mController).stun();
    }

    @Override
    public void render()
    {
        //mParticleSys.moveEmittersTo(mBody.getPosition().x*64.0f, mBody.getPosition().y*64.0f);
        mSkin.setRotation(mBodyType, getBody().getAngle()*(180/(float)Math.PI));
        super.render();
    }
    
    public void renderHUD()
    {
        if (sGraphicsManager.getClip() == mViewPort)
        {
            if (mCheckPoint != null)
            {
                mCheckPoint.renderRaceState(mRaceTimer);
                if(mCheckPoint.getNext() != null)
                {
                    Vec2 direction = mCheckPoint.getNext().getPosition().sub(getBody().getPosition());
                    direction.normalize();
                    float rotation = (float)Math.atan2(direction.y, direction.x);
                    //rotation -= 180.0f;
                    mArrowSprite.setRotation(rotation*180.0f/(float)Math.PI);
                    mArrowSprite.render(mViewPort.getWidth()*0.5f, 0);
                }
                    
                sGraphicsManager.drawString("You have died " + mDeaths + " times", 0f, 0.1f);
            }
            else if (mIntroSection != null)
                mIntroSection.render();
            
            mReticle.render(); //always render ontop
            mScoreTracker.render();
        }
    }
    
    public void changeBodyType(TileType _type)
    {
        mSkin.deactivateSubSkin(mBodyType);
        switch(_type)
        {
            case eEdible:
                mSkin.activateSubSkin("edi", false, 0.0f);
                mBodyType = "edi";
                break;
            case eMelonFlesh:
                mSkin.activateSubSkin("wtr", false, 0.0f);
                mBodyType = "wtr";
                break;
            case eBouncy:
                mSkin.activateSubSkin("jly", false, 0.0f);
                mBodyType = "jly";
                break;
            case eGum:
                mSkin.activateSubSkin("gum", false, 0.0f);
                mBodyType = "gum";
                break;
            case eChilli:
                mSkin.activateSubSkin("spi", false, 0.0f);
                mBodyType = "spi";
                break;
            default:
            case eTileTypesMax:
                mSkin.activateSubSkin("bdy", false, 0.0f);
                mBodyType = "bdy";
                break;
        }
    }
    public void setDirection(Vec2 _dir)
    {
        mDirection = _dir;
    }

    public int getRaceTimer()
    {
        return mRaceTimer;
    }
    @Override
    protected void airControl(float _value)
    {
        float max = 8;
        if(_value >= 0)
        {
            if(getBody().getLinearVelocity().x < max)
            {
                getBody().applyLinearImpulse(new Vec2(0.3f*_value,0), getBody().getWorldCenter());
            }
        }
        else //if(_value < 0)
        {
            if(getBody().getLinearVelocity().x > -max)
            {
                getBody().applyLinearImpulse(new Vec2(0.3f*_value,0), getBody().getWorldCenter());
            }
        }
    }
}
