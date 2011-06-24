/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.Joint;

/**
 *
 * @author alasair
 */
public class PlayerEntity extends AIEntity {

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
        {
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
    
}
