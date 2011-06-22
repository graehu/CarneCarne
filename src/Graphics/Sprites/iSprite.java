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
    protected iSkin mSkin;
    protected Vec2 mPos;
    protected iSprite()
    {
        
    }
    /* _x: world transform in X (pixels)
     * _y: world transform in Y (pixels)
     */
    public void render(float _x, float y)
    {
        mSkin.render(_x + mPos.x, y + mPos.y);
    }
    public void setPosition(Vec2 _pos)
    {
        mPos = _pos;
    }
    
    public void setRotation(float _radians)
    {
        mSkin.setRotation(_radians);
    }
}
