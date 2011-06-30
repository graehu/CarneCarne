/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.Entity;
import Entities.FireParticle;
import Events.EntityDeathEvent;
import Events.TarFireEvent;
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
class HighImpulseListener implements iListener
{

    public HighImpulseListener() {
    }

    public void beginContact(Contact _contact)
    {
        float impulse = 0; /// Only need to check one impulse because carne is a circle
        if (impulse > 0.2f)
        {
            if (_contact.m_fixtureA.m_filter.categoryBits != (1 << BodyCategories.ePlayer.ordinal()))
            {
                sEvents.triggerDelayedEvent(new EntityDeathEvent((Entity)_contact.m_fixtureB.m_body.getUserData()));
            }
            else 
            {
                sEvents.triggerDelayedEvent(new EntityDeathEvent((Entity)_contact.m_fixtureA.m_body.getUserData()));       
            }
        }
    }

    public void endContact(Contact _contact) {
    }

    public void preSolve(Contact _contact, Manifold _manifold)
    {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse)
    {
        float impulse = _impulse.normalImpulses[0]; /// Only need to check one impulse because carne is a circle
        if (impulse > 50.0f)
        {
            if (_contact.m_fixtureA.m_filter.categoryBits != (1 << BodyCategories.ePlayer.ordinal()))
            {
                sEvents.triggerDelayedEvent(new EntityDeathEvent((Entity)_contact.m_fixtureB.m_body.getUserData()));
            }
            else 
            {
                sEvents.triggerDelayedEvent(new EntityDeathEvent((Entity)_contact.m_fixtureA.m_body.getUserData()));       
            }
        }
    }
}