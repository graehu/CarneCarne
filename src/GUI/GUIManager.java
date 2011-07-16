
package GUI;

import GUI.Components.GraphicalComponent;
import GUI.Components.iComponent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Aaron
 */
public class GUIManager {

    enum ComponentType
    {
        eGraphical,
        eComponentTypeCount
    }
    
    static HashMap<Integer, iComponent> mManagedRoots = new HashMap<Integer, iComponent>();
    static GameContainer mContainer = null;
    static int IDCounter = 0;
    protected GUIManager()
    {
        
    }
    
    public static void init(GameContainer _context) throws SlickException
    {
        if(_context != null)
            mContainer = _context;
        else
            throw new SlickException("Null GUIContext given!");
    }
    
    public static Integer createRootComponent(ComponentType _type, Vector2f _position, Vector2f _dimensions)
    {
        iComponent component = null;
        switch(_type)
        {
            case eGraphical:
            {
                component = new GraphicalComponent(mContainer, _position, _dimensions);
                mManagedRoots.put(IDCounter, component);
                IDCounter++;
                return IDCounter;
                //break;
            }
            default:
            {
                return null;
                //break;
            }       
        }
    }
    
    public static void update(int _delta)
    {
        Iterator<iComponent> itr = mManagedRoots.values().iterator();
        while(itr.hasNext())
        {
            iComponent c = itr.next();
            
            c.update(_delta);
        }
    }
    
    public static void render(boolean _debug) throws SlickException
    {
//        for(iComponent c :mManagedRoots)
//        {
//            c.render(mContainer, mContainer.getGraphics(), _debug);
//        }
    }
    
}

/*
 * the gui should
 * have a node based system for ordering components
 * COMPONENTS: buttons, input text boxes, sliding bars, 
 * background, fonts, text alignment
 * static and animated objects should be interchangable
 * particle effects
 * sound effects on interactions
 * 
 */