/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author a203945
 */
public class ScrollableComponent extends GraphicalComponent{
    public ScrollableComponent(GUIContext _context, Vector2f _position, Vector2f _dimensions) {
        super(_context, _position, _dimensions);
    }
    
    private boolean isAutoScrollPaused = true;
    private Vector2f mAutoScroll = new Vector2f(0,0);
    private Vector2f mScroll = new Vector2f(0,0);
    private Vector2f mAbsMaxScroll = new Vector2f(0,0);

    @Override
    protected boolean updateSelf(int _delta) {
        if(false == isAutoScrollPaused)
        {
            mScroll.add(mAutoScroll);
            forceToBounds(mScroll);
            if(mScroll.x == 0 || mScroll.x == 1)
                autoScrollStop(true);
            if(mScroll.y == 0 || mScroll.y == 1)
                autoScrollStop(false);
        }
        return super.updateSelf(_delta);
    }

    @Override
    protected void renderSelf(GUIContext guic, Graphics grphcs, Vector2f _globalPos) throws SlickException 
    {        
        grphcs.setWorldClip((int)_globalPos.x, (int)_globalPos.y, getWidth(), getHeight());
            Vector2f offset = new Vector2f(-mScroll.x * mAbsMaxScroll.x, mScroll.y * mAbsMaxScroll.y);
            super.renderSelf(guic, grphcs, _globalPos.add(offset));
        grphcs.clearWorldClip();
    }

    @Override
    public void setImage(String _ref) {
        super.setImage(_ref);
        //we set the Absolute max scroll distance 
        //to the difference between the scroll box and component
        //and ensure it's not negative
        float absScrollX = Math.max(0,mImage.getWidth() - getWidth());
        float absScrollY = Math.max(0,mImage.getHeight() - getHeight());
        mAbsMaxScroll = new Vector2f(absScrollX, absScrollY);
    }
    
    //relative (0-1)
    public final void scrollHorizontalBy(float _dx)
    {
        mScroll.x += _dx;
        mScroll.x = forceToBounds(mScroll.x);
    }
    //relative (0-1)
    public final void scrollVerticalBy(float _dy)
    {
        mScroll.y += _dy;
        mScroll.y = forceToBounds(mScroll.y);
    }
    //relative (0-1)
    public final void setScrollHorizontal(float _x)
    {
        mScroll.x = _x;
        mScroll.x = forceToBounds(mScroll.x);
    }
    //relative (0-1)
    public final void setScrollVertical(float _y)
    {
        mScroll.y = _y;
        mScroll.y = forceToBounds(mScroll.y);
    }
    //scroll at given step per second (0-1)
    public final void autoScrollHorizonal(float _perSecond)
    {
        mAutoScroll.x = _perSecond;
        mAutoScroll.x = forceToBounds(mAutoScroll.x);
    }
    //scroll at given step per second (0-1)
    public final void autoScrollVertical(float _perSecond)
    {
        mAutoScroll.y = _perSecond;
        mAutoScroll.y = forceToBounds(mAutoScroll.y);
    }
    public final void autoScrollStop(boolean _stopHorizontal)
    {
        if(_stopHorizontal)
            mAutoScroll.x = 0;
        else
            mAutoScroll.y = 0;
    }
    public final void autoScrollPause(boolean _isPaused)
    {
        isAutoScrollPaused = _isPaused;
    }
    private void forceToBounds(Vector2f _value)
    {
        _value.x = forceToBounds(_value.x);
        _value.y = forceToBounds(_value.y);
    }
    private float forceToBounds(float _value)
    {
        float temp = Math.max(0,_value);
        return Math.min(1,temp);
    }
    
}
