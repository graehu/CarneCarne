/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Graphics.iSkin;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Animation;
import org.newdawn.slick.PackedSpriteSheet;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Aaron
 */
public class AnimatedSkin implements iSkin{
    float mWidth, mHeight;
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
        if(mWidth == 0 && mHeight == 0)
            mAnim.draw(_x, _y);
        else
            mAnim.draw(_x, _y, mWidth, mHeight);
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
    public void setIsLooping(boolean _isLooping)
    {
        mAnim.setLooping(_isLooping);
    }
    public void setSpeed(float _speed)
    {
        mAnim.setSpeed(_speed);
    }
    
    public void setDimentions(float _w, float _h) {
        if(_w != 0)
            mWidth = _w;
        else
            mWidth = mAnim.getWidth();
        if(_h != 0)
            mHeight = _h;
        else
            mHeight = mAnim.getHeight();
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

    public float getDuration() {
        return ((float)(mAnim.getDuration(0)*mAnim.getFrameCount()))/1000.0f;
    }

    public float startAnim(String _animation, boolean _isLooping, float _speed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDimentions(String _animation, float _w, float _h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setOffset(String _animation, Vec2 _offset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
