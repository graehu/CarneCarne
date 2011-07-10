/*
 * This class is a wrapper for the slick particle system
 * And provides functionality for moving the system while maintaining still particles
 * As well as a lifeTime for the system itself (with emitters wrapping up before destruction)
 */

package Graphics.Particles;

import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author a203945
 */
public class ParticleSys
{
    protected String mRef = null;
    protected float mLife = 0.0f;
    protected boolean mIsDead = false;
    protected ParticleSystem mSystem = null;
    private int mCompletedEmittors = 0;

    /*
     * _lifetime: -ve denotes persistence, +ve is lifetime in seconds
     */
    protected ParticleSys(ParticleSystem _system, float _lifeTime, String _ref)
    {
        mRef = _ref;
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
    
    protected void recycle()
    {
        sParticleManager.recycle(mSystem, mRef);
        mSystem = null;
    }
    
    //move emitters only
    public void moveEmittersTo(float _x, float _y)
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
    public void moveEmittersBy(float _dx, float _dy)
    {
        float newX = mSystem.getPositionX() + _dx;
        float newY = mSystem.getPositionY() + _dy;
        for(int i = 0; i< mSystem.getEmitterCount(); i++)
        {
            mSystem.moveAll(mSystem.getEmitter(i), -_dx, -_dy);
        }
        mSystem.setPosition(newX, newY);
    }
    
    public void setWind(float _windFactor)
    {
        for(int i = 0; i < mSystem.getEmitterCount(); i++)
        {
            ConfigurableEmitter emitter = ((ConfigurableEmitter)mSystem.getEmitter(i));
            ((ConfigurableEmitter.SimpleValue)emitter.windFactor).setValue(_windFactor);
        }
    }
    
    public void setGravity(float _gravityFactor)
    {
        for(int i = 0; i < mSystem.getEmitterCount(); i++)
        {
            ConfigurableEmitter emitter = ((ConfigurableEmitter)mSystem.getEmitter(i));
            ((ConfigurableEmitter.SimpleValue)emitter.gravityFactor).setValue(_gravityFactor);
        }
    }
    
    public void setAngularOffset(float _degrees)
    {
        for(int i = 0; i < mSystem.getEmitterCount(); i++)
        {
            ConfigurableEmitter emitter = ((ConfigurableEmitter)mSystem.getEmitter(i));
            ((ConfigurableEmitter.SimpleValue)emitter.angularOffset).setValue(_degrees);
        }
    }

    /*
     * Assumes time step in milliseconds
     * Returns false if life < 0
     */
    protected boolean update(int _delta)
    {
        if(mLife < 0.0f)
        {//while persistant
            //do nothing
        }
        else if(mLife > 0.0f)
        {//while still alive
            mLife -= Math.min(mLife,((float)_delta)/1000.0f);
            //do nothing
        }
        else //if(mLife == 0.0f)
        {//end of life
            mIsDead = false;
            for(int i = 0; i < mSystem.getEmitterCount(); i++)
            {
                //stop emitter producing
                mSystem.getEmitter(i).wrapUp();
                //when all particles expire declare system dead
                if(mSystem.getEmitter(i).completed())
                {
                    mCompletedEmittors++;
                    if(mSystem.getEmitterCount() == mCompletedEmittors)
                    {
                        mSystem.getEmitter(i).resetState();
                        mIsDead = true;
                    }
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
            mSystem.update(_delta);
            return true;
        }
        
        
    }

    protected void render(float _x, float _y)
    {
        mSystem.render(_x, _y);
    }
}
