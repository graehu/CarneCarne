/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author A203946
 */
public class MouseMoveEvent extends iEvent {
    
    Vec2 position;
    int mPlayer;
    public MouseMoveEvent(Vec2 _position, int _player)
    {
        position = _position;
        mPlayer = _player;
    }
    
    public String getType()
    {
        return "MouseMoveEvent";
    }
    
    public String getName()
    {
        return getType() + mPlayer;
    }
    public Vec2 getScreenPosition()
    {
        return position;
    }
    
    public Vec2 getPhysicsPosition()
    {
        return sWorld.translateToPhysics(position);
    }
}
