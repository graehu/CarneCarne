/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.MovingPlatform;

/**
 *
 * @author alasdair
 */
public abstract class iPlatformController
{
    MovingPlatform mPlatform;
    public void setPlatform(MovingPlatform _platform)
    {
        mPlatform = _platform;
    }
    public abstract void update();
}
