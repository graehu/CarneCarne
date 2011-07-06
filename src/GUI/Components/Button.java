/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author a203945
 */
public class Button extends GraphicalComponent{
    public Button(GUIContext _context, iComponent _parent, Vector2f _position, Vector2f _dimensions) {
        super(_context, _parent, _position, _dimensions);
        _context.getInput().addPrimaryListener(this);
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
    
    Image mDefaultImage = null;
    Color mDefaultColor = Color.white;
    Image mMouseOverImage = null;
    Color mMouseOverColor = Color.green;
    Image mSelectedImage = null;
    Color mSelectedColor = Color.red;
    ButtonState mState = ButtonState.eDefault;
    Runnable mCallback = null;

    public void setCallback(Runnable _callback)
    {
        mCallback = _callback;
    }
    @Override
    public boolean updateSelf(int _delta) 
    {        
        return super.updateSelf(_delta);
    }
    private void changeState(ButtonState _newState)
    {
        //do not process if already in _newState
        if(mState == _newState)
            return;
        
        switch(_newState)
        {
            case eSelected:
            {
                if(mCallback != null)
                    mCallback.run();
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
    }
    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if(mState != ButtonState.eSelected)
        {
            Shape myshape = getShape();
            if(myshape.contains(newx, newy))
            {
                changeState(ButtonState.eHoverOver);  
            }
            else 
                changeState(ButtonState.eDefault);
        }
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        switch(button)
        {
            case Input.MOUSE_LEFT_BUTTON: 
            {
                Shape myshape = getShape();
                if(myshape.contains(x, y))
                {
                    changeState(ButtonState.eSelected);
                    consumeEvent();
                }
                else
                    changeState(ButtonState.eDefault);
            }
        }
    }
    
    
   
    
    
    
}
