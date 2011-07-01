/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.Entity;
import Entities.PlayerEntity;
import World.sWorld;

/**
 *
 * @author alasdair
 */
abstract public class AreaEvent extends Entity
{
    int x,y,x2,y2;
    public AreaEvent(int _x, int _y, int _x2, int _y2)
    {
        super(null);
        x = _x;
        y = _y;
        x2 = _x2;
        y2 = _y2;
        if (x != -1)
            mBody = sWorld.createAreaEvent(_x, _y, _x2, _y2, this);
    }
    
    abstract public void enter(PlayerEntity _entity);
    abstract public void leave(PlayerEntity _entity);
    @Override
    public final void render()
    {
        
    }
    public void update()
    {
        
    }
}
