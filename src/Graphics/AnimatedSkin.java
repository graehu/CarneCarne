/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Aaron
 */
//class public to graphics package only
class AnimatedSkin implements iSkin{
    Animation mAnimation;
    AnimatedSkin(String spriteSheet, int[] frames, int[] durations) throws SlickException
    {
       // mAnimation = new Animation(, frames, durations);
    }
    AnimatedSkin(String spriteSheet, int _x1, int _y1, int _x2, int _y2, boolean _horizontalScan, int _duration) throws SlickException
    {
        mAnimation = new Animation(new SpriteSheet(new Image(spriteSheet), 64, 64), _x1, _y1, _x2, _y2, _horizontalScan, _duration, false);
        mAnimation.setAutoUpdate(true);
    }
    public void render(float _x, float _y)
    {
        //add to render list under given sprite sheet (batch rending)
         mAnimation.draw(_x, _y); //quick fix for render
    }
    public void render(float _x, float _y, float _w, float _h)
    {
        mAnimation.draw(_x, _y, _w, _h);
    }
    //internal graphics function for batch rendering
    void renderInUse(float _x, float _y)
    {
        //mAnimation.renderInUse(_x, _y);
    }
    
}
