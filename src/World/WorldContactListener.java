/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.Entity;
import Level.sLevel.TileType;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
public class WorldContactListener implements ContactListener{

    public void beginContact(Contact _contact)
    {
        if (_contact.m_fixtureA.m_filter.groupIndex == TileType.eWater.ordinal())
        {
            float height = (_contact.m_fixtureA.m_body.getPosition().y - _contact.m_fixtureB.m_body.getPosition().y)*1.0f;
            if (height > 1.0f)
            {
                height = 1.0f;
            }
            ((Entity)_contact.m_fixtureB.m_body.m_userData).submerge(((Integer)_contact.m_fixtureA.m_userData));
        }
        if (_contact.m_fixtureB.m_filter.groupIndex == TileType.eWater.ordinal())
        {
            float height = (_contact.m_fixtureB.m_body.getPosition().y - _contact.m_fixtureA.m_body.getPosition().y)*1.0f;
            if (height > 1.0f)
            {
                height = 1.0f;
            }
            ((Entity)_contact.m_fixtureA.m_body.m_userData).submerge(((Integer)_contact.m_fixtureB.m_userData));          
        }
    }

    public void endContact(Contact _contact)
    {
        if (_contact.m_fixtureA.m_filter.groupIndex == TileType.eWater.ordinal())
        {
            float height = (_contact.m_fixtureA.m_body.getPosition().y - _contact.m_fixtureB.m_body.getPosition().y)*1.0f;
            if (height > 1.0f)
            {
                height = 1.0f;
            }
            ((Entity)_contact.m_fixtureB.m_body.m_userData).unsubmerge();
        }
        if (_contact.m_fixtureB.m_filter.groupIndex == TileType.eWater.ordinal())
        {
            float height = (_contact.m_fixtureB.m_body.getPosition().y - _contact.m_fixtureA.m_body.getPosition().y)*1.0f;
            if (height > 1.0f)
            {
                height = 1.0f;
            }
            ((Entity)_contact.m_fixtureA.m_body.m_userData).unsubmerge();          
        }
    }

    public void preSolve(Contact _contact, Manifold _manifold)
    {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse)
    {
    }
    
}
