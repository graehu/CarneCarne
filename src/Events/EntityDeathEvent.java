/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Entities.Entity;
import Entities.Entity.CauseOfDeath;

/**
 *
 * @author alasdair
 */
public class EntityDeathEvent extends iEvent {

    private Entity mEntity;
    private CauseOfDeath mCauseOfDeath;
    private Object mKiller;
    public EntityDeathEvent(Entity _entity, CauseOfDeath _causeOfDeath, Object _killer)
    {
        mEntity = _entity;
        mCauseOfDeath = _causeOfDeath;
        mKiller = _killer;
    }
    @Override
    public String getName()
    {
        return getType();// + mPlayer;
    }

    @Override
    public String getType()
    {
        return "PlayerDeathEvent";
    }
    
    @Override
    public boolean process()
    {
        mEntity.kill(mCauseOfDeath, mKiller);
        return true;
    }
}
