/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Sprites;

import Graphics.Skins.iSkin;
import org.jbox2d.common.Vec2;

/**
 *
 * @author a203945
 */
public abstract class iSprite {
    protected Vec2 mPos = new Vec2(0,0);
    protected iSkin mSkin = null;
    protected iSprite()
    {
        
    }
    /* _x: world transform in X (pixels)
     * _y: world transform in Y (pixels)
     */
    public void render(float _x, float _y)
    {
        mSkin.render(_x + mPos.x, _y + mPos.y);
    }
    public void setRotation(float _radians)
    {
        mSkin.setRotation(_radians);
    }
    public void setPosition(Vec2 _pos)
    {
        mPos = _pos;
    }
}
