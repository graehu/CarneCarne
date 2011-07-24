/*
 * This class is for managing ParticleSys instances
 * It abstractsthe creation process and handles all updating, rending and culling
 */
package Graphics.Particles;

import Graphics.sGraphicsManager;
import Level.Tile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author aaron
 */
public class sParticleManager {
    
    static HashMap<String, ParticleSystem> mLoadedSystems = new HashMap<String, ParticleSystem>();
    static ArrayList<ParticleSysBase> mInstancedSystems = new ArrayList<ParticleSysBase>();
    static HashMap<String, ArrayList<ParticleSystem>> mSystemPool = new HashMap<String, ArrayList<ParticleSystem>>();
    
    private sParticleManager()
    {
        
    }
    
    public static void warmUp()
    {
        //warm the pool here
    }
    /*
     * _ref:        classpath dir of system's .xml
     * _x           position in X (pixels)
     * _y           position in Y (pixels)
     * _lifeTime:   time until death (seconds)
     */
    public static ParticleSysBase createMovingSystem(String _ref, float _lifeTime, Tile _tile)
    {
        ParticleSysBase p = new MovingParticleSys(createSystemImplementation(_ref, _tile.getWorldPosition().mul(64).add(new Vec2(32,32)), _lifeTime, true, false), _tile);
        mInstancedSystems.add(p);
        return p;
    }
    public static ParticleSysBase createSystem(String _ref, Vec2 _position, float _lifeTime)
    {
        return createSystemImplementation(_ref, _position, _lifeTime, true, true);
    }
    private static ParticleSysBase createSystemImplementation(String _ref, Vec2 _position, float _lifeTime, boolean _dive, boolean _store)
    {     
        if(false == sGraphicsManager.getAllowParticles())
            return new NullParticleSys();
        
        //if pooled object of same type exists use that
        ParticleSystem system = grabPooledSystem(_ref);
        if(system != null)
        {          
            system.reset();
        }
        //else if system already loaded but not pooled use that
        else if(mLoadedSystems.containsKey(_ref))
        {   
            system = getLoadedSystem(_ref);
        }
        else
        {//create new
            try
            {
                system = ParticleIO.loadConfiguredSystem("particleSystems/" + _ref + ".xml");
            } 
            catch (Throwable ex)
            {
                if (_dive)
                {
                    //load first system
                    ParticleSysBase sys = createSystemImplementation(_ref + "1", _position, _lifeTime, false, _store);
                    if(sys == null) return null;
                    system = sys.getSystem();
                    
                    //load second
                    sys = createSystemImplementation(_ref + "2", _position, _lifeTime, false, _store);
                    if(sys == null) return null;
                    ParticleSystem system2 = sys.getSystem();
                    
                    //combine them
                    ParticleSys p = new DoubleParticleSys(system,_ref + "1", system2, _ref + "2", _lifeTime);
                    if (_store)
                        mInstancedSystems.add(p);
                    return p;
                }
                else
                {
                    System.err.println(ex.getMessage());
                    return null;
                }
            }
            putLoadedSystem(_ref, system);
        }
        system.setPosition(_position.x, _position.y);
        
        //Create wrapper for system and instance it
        ParticleSys p = new ParticleSys(system, _lifeTime, _ref);
        
        if (_dive && _store) //only instance systems when diving so we don't duplicate
        {
            mInstancedSystems.add(p);
        }
        
        return p;        
    }
    
    //returns null if not pooled
    private static ParticleSystem grabPooledSystem(String _ref)
    {
        if(false == mSystemPool.containsKey(_ref))
            return null;
        
        ArrayList<ParticleSystem> pool = mSystemPool.get(_ref);
        if(pool.isEmpty())
            return null;
        
        ParticleSystem sys = pool.get(0);
        pool.remove(0);
        return sys;
    }
    
    private static ParticleSystem getLoadedSystem(String _ref)
    {
        try 
        {
            return mLoadedSystems.get(_ref).duplicate();
        } 
        catch (SlickException ex) 
        {
            Logger.getLogger(sParticleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static void putLoadedSystem(String _ref, ParticleSystem _system)
    {
        try 
        {
            mLoadedSystems.put(_ref, _system.duplicate());
        } 
        catch (SlickException ex) 
        {
            Logger.getLogger(sParticleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
     * _delta: time step in milliseconds
     */
    public static void update(int _delta)
    {
        ParticleSysBase sys = null;
        for(Iterator<ParticleSysBase> i = mInstancedSystems.iterator(); i.hasNext();)
        {
            sys = i.next();
            if(false == sys.update(_delta))
            {
                sys.recycle();
                i.remove();
            }
            
        }
    }
    
    protected static void recycle(ParticleSystem _system, String _ref)
    {
        if(mSystemPool.containsKey(_ref))
        {
            mSystemPool.get(_ref).add(_system);
        }
        else
        {
            mSystemPool.put(_ref, new ArrayList<ParticleSystem>());
            mSystemPool.get(_ref).add(_system);
        }
    }
    
    /*
     * _x:      world translation in X (pixels)
     * _y:      world translation in Y (pixels)
     * _width:  width of screen (pixels)
     * _height: height of screen (pixels)
     * _border: culling border size to stop popping (pixels)
     */
    public static void render(int _x, int _y, int _width, int _height, int _border)
    {
        //assumes equal length arrays: mInstancedSystems & mInstancedSysPos
        for(ParticleSysBase particle : mInstancedSystems)
        {
            //calc positions in screenspace
            Vec2 position = particle.getPosition();
            particle.render(position.x + _x, position.y + _y);
        }
    }
}
