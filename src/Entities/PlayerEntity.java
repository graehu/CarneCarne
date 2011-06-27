/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import Level.sLevel.TileType;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.Joint;

/**
 *
 * @author alasair
 */
public class PlayerEntity extends AIEntity {

    String mBodyType = "bdy";
    private Vec2 mCheckPoint;
    private Joint mDeathJoint;
    public PlayerEntity(iSkin _skin, Vec2 _checkPointPosition)
    {
        super(_skin);
        mCheckPoint = _checkPointPosition.clone();
    }
    public void placeCheckPoint(Vec2 _position)
    {
        mCheckPoint = _position.clone();
    }
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
            mDeathJoint = sWorld.createMouseJoint(mCheckPoint, mBody);
        }
    }
    boolean compareFloat(float a, float b, float epsilon)
    {
        return (a < b + epsilon && a > b - epsilon);
    }
    protected void subUpdate()
    {
        if (mDeathJoint != null)
        {//when player is within half a tile of checkpoint destroy joint
            if (compareFloat(mBody.getPosition().x, mCheckPoint.x, 0.5f) && compareFloat(mBody.getPosition().y, mCheckPoint.y, 0.5f))
            {
                sWorld.destroyMouseJoint(mDeathJoint);
                mDeathJoint = null;
                Fixture fixture = mBody.getFixtureList();
                while (fixture != null)
                {
                    fixture.setSensor(false);
                    fixture = fixture.getNext();
                }
            }
        }
    }
    @Override
    public void render()
    { 
        mSkin.setRotation(mBodyType, mBody.getAngle()*(180/(float)Math.PI));
        super.render();
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
    
}
