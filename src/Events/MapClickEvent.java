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
public class MapClickEvent implements iEvent {
    
    Vec2 mPosition; /// This is the world space position
    boolean mLeftButton;
    public MapClickEvent(Vec2 _position, boolean _leftButton) /// Pass in the screen space position
    {
        mPosition = _position;
        mLeftButton = _leftButton;
    }
    public String getName()
    {
        return "MapClickEvent";
    }
    public String getType()
    {
        return getName();
    }
    public Vec2 getPosition()
    {
        return mPosition;
    }
    public boolean leftbutton()
    {
        return mLeftButton;
    }
}
