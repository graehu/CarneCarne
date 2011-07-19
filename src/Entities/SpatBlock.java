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
    
    float mRadius; /// This is only needed if I need to render it, thats all
    int mRootId;
    SpatBlock(iSkin _skin, int _rootId, float _radius) /// Note, in SpatBlockFactory this currently hard coded to 0.125f
    {
        super(_skin);
        mRadius = _radius;
        mRootId = _rootId;
    }
    public void update()
    {
        if (mWaterTiles != 0)
            buoyancy();
    }
    public int getRootId()
    {
        return mRootId;
    }
    @Override
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(getBody().getPosition().add(new Vec2(0.5f-mRadius,0.5f-mRadius))); /// FIXME
        mSkin.render(pixelPosition.x,pixelPosition.y);
        mSkin.setRotation(getBody().getAngle()*(180/(float)Math.PI));
    }
}
