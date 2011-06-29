/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.AIEntity;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
class JumpListener implements iListener
{

    public JumpListener()
    {
    }

    public void beginContact(Contact _contact)
    {
        /*if (_contact.m_fixtureA.m_filter.categoryBits == (1 << sWorld.BodyCategories.ePlayer.ordinal()) ||
                _contact.m_fixtureA.m_filter.categoryBits == (1 << sWorld.BodyCategories.eEnemy.ordinal()))
        {
            ((AIEntity)_contact.m_fixtureA.m_body.getUserData()).canJump();
        }
        else
        {
            ((AIEntity)_contact.m_fixtureB.m_body.getUserData()).canJump();
        }*/
    }

    public void endContact(Contact _contact)
    {
        /*if (_contact.m_fixtureA.m_filter.categoryBits == (1 << sWorld.BodyCategories.ePlayer.ordinal()) ||
                _contact.m_fixtureA.m_filter.categoryBits == (1 << sWorld.BodyCategories.eEnemy.ordinal()))
        {
            ((AIEntity)_contact.m_fixtureA.m_body.getUserData()).cantJump();
        }
        else
        {
            ((AIEntity)_contact.m_fixtureB.m_body.getUserData()).cantJump();
        }*/
    }

    public void preSolve(Contact _contact, Manifold _manifold)
    {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse)
    {
    }
    
}
