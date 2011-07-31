/*
 * Empty class for when particle are turned off
 */
package Graphics.Particles;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author Aaron
 */
public class NullParticleSys implements ParticleSysBase 
{

    public boolean isDead() {
        return true;
    }
    
    public boolean isPersistant() {
        return false;
    }

    public void kill() {
        
    }

    public void recycle() {
        
    }

    public void moveEmittersTo(float _x, float _y) {
        
    }

    public void moveEmittersBy(float _dx, float _dy) {
        
    }

    public void setWind(float _windFactor) {
        
    }

    public void setGravity(float _gravityFactor) {
        
    }

    public void setAngularOffset(float _degrees) {
        
    }

    public boolean update(int _delta) {
        return false;
    }

    public void render(float _x, float _y) {
        
    }

    public Vec2 getPosition() {
        return new Vec2();
    }

    public ParticleSystem getSystem() {
        return null;
    }
    public void setPosition(Vec2 mul) {
    }
    public void setScale(float _s){
    }
    
}
