/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Events.EntityDeathEvent;
import Events.TarFireEvent;
import Events.sEvents;
import Graphics.Particles.ParticleSys;
import Graphics.Particles.sParticleManager;
import Graphics.Skins.iSkin;
import Level.Tile;
import World.sWorld;
import World.sWorld.BodyCategories;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.ContactEdge;

/**
 *
 * @author alasdair
 */
public class FireParticle extends Entity
{
    Vec2 mVelocity;
    ParticleSys mParticles;
    int mTimer;
    public FireParticle(iSkin _skin, Vec2 _velocity)
    {
        super(_skin);
        mVelocity = _velocity;
        mTimer = 10000;
        mParticles = sParticleManager.createSystem("Fire", new Vec2(0, 0),-1);
        mTimer = 100;
    }
    @Override
    public void update()
    {
        mTimer--;
        if (mTimer == 0)
        {
            kill();
        }
        else
        {
            mBody.setLinearVelocity(mVelocity);
            int tileMask = (1 << BodyCategories.eEdibleTiles.ordinal()) |
                (1 << BodyCategories.eNonEdibleTiles.ordinal()) |
                (1 << BodyCategories.eWater.ordinal()) |
                (1 << BodyCategories.eIce.ordinal()) |
                (1 << BodyCategories.eGum.ordinal()) |
                (1 << BodyCategories.eTar.ordinal());
            for (ContactEdge edge = mBody.getContactList(); edge != null; edge = edge.next)
            {
                if (edge.contact.isTouching() && (edge.other.m_fixtureList.m_filter.categoryBits & tileMask) != 0)
                {
                    Tile tile = (Tile)edge.contact.m_fixtureA.getUserData();
                    if (tile == null)
                        tile = (Tile)edge.contact.m_fixtureB.getUserData();
                    ParticleSys system = sParticleManager.createSystem(tile.getAnimationsName() + "FireHit", this.mBody.getPosition().add(new Vec2(0.5f,0.5f)).mul(64.0f), 120);
                    Vec2 direction = this.mBody.getLinearVelocity();
                    direction.normalize();
                    float offset = (float)Math.atan2(direction.y, direction.x) * 180.0f/(float)Math.PI;
                    system.setAngularOffset(offset-90.0f);
                    tile.setOnFire();
                    kill();
                    break;
                }
            }
        }
    }
    @Override
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(mBody.getPosition().add(new Vec2(0.0f,0.0f))); /// FIXME
        Vec2 particlePosition = mBody.getPosition().add(new Vec2(0.5f,0.5f)).mul(64f);
        mParticles.moveEmittersTo(particlePosition.x, particlePosition.y);
        mSkin.render(pixelPosition.x,pixelPosition.y);
        mSkin.setRotation(mBody.getAngle()*(180/(float)Math.PI));
    }
    @Override
    public void kill()
    {
        sWorld.destroyBody(mBody);
        mParticles.kill();
    }
}
