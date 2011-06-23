/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;

/**
 *
 * @author alasdair
 */
public class CaveIn extends Entity
{
    public static class Tile
    {
        public Image mImage;
        public Vec2 mPosition;
        public Tile(Image _image, Vec2 _position)
        {
            mImage = _image;
            mPosition = _position;
        }
    }
    CaveIn(iSkin _skin)
    {
        super(_skin);
    }
    @Override
    public void update()
    {
    }
    public void render()
    {
        mSkin.render(mBody.getPosition().x,mBody.getPosition().y);
    }
    
}
