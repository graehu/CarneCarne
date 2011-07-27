/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import World.sWorld;
import World.sWorld.BodyCategories;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
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
        int tileMask = (1 << BodyCategories.ePlayer.ordinal()) |
            (1 << BodyCategories.eEnemy.ordinal());
        //if (mBody.getLinearVelocity().length() > 100.0f)
        {
            tileMask |= (1 << BodyCategories.eEdibleTiles.ordinal()) |
                (1 << BodyCategories.eNonEdibleTiles.ordinal())|
                (1 << BodyCategories.eGum.ordinal()) |
                (1 << BodyCategories.eTar.ordinal());
        }
        for (ContactEdge edge = getBody().getContactList(); edge != null; edge = edge.next)
        {
            Entity entity = (Entity)edge.contact.m_fixtureB.getBody().getUserData();
            Fixture other = null;
            if (entity == this)
            {
                entity = (Entity)edge.contact.m_fixtureA.getBody().getUserData();
                other = edge.contact.m_fixtureA;
            }
            else other = edge.contact.m_fixtureB;
            if (edge.contact.isTouching() && (other.m_filter.categoryBits & tileMask) != 0)
            {
                try
                {
                    ((AIEntity)entity).stun();
                }
                catch (Throwable e) /// Null pointer and invalid cast catch
                {
                    
                }
                kill(CauseOfDeath.eMundane, null);
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
        mSkin.setRotation(getBody().getAngle()*(180/(float)Math.PI));
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }
}
