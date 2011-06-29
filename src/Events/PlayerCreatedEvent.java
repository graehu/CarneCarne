/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Entities.Entity;

/**
 *
 * @author A203946
 */
public class PlayerCreatedEvent extends iEvent {
    
    Entity mEntity;
    int mPlayerID;
    public PlayerCreatedEvent(Entity _entity, int _playerID)
    {
        mEntity = _entity;
        mPlayerID = _playerID;
    }
    public String getName()
    {
        return getType();
    }
    public String getType()
    {
        return "PlayerCreatedEvent";
    }
    public int getPlayerID()
    {
        return mPlayerID;
    }
}
