/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class SpatBlock extends Entity {
    
    SpatBlock(iSkin _skin)
    {
        super(_skin);
    }
    public void update()
    {
        
    }
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
        mSkin.setRotation(mBody.getAngle()*(180/(float)Math.PI));
    }
}
