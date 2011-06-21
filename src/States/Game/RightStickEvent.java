/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.iEvent;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class RightStickEvent extends iEvent {

    Vec2 mDirection;
    public RightStickEvent(Vec2 _direction)
    {
        mDirection = _direction;
    }
    
    public Vec2 getDirection()
    {
        return mDirection;
    }

    @Override
    public String getName()
    {
        return "RightStickEvent";
    }

    @Override
    public String getType()
    {
        return getName();
    }
    
}
