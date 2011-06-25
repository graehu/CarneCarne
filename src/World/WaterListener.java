/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.Entity;
import Entities.PlayerEntity;
import Events.PlayerDeathEvent;
import Events.sEvents;
import Level.sLevel;
import Level.sLevel.TileType;
import World.sWorld.BodyCategories;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
class WaterListener implements iListener {

    public WaterListener() {
    }

    public void beginContact(Contact _contact)
    {
        try
        {
            if (_contact.m_fixtureA.m_filter.categoryBits == (1 << BodyCategories.eWater.ordinal()))
            {
                TileType tileType = sLevel.TileType.class.getEnumConstants()[_contact.m_fixtureA.m_filter.groupIndex];
                if (tileType.equals(TileType.eAcid))
                {
                    sEvents.triggerDelayedEvent(new PlayerDeathEvent(((Entity)_contact.m_fixtureB.m_body.m_userData)));
                }
                ((Entity)_contact.m_fixtureB.m_body.m_userData).submerge(((Integer)_contact.m_fixtureA.m_userData));
            }
            else
            {
                TileType tileType = sLevel.TileType.class.getEnumConstants()[_contact.m_fixtureB.m_filter.groupIndex];
                if (tileType.equals(TileType.eAcid))
                {
                    sEvents.triggerDelayedEvent(new PlayerDeathEvent(((Entity)_contact.m_fixtureA.m_body.m_userData)));
                }
                ((Entity)_contact.m_fixtureA.m_body.m_userData).submerge(((Integer)_contact.m_fixtureB.m_userData));        
            }
        }
        catch (NullPointerException e)
        {
            
        }
    }

    public void endContact(Contact _contact)
    {
        if (_contact.m_fixtureA.m_filter.categoryBits == (1 << BodyCategories.eWater.ordinal()))
        {
            ((Entity)_contact.m_fixtureB.m_body.m_userData).unsubmerge();
        }
        else
        {
            ((Entity)_contact.m_fixtureA.m_body.m_userData).unsubmerge();          
        }
    }

    public void preSolve(Contact _contact, Manifold _manifold) {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse) {
    }
    
}
