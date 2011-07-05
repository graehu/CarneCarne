/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import java.util.Iterator;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author a203945
 */
public class SelectableComponent extends iComponent{
    
    public SelectableComponent(GUIContext _context, iComponent _parent, Vector2f _position, Vector2f _dimensions) {
        super(_context, _parent);
        setDimensions(_dimensions);
        setLocalTranslation(_position);
    }
    public SelectableComponent(GUIContext _context) {
        super(_context, null);
    }
    
    protected Image mImage = null;
    protected Color mColor = Color.pink;
    
    @Override
    public boolean update(int _delta) {
        return true;
    }    

    @Override
    public void render(GUIContext guic, Graphics grphcs) throws SlickException {
        super.render(guic, grphcs);
        
        grphcs.setColor(mColor);

        float rot = getGlobalRotation();
        Vector2f trans = getGlobalTranslation();
        float centerX = trans.x + (getWidth() * 0.5f);
        float centerY = trans.y + (getHeight() * 0.5f);
        
        grphcs.rotate(centerX, centerY, rot);
        
            grphcs.drawRect(trans.x, trans.y, getWidth(), getHeight());
            
        grphcs.rotate(centerX, centerY, -rot);
        
        Iterator<iComponent> itr = getChildIterator();
        while(itr.hasNext())
        {
            iComponent child = itr.next();
            child.render(guic, grphcs);
        }
    }    
}
