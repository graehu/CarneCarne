/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Level.Tile;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.ContactEdge;

/**
 *
 * @author alasdair
 */
class Explosion extends Entity
{
    int mTimer;
    PlayerEntity mKiller;
    public Explosion(PlayerEntity _killer)
    {
        super(null);
        mTimer = 30;
        mKiller = _killer;
    }

    @Override
    public void update()
    {
        mBody.setLinearVelocity(new Vec2(0,0));
        for (ContactEdge edge = mBody.getContactList(); edge != null; edge = edge.next)
        {
            if (edge.other.getUserData() != null)
            {
                ((Entity)edge.other.getUserData()).kill(CauseOfDeath.eFire, mKiller);
            }
            else
            {
                Fixture fixture = edge.contact.getFixtureA();
                if (fixture.getBody() == mBody)
                    fixture = edge.contact.getFixtureB();
                ((Tile)fixture.getUserData()).setOnFire();
            }
        }
        if (mTimer == 0)
        {
            sWorld.destroyBody(mBody);
        }
        mTimer--;
    }
    
    @Override
    public void render()
    {
        
    }
    
    @Override
    public void kill(CauseOfDeath _causeOfDeath, Object _killer) // I'm invincible!
    {
        
    }
    
}
