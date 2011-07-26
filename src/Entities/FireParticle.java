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
import Level.sLevel.TileType;
import Score.ScoreTracker.ScoreEvent;
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
    ParticleSysBase mParticles;
    int mTimer;
    PlayerEntity mOwner;
    public FireParticle(iSkin _skin, Vec2 _velocity, PlayerEntity _owner)
    {
        super(_skin);
        mVelocity = _velocity;
        mOwner = _owner;
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
            getBody().setLinearVelocity(mVelocity);
            int tileMask = (1 << BodyCategories.eEdibleTiles.ordinal()) |
                (1 << BodyCategories.eNonEdibleTiles.ordinal()) |
                (1 << BodyCategories.eWater.ordinal()) |
                (1 << BodyCategories.eGum.ordinal()) |
                (1 << BodyCategories.eTar.ordinal());
            for (ContactEdge edge = getBody().getContactList(); edge != null; edge = edge.next)
            {
                    Tile tile = (Tile)edge.contact.m_fixtureA.getUserData();
                    if (tile == null)
                        tile = (Tile)edge.contact.m_fixtureB.getUserData();
                if (edge.contact.isTouching() && (edge.other.m_fixtureList.m_filter.categoryBits & tileMask) != 0)
                {
                    Vec2 position = this.getBody().getPosition();
                    Vec2 direction = this.getBody().getLinearVelocity();
                    String animationName = tile.getAnimationsName(AnimationType.eFireHit);
                    if (!tile.getTileType().equals(TileType.eIce))
                        kill();
                    tile.setOnFire();
                    ParticleSysBase system = sParticleManager.createSystem(animationName + "FireHit", position.add(new Vec2(0.5f,0.5f)).mul(64.0f), 2);
                    direction.normalize();
                    float offset = (float)Math.atan2(direction.y, direction.x) * 180.0f/(float)Math.PI;
                    system.setAngularOffset(offset-90.0f);
                    break;
                }
            }
        }
    }
    @Override
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(getBody().getPosition().add(new Vec2(0.0f,0.0f))); /// FIXME
        Vec2 particlePosition = getBody().getPosition().add(new Vec2(0.5f,0.5f)).mul(64f);
        mParticles.moveEmittersTo(particlePosition.x, particlePosition.y);
        mSkin.render(pixelPosition.x,pixelPosition.y);
        mSkin.setRotation(getBody().getAngle()*(180/(float)Math.PI));
    }
    @Override
    public void kill()
    {
        sWorld.destroyBody(getBody());
        mParticles.kill();
    }

    public void killedOpponent()
    {
        mOwner.mScoreTracker.score(ScoreEvent.eKilledOpponent);
    }
}
