/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author Aaron
 */
public class Text extends iComponent{
    public Text(GUIContext _context, Font _font, String _str, Vector2f _position) {
        super(_context);
        if(_font == null)
            mFont = _context.getDefaultFont(); //must be intialised first
        else 
            mFont = _font;
        mTextString = _str;
        calcDimensions();
        setLocalTranslation(_position);
    }
    
    Font mFont = null;
    String mTextString = "";
    
    @Override
    protected boolean updateSelf(int _delta) {
        //do nothing
        return true;
    }

    @Override
    protected void renderSelf(GUIContext guic, Graphics grphcs, Vector2f _globalPos) throws SlickException {
        if(mFont == null)
            super.renderSelf(guic, grphcs, _globalPos);
        else
            mFont.drawString(_globalPos.x, _globalPos.y, mTextString, getColor());
            
    }
    
    public final void setFont(Font _font)
    {
        if(_font != null)
            mFont = _font;
    }
    
    public final String getTextString(){return mTextString;}
    public final void setTextString(String _str)
    {
        if(_str != null)
        {
            mTextString = _str;
            calcDimensions();
        }
    }
    
    /*
     * helper functions
     */
    private final void calcDimensions()
    {
        Vector2f dimensions = new Vector2f(mFont.getWidth(mTextString),mFont.getHeight(mTextString));
        setDimensions(dimensions);
    }
    
}
