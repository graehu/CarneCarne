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
public class FireParticle extends Entity
{
    Vec2 mVelocity;
    public FireParticle(iSkin _skin, Vec2 _velocity)
    {
        super(_skin);
        mVelocity = _velocity;
    }
    @Override
    public void update()
    {
        mBody.setLinearVelocity(mVelocity);
    }
    @Override
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(mBody.getPosition().add(new Vec2(0.0f,0.0f))); /// FIXME
        mSkin.render(pixelPosition.x,pixelPosition.y);
        mSkin.setRotation(mBody.getAngle()*(180/(float)Math.PI));
    }
    
}
