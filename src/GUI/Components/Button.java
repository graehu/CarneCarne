/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author Aaron
 */
public class Button extends GraphicalComponent{
    public Button(GUIContext _context, Vector2f _position, Vector2f _dimensions) {
        super(_context, _position, _dimensions);
        _context.getInput().addListener(this);
        setColor(mDefaultColor);
    }
    public Button(GUIContext _context) {
        super(_context);
    }
    
    enum ButtonState
    {
        eHoverOver,
        eSelected,
        eDefault   
    }
    
    Text mText = null;
    Image mDefaultImage = null;
    Color mDefaultColor = new Color(0,0,0);
    Image mMouseOverImage = null;
    Color mMouseOverColor = new Color(187,139,44);
    Image mSelectedImage = null;
    Color mSelectedColor = new Color(210,187,21);
    ButtonState mState = ButtonState.eDefault;
    Runnable mCallback = null;    
    
    public void setCallback(Runnable _callback)
    {
        mCallback = _callback;
    }

    protected void changeState(ButtonState _newState)
    {
        //do not process if already in _newState
        if(mState == _newState)
            return;
        
        switch(_newState)
        {
            case eSelected:
            {
                //set relative image and color
                if(mSelectedImage != null)
                    mImage = mSelectedImage;
                else
                    setColor(mSelectedColor);
                break;
            }
            case eHoverOver:
            {
                //set relative image and color
                if(mMouseOverImage != null)
                    mImage = mMouseOverImage;
                else
                    setColor(mMouseOverColor);
                break;
            }
            case eDefault:
            {
                //set relative image and color
                if(mDefaultImage != null)
                    mImage = mDefaultImage;
                else
                    setColor(mDefaultColor);
                break;
            }
        } 
        mState = _newState;
        mText.setColor(getColor());
    }
    
    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if(mState != ButtonState.eSelected)
        {
            Shape myshape = getShape();
            if(myshape.contains(newx, newy))
            {
                if(mState != ButtonState.eSelected)
                    changeState(ButtonState.eHoverOver);  
            }
            else 
                changeState(ButtonState.eDefault);
        }
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        Shape myshape = getShape();
        if(myshape.contains(newx, newy))
        {
            //do nothing
        }
        else 
            changeState(ButtonState.eDefault);
    }
    

    @Override
    public void mousePressed(int button, int x, int y) {
        switch(button)
        {
            case Input.MOUSE_LEFT_BUTTON: 
            {
                Shape myshape = getShape();
                if(myshape.contains(x, y))
                {
                    changeState(ButtonState.eSelected);
                    //consumeEvent();
                }
                else
                    changeState(ButtonState.eDefault);
            }
        }
    }
    
    @Override
    public void mouseReleased(int button, int x, int y) {
        switch(button)
        {
            case Input.MOUSE_LEFT_BUTTON: 
            {
                Shape myshape = getShape();
                if(myshape.contains(x, y))
                {
                    if(mCallback != null && mState.equals(ButtonState.eSelected))
                        mCallback.run();
                    changeState(ButtonState.eHoverOver);
                    //consumeEvent();
                }
                else
                    changeState(ButtonState.eDefault);
            }
        }
    }

    public void addText(GUIContext _context, Font _font, String _str)
    {
         addText(_context, _font, _str, false);
    }
    public void addText(GUIContext _context, Font _font, String _str, boolean _sizeToText)
    {
        if(mText != null)
        {
            removeChild(mText);
            mText.destroy();
            mText = null;
        }
        Vector2f pos = new Vector2f(    (getWidth() - _font.getWidth(_str)) * 0.5f,
                                        (getHeight() - _font.getHeight(_str)) * 0.5f);
        mText = new Text(_context, _font, _str, pos);
        addChild(mText);
        mText.setColor(getColor());
        
        if(_sizeToText)
            setDimensionsToText();
    }
    
    public void setDimensionsToText()
    {
        setDimensions(mText.getDimensions());
        mText.setLocalTranslation(new Vector2f(0,0));
    }
    
    
    
    
    
   
    
    
    
}
