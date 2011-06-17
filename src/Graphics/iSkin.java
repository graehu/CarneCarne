/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import org.jbox2d.common.Vec2;

/**
 *
 * @author aaron
 */
//class public to package only
public interface iSkin {
    
    abstract void render(float _x, float _y);
    abstract void setDimentions(float _w, float _h);
    
    //Animation methods
    abstract float getDuration();
    abstract void setRotation(float _radians);
    abstract void restart();
    abstract public void setIsLooping(boolean _isLooping);
    abstract public void setSpeed(float _speed);
    
    //character methods
    abstract void setRotation(String _animation,float _radians);
    abstract float startAnim(String _animation, boolean _isLooping, float _speed);
    abstract void stopAnim(String _animation);
    abstract void restart(String _animation);
    abstract void setDimentions(String _animation, float _w, float _h);
    abstract void setOffset(String _animation, Vec2 _offset);
}
