/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Almax
 */
class StaticSkin implements iSkin
{
    Image mImage;
    StaticSkin(String _image) throws SlickException
    {
        mImage = new Image(_image);
    }
    public void render(float _x, float _y)
    {
        mImage.draw(_x, _y);
    }
    public void render(float _x, float _y, float _w, float _h)
    {
        mImage.draw(_x, _y, _w, _h);
    }
}
