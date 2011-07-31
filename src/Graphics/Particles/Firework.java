/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Particles;

import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class Firework extends MovingParticleSys
{
    Vec2 mPosition, mVelocity;
    String mColour;
    Firework(ParticleSysBase _particles, Vec2 _position, Vec2 _velocity, String _colour)
    {
        super(_particles);
        mPosition = _position;
        mVelocity = _velocity;
        mColour = _colour;
    }
    public boolean update(int _delta)
    {
        mPosition = mPosition.add(mVelocity);
        Vec2 translatedPosition = getPosition();
        mParticles.moveEmittersTo(translatedPosition.x, translatedPosition.y);
        boolean ret = mParticles.update(_delta);
        if (!ret)
        {
            sParticleManager.createSystem("Firework" + mColour, translatedPosition, 0.5f);            
        }
        return ret;
    }

    public Vec2 getPosition()
    {
        return sWorld.translateToPhysics(mPosition).mul(64.0f);
    }
    
    @Override
    public void recycle()
    {
        super.recycle();
    }    
}
