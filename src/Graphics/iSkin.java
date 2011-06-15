/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import org.newdawn.slick.*;

/**
 *
 * @author aaron
 */
//class public to package only
public interface iSkin {
    abstract void render(float _x, float _y);
    abstract void render(float _x, float _y, float _w, float _h);
    
    //Animation methods
    abstract void setRotation(float _radians);
    abstract void restart();
    
    //character methods
    abstract void setRotation(String _animation,float _radians);
    abstract void startAnim(String _animation);
    abstract void stopAnim(String _animation);
    abstract void restart(String _animation);
}
