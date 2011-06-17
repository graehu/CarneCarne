/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
abstract class iSprite
{
    protected iSkin mSkin;
    protected Vec2 mPosition; /// This will either be in screen space or world space depending on implementation
    public iSprite(iSkin _skin)
    {
        mSkin = _skin;
    }
    public void setPosition(Vec2 _position)
    {
        mPosition = _position;
    }
    abstract public void render();
}
