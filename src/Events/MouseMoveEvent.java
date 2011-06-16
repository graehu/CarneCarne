/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Physics.sPhysics;
import org.jbox2d.common.Vec2;

/**
 *
 * @author A203946
 */
public class MouseMoveEvent implements iEvent {
    
    Vec2 position;
    public MouseMoveEvent(Vec2 _position)
    {
        position = _position;
    }
    
    public String getType()
    {
        return getName();
    }
    
    public String getName()
    {
        return "MouseMoveEvent";
    }
    public Vec2 getScreenPosition()
    {
        return position;
    }
    
    public Vec2 getPhysicsPosition()
    {
        return sPhysics.translateToPhysics(position);
    }
}
