/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Graphics.iSkin;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Almax
 */
public class StaticSkin implements iSkin
{
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
        mImage.draw(_x, _y);
    }
    public void render(float _x, float _y, float _w, float _h)
    {
        mImage.draw(_x, _y, _w, _h);
    }
    public void setRotation(float _radians) {
        mImage.setRotation(_radians);
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

}
