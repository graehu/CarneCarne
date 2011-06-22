/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Skins;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Almax
 */
public class StaticSkin implements iSkin
{
    float mWidth, mHeight;
    Image mImage;
    //constructor public to graphics package only
    StaticSkin(String _image) throws SlickException
    {
        mImage = new Image(_image);
    }
    StaticSkin(Image _image) throws SlickException
    {
        mImage = _image;
    }
    public void render(float _x, float _y)
    {
        if(mWidth == 0 && mHeight == 0)
            mImage.draw(_x, _y);
        else
            mImage.draw(_x, _y, mWidth, mHeight);
    }
    public void setRotation(float _radians) {
        mImage.setRotation(_radians);
    }
    
    public void setDimentions(float _w, float _h) {
        if(_w != 0)
            mWidth = _w;
        else
            mWidth = mImage.getWidth();
        if(_h != 0)
            mHeight = _h;
        else
            mHeight = mImage.getHeight();
    }

    public void restart() {
        assert(false);
    }

    public void stopAnim(String _animation) {
        assert(false);
    }

    public void restart(String _animation) {
        assert(false);
    }

    public void setRotation(String _animation, float _radians) {
        assert(false);
    }

    public void setIsLooping(boolean _isLooping) {
        assert(false);
    }

    public void setSpeed(float _speed) {
        assert(false);
    }

    public float getDuration() {
        return 0.0f;
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

    public Vec2 getOffset(String _animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
