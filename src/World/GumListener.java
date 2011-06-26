/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.SpatBlock;
import Events.GumLandEvent;
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
class GumListener implements iListener {

    public GumListener() {
    }

    public void beginContact(Contact _contact)
    {
        if (_contact.m_fixtureA.m_filter.categoryBits == (1 << BodyCategories.eGum.ordinal()))
        {
            int x = (int)(_contact.m_fixtureA.m_body.getPosition().x+0.0625f);
            int y = (int)(_contact.m_fixtureA.m_body.getPosition().y+0.0625f);
            int rootId = ((SpatBlock)_contact.m_fixtureA.getBody().getUserData()).getRootId();
            Tile tile = (Tile)_contact.m_fixtureB.getUserData();
            sEvents.triggerDelayedEvent(new GumLandEvent(x,y,_contact.m_fixtureA.m_body,rootId,tile));
        }
        else
        {
            int x = (int)(_contact.m_fixtureB.m_body.getPosition().x+0.0625f);
            int y = (int)(_contact.m_fixtureB.m_body.getPosition().y+0.0625f);
            int rootId = ((SpatBlock)_contact.m_fixtureB.getBody().getUserData()).getRootId();
            Tile tile = (Tile)_contact.m_fixtureA.getUserData();
            sEvents.triggerDelayedEvent(new GumLandEvent(x,y,_contact.m_fixtureB.m_body,rootId,tile));
        }
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
