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
    public PlayerCreatedEvent(Entity _entity)
    {
        mEntity = _entity;
    }
    public String getName()
    {
        return getType();
    }
    public String getType()
    {
        return "PlayerCreatedEvent";
    }
}
