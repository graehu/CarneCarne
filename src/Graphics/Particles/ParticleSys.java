/*
 * This class is a wrapper for the slick particle system
 * And provides functionality for moving the system while maintaining still particles
 * As well as a lifeTime for the system itself (with emitters wrapping up before destruction)
 */

package Graphics.Particles;

import java.util.Arrays;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author a203945
 */
public class ParticleSys
{
    protected float mLife;
    protected boolean mIsDead = false;
    protected ParticleSystem mSystem;

    /*
     * _lifetime: -ve denotes persistence, +ve is lifetime in seconds
     */
    protected ParticleSys(ParticleSystem _system, float _lifeTime)
    {
        mSystem = _system;
        mLife = _lifeTime;
    }

    //accessor for mIsDead
    public boolean isDead()
    {
        return mIsDead;
    }
    
    public void kill()
    {
        mLife = 0.0f;
    }
    
    //move emitters only
    public void moveTo(float _x, float _y)
    {
        float deltaX = mSystem.getPositionX() - _x;
        float deltaY = mSystem.getPositionY() - _y;
        for(int i = 0; i< mSystem.getEmitterCount(); i++)
        {
            mSystem.moveAll(mSystem.getEmitter(i), deltaX, deltaY);
        }
        mSystem.setPosition(_x, _y);
    }
    
    //move emitters only
    public void moveBy(float _dx, float _dy)
    {
        float newX = mSystem.getPositionX() + _dx;
        float newY = mSystem.getPositionY() + _dy;
        for(int i = 0; i< mSystem.getEmitterCount(); i++)
        {
            mSystem.moveAll(mSystem.getEmitter(i), -_dx, -_dy);
        }
        mSystem.setPosition(newX, newY);
    }
    
    public void setWind(ConfigurableEmitter.Value _value)
    {
        for(int i = 0; i < mSystem.getEmitterCount(); i++)
        {
            ((ConfigurableEmitter)mSystem.getEmitter(i)).windFactor = _value;
        }
    }
    
    public void setGravity(ConfigurableEmitter.Value _value)
    {
        for(int i = 0; i < mSystem.getEmitterCount(); i++)
        {
            ((ConfigurableEmitter)mSystem.getEmitter(i)).gravityFactor = _value;
        }
    }
    
    public void setAngularOffset(ConfigurableEmitter.Value _value)
    {
        for(int i = 0; i < mSystem.getEmitterCount(); i++)
        {
            ((ConfigurableEmitter)mSystem.getEmitter(i)).angularOffset = _value;
        }
    }

    /*
     * Assumes time step in milliseconds
     * Returns false if life < 0
     */
    protected boolean update(int _delta)
    {
        mSystem.update(_delta);
        if(mLife < 0.0f)
        {//while persistant
            return true;
        }
        else if(mLife > 0.0f)
        {//while still alive
            mLife -= Math.min(mLife,((float)_delta)/1000.0f);
            return true;
        }
        else //if(mLife == 0.0f)
        {//end of life
            for(int i = 0; i < mSystem.getEmitterCount(); i++)
            {
                //stop emitter producing
                mSystem.getEmitter(i).wrapUp();

                //when all particles expire declare system dead
                if(mSystem.getEmitter(i).completed())
                {
                    mIsDead = true;
                }
            }
        }
        //if dead return false to destroy
        if(mIsDead)
        {
            return false;
        }
        else
        {
            return true;
        }
        
    }

    protected void render(float _x, float _y)
    {
        mSystem.render(_x, _y);
    }
}
