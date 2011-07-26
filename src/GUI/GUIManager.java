
package GUI;

import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import GUI.Components.GraphicalComponent;
import GUI.Components.iComponent;
import Graphics.sGraphicsManager;
import Utils.Throw;
import java.util.HashMap;
import java.util.Iterator;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Aaron
 */

//FIXME: GUI should be reworked to use relative screen coords

//this class provides multiplue instances o9f itself to allow for persistance GUIs between states
public class GUIManager implements iEventListener
{    
    static private int keyCount = 0;
    static private HashMap<Integer, GUIManager> instances = new HashMap<Integer, GUIManager>();
    static GUIManager currentInstance = null;
    
    //Gets current instance
    public static GUIManager get()
    {
        return currentInstance;
    }
    public static void set(int _inst)
    {
        currentInstance = instances.get(_inst);
    }
    
    public static int create(GameContainer _context)
    {
        instances.put(keyCount, new GUIManager(_context));
        return keyCount++;
    }
    
    public enum ComponentType
    {
        eGraphical,
        eComponentTypeCount
    }
    
    HashMap<Integer, iComponent> mManagedRoots = new HashMap<Integer, iComponent>();
    GameContainer mContainer = null;
    int IDCounter = 0;
    float scale = 1.0f;
    
    private GUIManager(GameContainer _context)
    {
        scale = sGraphicsManager.getTrueScreenDimensions().x / 1680; //FIXME: assumes native resolution at 1680x1050
        if(_context != null)
            mContainer = _context;
        else
            Throw.err("Null GUIContext given!");
        //subscribe to events
        sEvents.subscribeToEvent("WindowResizeEvent", this);
    }
    
    public Integer addRootComponent(iComponent _component)
    {
        _component.setLocalScale(scale);
        if(_component.isRoot())
        {
            mManagedRoots.put(IDCounter, _component);
            return IDCounter++;
        }
        else
        {
            Throw.err("Component is not a root");
            return null;
        }
    }
    public Integer createRootComponent(ComponentType _type, Vector2f _position, Vector2f _dimensions)
    {
        iComponent component = null;
        switch(_type)
        {
            case eGraphical:
            {
                component = new GraphicalComponent(mContainer, _position, _dimensions);
                component.setLocalScale(scale);
                mManagedRoots.put(IDCounter, component);
                return IDCounter++;
                //break;
            }
            default:
            {
                return null;
                //break;
            }       
        }
    }
    
    public void update(int _delta)
    {
        Iterator<iComponent> itr = mManagedRoots.values().iterator();
        while(itr.hasNext())
        {
            iComponent c = itr.next();
            
            c.update(_delta);
        }
    }
    
    public void render(boolean _debug) throws SlickException
    {
        Iterator<iComponent> itr = mManagedRoots.values().iterator();
        while(itr.hasNext())
        {
            iComponent c = itr.next();
            c.setLocalScale(scale);
            c.render(mContainer, mContainer.getGraphics(), _debug);
        }
    }
    
    public boolean trigger(iEvent _event) 
    {
        if(_event.getType().equals("WindowResizeEvent"))
        {
            //calc scale
            Vec2 screen = sGraphicsManager.getTrueScreenDimensions();
            //FIXME: assumes native resolution at 1680x1050
            scale = screen.x / 1680;
        }
        return true; //do not unsubscribe
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