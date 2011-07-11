/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Level.sLevel;
import Level.sLevel.PathInfo;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class SimplePlatformController extends iPlatformController
{
    boolean mToggle = false;
    @Override
    public void update() 
    {
        Vec2 pos = mEntity.mBody.getPosition();
        Vec2 dimes = new Vec2(mEntity.getWidth(), mEntity.getHeight());
        if(mToggle == false)
        {
            if(sLevel.getPathInfo((int)(pos.x-((dimes.x/2)-0.5f)), (int)(pos.y-((dimes.y/2)-0.5f))) == PathInfo.eNotPassable)
            {
                mToggle = true;
            }
        }
        else
        {
            if(sLevel.getPathInfo((int)(pos.x+((dimes.x/2)+0.5f)), (int)(pos.y+((dimes.y/2)+0.5f))) == PathInfo.eNotPassable)
            {
                mToggle = false;
            } 
        }
        
        if(mToggle)
        {
            mEntity.mBody.setLinearVelocity(new Vec2(1, 0)); /// changed from mPlatform.getMoveSpeed()
        }
        else
        {
            mEntity.mBody.setLinearVelocity(new Vec2(-1, 0));
        }
    }
    
}
