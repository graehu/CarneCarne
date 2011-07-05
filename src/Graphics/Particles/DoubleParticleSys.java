/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Particles;

import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleSystem;

/**
 *
 * @author alasdair
 */
public class DoubleParticleSys extends ParticleSys
{
    ParticleSystem mOther;
    public DoubleParticleSys(ParticleSystem _system1, ParticleSystem _system2, float _lifeTime)
    {
        super(_system1, _lifeTime);
        mOther = _system2;
    }
    private void switchSystems()
    {
        ParticleSystem other = mOther;
        mOther = mSystem;
        mSystem = other;
    }
    //move emitters only
    @Override
    public void moveTo(float _x, float _y)
    {
        super.moveTo(_x, _y);
        switchSystems();
        super.moveTo(_x, _y);
        switchSystems();
    }
    
    //move emitters only
    @Override
    public void moveBy(float _dx, float _dy)
    {
        super.moveBy(_dx, _dy);
        switchSystems();
        super.moveBy(_dx, _dy);
        switchSystems();
    }
    
    @Override
    public void setWind(ConfigurableEmitter.Value _value)
    {
        super.setWind(_value);
        switchSystems();
        super.setWind(_value);
        switchSystems();
    }
    
    @Override
    public void setGravity(ConfigurableEmitter.Value _value)
    {
        super.setGravity(_value);
        switchSystems();
        super.setGravity(_value);
        switchSystems();
    }
    
    @Override
    public void setAngularOffset(ConfigurableEmitter.Value _value)
    {
        super.setAngularOffset(_value);
        switchSystems();
        super.setAngularOffset(_value);
        switchSystems();
    }

    /*
     * Assumes time step in milliseconds
     * Returns false if life < 0
     */
    @Override
    protected boolean update(int _delta)
    {
        boolean ret = super.update(_delta);
        switchSystems();
        ret = super.update(_delta) || ret;
        switchSystems();
        return ret;
    }

    @Override
    protected void render(float _x, float _y)
    {
        super.render(_x, _y);
        switchSystems();
        super.render(_x, _y);
        switchSystems();
    }
}
