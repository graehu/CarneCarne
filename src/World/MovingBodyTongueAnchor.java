/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
public class MovingBodyTongueAnchor extends TongueAnchor
{
    Body mBody;
    Vec2 mLocalAnchor;
    public MovingBodyTongueAnchor(Body _body, Vec2 _localAnchor)
    {
        mBody = _body;
        mLocalAnchor = _localAnchor;
    }

    @Override
    public Vec2 getPosition()
    {
        return mBody.getWorldPoint(mLocalAnchor);
    }
    int timer = 0;
    @Override
    public boolean hasContact()
    {
        if (mBody.m_world == null)
            return false;
        return true;
    }
}
