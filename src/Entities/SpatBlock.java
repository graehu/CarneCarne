/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Particles.ParticleSysBase;
import Graphics.Particles.sParticleManager;
import Graphics.Skins.iSkin;
import Level.RootTile.AnimationType;
import Level.Tile;
import World.sWorld;
import World.sWorld.BodyCategories;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.ContactEdge;

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
        int tileMask = (1 << BodyCategories.eEdibleTiles.ordinal()) |
            (1 << BodyCategories.eNonEdibleTiles.ordinal()) |
            (1 << BodyCategories.ePlayer.ordinal()) |
            (1 << BodyCategories.eEnemy.ordinal()) |
            (1 << BodyCategories.eWater.ordinal()) |
            (1 << BodyCategories.eGum.ordinal()) |
            (1 << BodyCategories.eTar.ordinal());
        for (ContactEdge edge = getBody().getContactList(); edge != null; edge = edge.next)
        {
            if (edge.contact.isTouching() && (edge.other.m_fixtureList.m_filter.categoryBits & tileMask) != 0)
            {
                Entity entity = (Entity)edge.contact.m_fixtureB.getBody().getUserData();
                if (entity == this)
                    entity = (Entity)edge.contact.m_fixtureA.getBody().getUserData();
                try
                {
                    ((AIEntity)entity).stun();
                }
                catch (Throwable e) /// Null pointer and invalid cast catch
                {
                    
                }
                kill();
                break;
            }
        }
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
