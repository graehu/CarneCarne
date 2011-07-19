/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author alasdair
 */
public class MovingBodyTongueAnchor extends TongueAnchor
{
    Fixture mFixture;
    Vec2 mLocalAnchor;
    public MovingBodyTongueAnchor(Fixture _fixture, Vec2 _localAnchor)
    {
        mFixture = _fixture;
        mLocalAnchor = _localAnchor;
    }

    @Override
    public Vec2 getPosition()
    {
        return mFixture.getBody().getWorldPoint(mLocalAnchor);
    }
    @Override
    public boolean hasContact()
    {
        if (mFixture.getBody() == null)
            return false;
        return true;
    }
}
