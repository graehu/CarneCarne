/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Entities.AIEntityState.State;
import Events.AreaEvents.CheckPointZone;
import Graphics.Skins.iSkin;
import Graphics.Sprites.iSprite;
import Graphics.Sprites.sSpriteFactory;
import Graphics.sGraphicsManager;
import HUD.Reticle;
import Level.sLevel.TileType;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.Joint;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author alasair
 */
public class PlayerEntity extends AIEntity
{
    String mBodyType = "bdy";
    private CheckPointZone mOriginalSpawnPoint;
    private CheckPointZone mCheckPoint;
    private Joint mDeathJoint;
    private Vec2 mDirection;
    public Reticle mReticle;
    private iSprite mArrowSprite;
    private Rectangle mViewPort;
    private int mRaceTimer;
    private int mDeaths;
    //private ParticleSys mParticleSys;
    public PlayerEntity(iSkin _skin, CheckPointZone _spawnPoint)
    {
        super(_skin);
        mOriginalSpawnPoint = mCheckPoint = _spawnPoint;
        mReticle = new Reticle(this);
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
    public void resetRace()
    {
        mCheckPoint = mOriginalSpawnPoint;
        mRaceTimer = 0;
        int deaths = mDeaths;
        kill();
        mAIEntityState.restartingRace();
        mDeaths = deaths;
    }
    public void getToStartingZone()
    {
        mCheckPoint = mOriginalSpawnPoint;
    }
    public void placeCheckPoint(CheckPointZone _checkPoint)
    {
        if (mDeathJoint == null)
        {
            if (mCheckPoint == mOriginalSpawnPoint)
            {
                if (_checkPoint.getCheckpointNumber() == mCheckPoint.getCheckpointNumber()+1)
                {
                    mCheckPoint = _checkPoint;
                }
            }
            else if (_checkPoint.getCheckpointNumber() > mCheckPoint.getCheckpointNumber())
            {
                mCheckPoint = _checkPoint;
            }
        }
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
            mDeaths++;
            Fixture fixture = mBody.getFixtureList();
            while (fixture != null)
            {
                fixture.setSensor(true);
                fixture = fixture.getNext();
            }
            mDeathJoint = sWorld.createMouseJoint(mCheckPoint.getPosition(), mBody);
        }
        mAIEntityState.kill();
    }
    boolean compareFloat(float a, float b, float epsilon)
    {
        return (a < b + epsilon && a > b - epsilon);
    }
    @Override
    protected void subUpdate()
    {
        if (mCheckPoint != null && mCheckPoint.incrementRaceTimer()) /// FIXME - put this null check in as a quick fix for IntroMode
        {
            mRaceTimer++;
        }
        mReticle.update();
        if (mDeathJoint != null)
        {//when player is within half a tile of checkpoint destroy joint
            if (compareFloat(mBody.getPosition().x, mCheckPoint.getPosition().x, 0.5f) && compareFloat(mBody.getPosition().y, mCheckPoint.getPosition().y, 0.5f))
            {
                sWorld.destroyMouseJoint(mDeathJoint);
                mDeathJoint = null;
                Fixture fixture = mBody.getFixtureList();
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
    public void render()
    {
        //mParticleSys.moveEmittersTo(mBody.getPosition().x*64.0f, mBody.getPosition().y*64.0f);
        mSkin.setRotation(mBodyType, mBody.getAngle()*(180/(float)Math.PI));
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
                    Vec2 direction = mCheckPoint.getNext().getPosition().sub(mBody.getPosition());
                    direction.normalize();
                    float rotation = (float)Math.atan2(direction.y, direction.x);
                    //rotation -= 180.0f;
                    mArrowSprite.setRotation(rotation*180.0f/(float)Math.PI);
                    mArrowSprite.render(mViewPort.getWidth()*0.5f, 0);
                }

                sGraphicsManager.drawString("You have died " + mDeaths + " times", 0f, 0.1f);
            }
            mReticle.render(); //always render ontop
        }
    }
    
    public void changeBodyType(TileType _type)
    {
        mSkin.stopAnim(mBodyType);
        switch(_type)
        {
            case eEdible:
                mSkin.startAnim("edi", false, 0.0f);
                mBodyType = "edi";
                break;
            case eMelonFlesh:
                mSkin.startAnim("wtr", false, 0.0f);
                mBodyType = "wtr";
                break;
            case eBouncy:
                mSkin.startAnim("jly", false, 0.0f);
                mBodyType = "jly";
                break;
            case eGum:
                mSkin.startAnim("gum", false, 0.0f);
                mBodyType = "gum";
                break;
            case eChilli:
                mSkin.startAnim("spi", false, 0.0f);
                mBodyType = "spi";
                break;
            default:
            case eTileTypesMax:
                mSkin.startAnim("bdy", false, 0.0f);
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
        /*if (((PlayerInputController)mController).isSwinging())
        {
            Vec2 tongueDir = ((PlayerInputController)mController).getTongueDir();
            tongueDir = new Vec2(-tongueDir.y,tongueDir.x).mul(_value);
            mBody.applyLinearImpulse(tongueDir, mBody.getWorldCenter());
        }
        else*/
        {
            mBody.applyLinearImpulse(new Vec2(0.1f*_value,0), mBody.getWorldCenter());
        }
    }

    
    
}
