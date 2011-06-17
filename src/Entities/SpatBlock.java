/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.iSkin;
import Physics.sPhysics;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class SpatBlock extends Entity {
    
    SpatBlock(iSkin _skin)
    {
        super(_skin);
    }
    public void update()
    {
        
    }
    public void render()
    {
        Vec2 pixelPosition = sPhysics.translateToWorld(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
        mSkin.setRotation("body", mBody.getAngle()*(180/3.14f));
    }
}
