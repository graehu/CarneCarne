/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Entities.Entity;

/**
 *
 * @author alasdair
 */
public class PlayerDeathEvent extends iEvent {

    private Entity mEntity;
    public PlayerDeathEvent(Entity _entity)
    {
        mEntity = _entity;
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
    
    public void process()
    {
        mEntity.kill();
    }
}
