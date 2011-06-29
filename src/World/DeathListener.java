/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.Entity;
import Events.EntityDeathEvent;
import Events.sEvents;
import Level.Tile;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
class DeathListener implements iListener
{
    public DeathListener()
    {
    }

    public void beginContact(Contact _contact)
    {
        Tile tile = (Tile)_contact.m_fixtureA.getUserData();//sLevel.TileType.class.getEnumConstants()[_contact.m_fixtureA.m_filter.groupIndex];
        if (_contact.m_fixtureA.m_filter.categoryBits == (1 << sWorld.BodyCategories.eSpikes.ordinal()))
        {
            sEvents.triggerDelayedEvent(new EntityDeathEvent(((Entity)_contact.m_fixtureB.m_body.m_userData)));
        }
        else
        {
            sEvents.triggerDelayedEvent(new EntityDeathEvent(((Entity)_contact.m_fixtureA.m_body.m_userData)));
        }
    }

    public void endContact(Contact _contact)
    {
    }

    public void preSolve(Contact _contact, Manifold _manifold) {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse) {
    }
    
}
