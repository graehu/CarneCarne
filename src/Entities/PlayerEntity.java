/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Events.AreaEvents.CheckPointZone;
import Graphics.Skins.iSkin;
import Graphics.sGraphicsManager;
import HUD.Reticle;
import Level.sLevel.TileType;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.Joint;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author alasair
 */
public class PlayerEntity extends AIEntity
{
    String mBodyType = "bdy";
    private CheckPointZone mCheckPoint;
    private Joint mDeathJoint;
    private Vec2 mDirection;
    public Reticle mReticle;
    private Rectangle mViewPort;
    private int mScore;
    public PlayerEntity(iSkin _skin, CheckPointZone _spawnPoint)
    {
        super(_skin);
        mCheckPoint = _spawnPoint;
        mReticle = new Reticle(this);
        mScore = 0;
    }
    public void setClip(Rectangle _viewPort)
    {
        mViewPort = _viewPort;
    }
    public void placeCheckPoint(CheckPointZone _checkPoint)
    {
        if (_checkPoint.getCheckpointNumber() > mCheckPoint.getCheckpointNumber())
        {
            mCheckPoint = _checkPoint;
        }
    }
    public int getScore()
    {
        return mScore;
    }
    @Override
    public void kill()
    {
        if (mDeathJoint == null)
        {
            Fixture fixture = mBody.getFixtureList();
            while (fixture != null)
            {
                fixture.setSensor(true);
                fixture = fixture.getNext();
            }
            mDeathJoint = sWorld.createMouseJoint(mCheckPoint.getPosition(), mBody);
            mAIEntityState.kill();
        }
    }
    boolean compareFloat(float a, float b, float epsilon)
    {
        return (a < b + epsilon && a > b - epsilon);
    }
    @Override
    protected void subUpdate()
    {
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
    }

    public void render()
    {
        mSkin.setRotation(mBodyType, mBody.getAngle()*(180/(float)Math.PI));
        super.render();
        if (sGraphicsManager.getClip() == mViewPort)
        {
            mReticle.render();
            mCheckPoint.renderRaceState();
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
}
