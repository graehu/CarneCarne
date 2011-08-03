
package GUI;

import Events.WindowResizeEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import GUI.Components.GraphicalComponent;
import GUI.Components.Text;
import GUI.Components.iComponent;
import Graphics.sGraphicsManager;
import Utils.Throw;
import java.util.ArrayList;
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
    static GameContainer mDefaultContext = null;
    
    public static void setDefaultContext(GameContainer _gc)
    {
        mDefaultContext = _gc;
    }
    //Gets current instance
    public static GUIManager get()
    {
        return currentInstance;
    }
    //sets current instance
    public static void set(int _inst)
    {
        currentInstance = instances.get(_inst);
    }
    public static GUIManager use(int _inst)
    {
        return instances.get(_inst);
    }
    //create GUIManager instance using default context
    public static Integer create()
    {
        if(mDefaultContext != null)
            return create(mDefaultContext);
        else
            Throw.err("No Default context set!");
        return null;
    }
    public static Integer create(GameContainer _context)
    {
        instances.put(keyCount, new GUIManager(_context));
        return keyCount++;
    }
    public static void destroy(int _inst)
    {
        instances.get(_inst).destroyAllComponents();
        instances.remove(_inst);
    }
    public enum ComponentType
    {
        eGraphical,
        eText,
        eComponentTypeCount
    }
    
    HashMap<Integer, iComponent> mManagedRoots = new HashMap<Integer, iComponent>();
    GameContainer mContainer = null;
    int IDCounter = 0;
    Vector2f scale = new Vector2f(1.0f,1.0f);
    Vector2f mDimensions = null;
    
    private GUIManager(GameContainer _context)
    {
        scale.x = sGraphicsManager.getTrueScreenDimensions().x / 1680; //FIXME: assumes native resolution at 1680x1050
        scale.y = sGraphicsManager.getTrueScreenDimensions().y / 1050;
        if(_context != null)
            mContainer = _context;
        else
            Throw.err("Null GUIContext given!");
        //subscribe to events
        sEvents.subscribeToEvent("WindowResizeEvent", this);
    }
    
    public void addRootComponent(iComponent _component)
    {
        _component.setLocalScale(scale);
        if(_component.isRoot())
        {
            mManagedRoots.put(IDCounter, _component);
            IDCounter++;
        }
        else
        {
            Throw.err("Component is not a root");
        }
    }
    public iComponent createRootComponent(ComponentType _type, Vector2f _position, Vector2f _dimensions)
    {
        iComponent component = null;
        switch(_type)
        {
            case eGraphical:
            {
                component = new GraphicalComponent(mContainer, _position, _dimensions);
                component.setLocalScale(scale);
                mManagedRoots.put(IDCounter, component);
                IDCounter++;
                return component;
                //break;
            }
            case eText:
            {
                component = new Text(mContainer, null, "", null, false);
                component.setLocalScale(scale);
                mManagedRoots.put(IDCounter, component);
                IDCounter++;
                return component;
            }
            default:
            {
                return null;
                //break;
            }       
        }
    }
    public void removeRootComponent(Integer _ref)
    {
        mManagedRoots.remove(_ref);
    }
    public void removeRootComponentList(ArrayList<Integer> _list)
    {
        for(Integer i : _list)
            mManagedRoots.remove(i);
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
    
    public void setDimensions(Vector2f _dimensions)
    {
        mDimensions = _dimensions;
        scale.x = mDimensions.x / 1680; //FIXME: assumes native resolution at 1680x1050
        scale.y = mDimensions.y / 1050;
    }
    public void render(boolean _debug)
    {
        try 
        {
            Iterator<iComponent> itr = mManagedRoots.values().iterator();
            while(itr.hasNext())
            {
                iComponent c = itr.next();
                c.setLocalScale(scale);
                c.render(mContainer, mContainer.getGraphics(), _debug);
            }
        } catch (SlickException ex) {Throw.err(ex.toString());}
    }
    
    public boolean trigger(iEvent _event) 
    {
        if(_event.getType().equals("WindowResizeEvent"))
        {
            WindowResizeEvent e = (WindowResizeEvent)_event;
            if(mDimensions == null)
            {
                scale.x = e.getDimensions().x / 1680; //FIXME: assumes native resolution at 1680x1050
                scale.y = e.getDimensions().y / 1050;
            }
            else
            {
                scale.x = mDimensions.x / 1680; //FIXME: assumes native resolution at 1680x1050
                scale.y = mDimensions.y / 1050;
            }
        }
        return true; //do not unsubscribe
    }
    
    public void setAcceptingInput(boolean _isAccepting)
    {
        Iterator<iComponent> itr = mManagedRoots.values().iterator();
        while(itr.hasNext())
        {
            iComponent c = itr.next();
            c.setAcceptingInput(_isAccepting);
        }
    }
    
    private void destroyAllComponents()
    {
        Iterator<iComponent> itr = mManagedRoots.values().iterator();
        while(itr.hasNext())
        {
            iComponent c = itr.next();
            c.destroy();
        }
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