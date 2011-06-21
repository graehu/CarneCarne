/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Particles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author a203945
 */
public class sParticles {
    
    static HashMap<String, ParticleSystem> mLoadedSystems = new HashMap<String, ParticleSystem>();
    static ArrayList<ParticleSystem> mInstancedSystems = new ArrayList<ParticleSystem>();
    
    private sParticles()
    {
        
    }
    
    public static void createSystem(String _ref, float _x, float _y) throws SlickException
    {
        ParticleSystem system = null;
        if(mLoadedSystems.containsKey(_ref))
        {
            system = mLoadedSystems.get(_ref);
        }
        else
        {
            try {system = ParticleIO.loadConfiguredSystem(_ref);} 
            catch (IOException ex) {Logger.getLogger(sParticles.class.getName()).log(Level.SEVERE, null, ex);}
            
            mLoadedSystems.put(_ref, system);
        }
        system = system.duplicate(); //clone
        
        system.setPosition(_x, _y);
        mInstancedSystems.add(system);
    }
    
    /*
     * _delta: time step in milliseconds
     */
    public static void update(int _delta)
    {
        for(ParticleSystem system : mInstancedSystems)
        {
            system.update(_delta);
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
        for(ParticleSystem system : mInstancedSystems)
        {
            //calc positions in screenspace
            float posX = system.getPositionX() + _x;
            float posY = system.getPositionY() + _y;
            //cull
            if( posX >= -_border && posX < _width + _border &&
                posY >= -_border && posY < _height + _border)
            {
                system.render(posX, posY);
            }
        }
    }
}
