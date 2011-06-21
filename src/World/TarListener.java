/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.Entity;
import World.sWorld.BodyCategories;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
class TarListener implements iListener {

    public TarListener()
    {
    }


    public void beginContact(Contact _contact)
    {
        if (_contact.m_fixtureA.m_filter.categoryBits == (1 << BodyCategories.eTar.ordinal()))
        {
            ((Entity)_contact.m_fixtureB.m_body.m_userData).tar();
        }
        else
        {
            ((Entity)_contact.m_fixtureA.m_body.m_userData).tar();          
        }
    }

    public void endContact(Contact _contact)
    {
        if (_contact.m_fixtureA.m_filter.categoryBits == (1 << BodyCategories.eTar.ordinal()))
        {
            ((Entity)_contact.m_fixtureB.m_body.m_userData).untar();
        }
        else
        {
            ((Entity)_contact.m_fixtureA.m_body.m_userData).untar();          
        }
    }

    public void preSolve(Contact _contact, Manifold _manifold) {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse) {
    }
}
