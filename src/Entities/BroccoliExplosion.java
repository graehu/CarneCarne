/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.ContactEdge;

/**
 *
 * @author alasdair
 */
public class BroccoliExplosion extends Entity
{
    private int mTimer;
    public BroccoliExplosion(iSkin _skin, int _timer)
    {
        super(_skin);
    }
    @Override
    public void update()
    {
        mTimer--;
        if (mTimer == 0)
        {
            kill(CauseOfDeath.eMundane, this);
        }
        for (ContactEdge edge = mBody.getContactList(); edge != null; edge = edge.next)
        {
            Vec2 direction = edge.other.getPosition().sub(mBody.getPosition());
            float length = direction.normalize();
            edge.other.applyLinearImpulse(direction.mul(2.0f), edge.other.getPosition());
        }
    }
    
    @Override
    public void render()
    {
        
    }
    
}
