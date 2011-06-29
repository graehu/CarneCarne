/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.Entity;
import Events.TarFireEvent;
import Entities.FireParticle;
import Events.EntityDeathEvent;
import Events.sEvents;
import Level.Tile;
import World.sWorld.BodyCategories;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
class TarFireListener implements iListener
{

    public TarFireListener()
    {
    }

    public void beginContact(Contact _contact)
    {
        if (_contact.m_fixtureA.m_filter.categoryBits != (1 << BodyCategories.eFire.ordinal()))
        {
            sEvents.triggerDelayedEvent(new TarFireEvent((Tile)_contact.m_fixtureA.getUserData(),(FireParticle)_contact.m_fixtureB.m_body.getUserData()));
            sEvents.triggerDelayedEvent(new EntityDeathEvent((Entity)_contact.m_fixtureB.m_body.getUserData()));
        }
        else 
        {
            sEvents.triggerDelayedEvent(new TarFireEvent((Tile)_contact.m_fixtureB.getUserData(),(FireParticle)_contact.m_fixtureA.m_body.getUserData())); 
            sEvents.triggerDelayedEvent(new EntityDeathEvent((Entity)_contact.m_fixtureA.m_body.getUserData()));       
        }
    }

    public void endContact(Contact _contact) {
    }

    public void preSolve(Contact _contact, Manifold _manifold)
    {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse) {
    }
    
}
