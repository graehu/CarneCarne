/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import org.jbox2d.common.Vec2;

/**
 *
 * @author alasair
 */
public class MapClickEvent extends iEvent {
    
    Vec2 mPosition; /// This is the world space position
    boolean mLeftButton;
    protected int mPlayer;
    public MapClickEvent(Vec2 _position, boolean _leftButton, int _player) /// Pass in the screen space position
    {
        mPosition = _position;
        mLeftButton = _leftButton;
        mPlayer = _player;
    }
    public String getName()
    {
        return "MapClickEvent" + mPlayer;
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
