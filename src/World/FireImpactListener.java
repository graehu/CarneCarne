/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Events.TarFireEvent;
import Entities.FireParticle;
import Events.EntityDeathEvent;
import Events.sEvents;
import Graphics.Particles.ParticleSys;
import Graphics.Particles.sParticleManager;
import Level.Tile;
import World.sWorld.BodyCategories;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
class FireImpactListener implements iListener
{

    public FireImpactListener()
    {
    }

    public void beginContact(Contact _contact)
    {
        if (_contact.m_fixtureA.m_filter.categoryBits != (1 << BodyCategories.eFire.ordinal()))
        {
            FireParticle particle = (FireParticle)_contact.m_fixtureB.m_body.getUserData();
            Tile tile = (Tile)_contact.m_fixtureA.getUserData();
            begin(particle, tile);
        }
        else 
        {
            FireParticle particle = (FireParticle)_contact.m_fixtureA.m_body.getUserData();
            Tile tile = (Tile)_contact.m_fixtureB.getUserData();
            begin(particle, tile);       
        }
    }
    private void begin(FireParticle _particle, Tile _tile)
    {
        sEvents.triggerDelayedEvent(new TarFireEvent(_tile,_particle));
        ParticleSys system = sParticleManager.createSystem(_tile.getAnimationsName() + "FireHit", _particle.mBody.getPosition().add(new Vec2(0.5f,0.5f)).mul(64.0f), 120);
        Vec2 direction = _particle.mBody.getLinearVelocity();
        direction.normalize();
        float offset = (float)Math.atan2(direction.y, direction.x) * 180.0f/(float)Math.PI;
        system.setAngularOffset(offset-90.0f);
        sEvents.triggerDelayedEvent(new EntityDeathEvent(_particle));
    }

    public void endContact(Contact _contact)
    {
    }

    public void preSolve(Contact _contact, Manifold _manifold)
    {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse)
    {
    }
}
