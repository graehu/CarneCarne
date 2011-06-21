/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Events.iEvent;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class MapClickReleaseEvent extends iEvent {

    Vec2 mPosition; /// This is the world space position
    boolean mLeftButton;
    public MapClickReleaseEvent(Vec2 _position, boolean _leftButton) /// Pass in the screen space position
    {
        mPosition = _position;
        mLeftButton = _leftButton;
    }
    public String getName()
    {
        return "MapClickReleaseEvent";
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
