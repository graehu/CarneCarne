/*
 * This class is for managing ParticleSys instances
 * It abstractsthe creation process and handles all updating, rending and culling
 */
package Graphics.Particles;

import java.io.IOException;
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
    static ArrayList<ParticleSys> mInstancedSystems = new ArrayList<ParticleSys>();
    
    private sParticleManager()
    {
        
    }
    /*
     * _ref:        classpath dir of system's .xml
     * _x           position in X (pixels)
     * _y           position in Y (pixels)
     * _lifeTime:   time until death (seconds)
     */
    public static ParticleSys createSystem(String _ref, Vec2 _position, float _lifeTime)
    {
        ParticleSystem system = null;
        if(mLoadedSystems.containsKey(_ref))
        {//already loaded
            system = mLoadedSystems.get(_ref);
        }
        else
        {//create new
            try {system = ParticleIO.loadConfiguredSystem("particleSystems/" + _ref + ".xml");} 
            catch (IOException ex) {Logger.getLogger(sParticleManager.class.getName()).log(Level.SEVERE, null, ex);}
            mLoadedSystems.put(_ref, system);
        }
        //clone and initialise new system
        try
        {
            system = system.duplicate(); //clone
        }
        catch (SlickException e)
        {
            
        }
        system.setPosition(_position.x, _position.y);
        //Create wrapper for system and instance it
        ParticleSys p = new ParticleSys(system, _lifeTime);
        mInstancedSystems.add(p);
        
        return p;
    }
    
    /*
     * _delta: time step in milliseconds
     */
    public static void update(int _delta)
    {
        for(Iterator<ParticleSys> i = mInstancedSystems.iterator(); i.hasNext();)
        {
            if(false == i.next().update(_delta))
            {
                i.remove();
            }
            
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
        for(ParticleSys particle : mInstancedSystems)
        {
            //calc positions in screenspace
            float posX = particle.mSystem.getPositionX() + _x;
            float posY = particle.mSystem.getPositionY() + _y;
            //cull
            if( posX >= -_border && posX < _width + _border &&
                posY >= -_border && posY < _height + _border)
            {
                particle.render(posX, posY);
            }
        }
    }
}
