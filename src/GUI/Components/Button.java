/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author a203945
 */
public class Button extends GraphicalComponent{
    public Button(GUIContext _context, iComponent _parent, Vector2f _position, Vector2f _dimensions) {
        super(_context, _parent, _position, _dimensions);
    }
    public Button(GUIContext _context) {
        super(_context);
    }
    
    Image mDefaultImage = null;
    Color mDefaultColor = Color.white;
    Image mMouseOverImage = null;
    Color mMouseOverColor = Color.green;
    Image mMouseClickImage = null;
    Color mMouseClickColor = Color.red;

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        
        if(mMouseOverImage == null)
            setColor(mMouseOverColor);
        else
            mImage = mMouseOverImage;
    }
    
    
    
    
    
}
