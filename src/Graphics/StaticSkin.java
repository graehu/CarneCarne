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
        throw new UnsupportedOperationException("Not supported yet.");
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
