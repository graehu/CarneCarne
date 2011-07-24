/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import World.sWorld;
import java.util.ArrayList;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
public class StartBarrier implements iEventListener
{
    public boolean trigger(iEvent _event)
    {
        if (_event.getName().equals("RaceStartEvent"))
        {
            disable();
        }
        else
        {
            enable();
        }
        return true;
    }
    private class BarrierTile
    {
        int x, y;
        int mRootId;
        Body mBody;
        public BarrierTile(int _x, int _y, int _rootId)
        {
            x = _x;
            y = _y;
            mRootId = _rootId;
            mBody = null;
        }
    }
    ArrayList<BarrierTile> tiles = new ArrayList<BarrierTile>();

    public StartBarrier()
    {
        sEvents.subscribeToEvent("RaceStartEvent", this);
        sEvents.subscribeToEvent("RaceWonEvent", this);
    }
    
    void addTile(int _x, int _y, int _rootId)
    {
        tiles.add(new BarrierTile(_x, _y, _rootId));
    }
    public void enable()
    {
        for (BarrierTile tile: tiles)
        {
            sLevel.placeTileNoBody(tile.x, tile.y, tile.mRootId);
            HashMap params = new HashMap();
            params.put("position", new Vec2(tile.x,tile.y));
            tile.mBody = sWorld.useFactory("GroundBody", params);
        }
    }
    public void disable()
    {
        for (BarrierTile tile: tiles)
        {
            //sLevel.getTileGrid().mTiles[tile.x][tile.y].destroyFixture();
            if(tile.mBody != null)
            {
                sWorld.destroyBody(tile.mBody);
                if (tile.mBody.getFixtureList() != null)
                {
                    throw new UnsupportedOperationException("Body wasn't destroyed");
                }
                tile.mBody = null;
            }
            sLevel.placeTile(tile.x, tile.y, 0);
        }
    }
}
