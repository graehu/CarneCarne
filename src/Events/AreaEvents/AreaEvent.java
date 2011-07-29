/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.AIEntity;
import Entities.Entity;
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
            setBody(sWorld.createAreaEvent(_x, _y, _x2, _y2, this));
    }
    
    abstract public void enter(AIEntity _entity);
    abstract public void leave(AIEntity _entity);
    @Override
    public final void render()
    {
        /*Vec2 topLeft = sWorld.translateToWorld(mBody.getFixtureList().getAABB().lowerBound);
        Vec2 dims = sWorld.translateToWorld(mBody.getFixtureList().getAABB().upperBound).sub(topLeft);
        Shape shape = new Rectangle(topLeft.x, topLeft.y, dims.x, dims.y);
        sGraphicsManager.fill(shape);*/
    }
    public void update()
    {
        
    }
}
