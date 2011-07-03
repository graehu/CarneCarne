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
        Vec2 pos = mPlatform.mBody.getPosition();
        Vec2 dimes = mPlatform.getDimensions();
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
            mPlatform.mBody.setLinearVelocity(new Vec2(mPlatform.getMoveSpeed(), 0));
        }
        else
        {
            mPlatform.mBody.setLinearVelocity(new Vec2(-mPlatform.getMoveSpeed(), 0));
        }
    }
    
}
