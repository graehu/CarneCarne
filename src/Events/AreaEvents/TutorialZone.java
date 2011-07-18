/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.PlayerEntity;

/**
 *
 * @author alasdair
 */
public class TutorialZone extends CheckPointZone
{

    public TutorialZone()
    {
        super(-1,-1,-1,-1, -1, null);
    }
    
    @Override
    public void enter(PlayerEntity _entity)
    {
    }

    @Override
    public void leave(PlayerEntity _entity)
    {
    }
    
}
