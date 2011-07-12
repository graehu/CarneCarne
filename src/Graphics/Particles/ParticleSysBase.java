/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Particles;

/**
 *
 * @author alasdar
 */
public interface ParticleSysBase
{
    
    //accessor for mIsDead
    public boolean isDead();
    
    public void kill();
    
    void recycle();
    
    //move emitters only
    public void moveEmittersTo(float _x, float _y);
    
    //move emitters only
    public void moveEmittersBy(float _dx, float _dy);
    
    public void setWind(float _windFactor);
    
    public void setGravity(float _gravityFactor);
    
    public void setAngularOffset(float _degrees);

    /*
     * Assumes time step in milliseconds
     * Returns false if life < 0
     */
    boolean update(int _delta);

    void render(float _x, float _y);
}