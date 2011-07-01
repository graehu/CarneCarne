/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.PlayerEntity;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
abstract public class CheckPointZone extends AreaEvent
{
    int mCheckPointNumber;
    int mFrames;
    CheckPointZone mNextCheckPoint;
    public CheckPointZone(int _x, int _y, int _x2, int _y2, int _checkPointNumber, CheckPointZone _nextCheckPoint)
    {
        super(_x, _y, _x2, _y2);
        mCheckPointNumber = _checkPointNumber;
        mNextCheckPoint = _nextCheckPoint;
        mFrames = 0;
    }
    @Override
    public void enter(PlayerEntity _entity)
    {
        _entity.placeCheckPoint(this);
    }

    @Override
    public void leave(PlayerEntity _entity)
    {
    }
    
    public Vec2 getPosition()
    {
        return new Vec2(x,y);
    }
    
    public int getCheckpointNumber()
    {
        return mCheckPointNumber;
    }
    
    @Override
    public final void update()
    {
        mFrames++;
    }
    
    public abstract void renderRaceState();
    
}
