/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Graphics.iSkin;
import java.util.HashMap;
import org.newdawn.slick.Animation;
import org.newdawn.slick.PackedSpriteSheet;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Aaron
 */
public class AnimatedSkin implements iSkin{
    Animation mAnim;
    
    //constructor public to graphics package only
    AnimatedSkin(String _spriteSheet, int _duration) throws SlickException
    {
        mAnim = new Animation(new SpriteSheet("data/" + _spriteSheet + ".png", 64, 64), _duration);
        mAnim.restart();
    }
    //constructor public to graphics package only
    AnimatedSkin(SpriteSheet _spriteSheet, int _duration) throws SlickException
    {
        mAnim = new Animation(_spriteSheet, _duration);
        mAnim.restart();
    }
    public void render(float _x, float _y)
    {
        //add to render list under given sprite sheet (batch rending)
        mAnim.draw(_x, _y); //quick fix for render
    }
    public void render(float _x, float _y, float _w, float _h)
    {
        mAnim.draw(_x, _y, _w, _h);
    }
    //internal graphics function for batch rendering
    void renderInUse(float _x, float _y)
    {
        //mAnimation.renderInUse(_x, _y);
    }
    //sets rotation of current frame
    public void setRotation(float _radians) {
        mAnim.getCurrentFrame().setRotation(_radians);
    }
    
    //animation control functions
    public void restart()
    {
        mAnim.restart();
    }

    public void startAnim(String _animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stopAnim(String _animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void restart(String _animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setRotation(String _animation, float _radians) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
