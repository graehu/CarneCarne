/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import java.util.ArrayList;

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
        public BarrierTile(int _x, int _y, int _rootId)
        {
            x = _x;
            y = _y;
            mRootId = _rootId;
        }
    }
    ArrayList<BarrierTile> tiles = new ArrayList<BarrierTile>();

    public StartBarrier()
    {
        sEvents.subscribeToEvent("RaceStartEvent", this);
        sEvents.subscribeToEvent("RaceEndEvent", this);
    }
    
    void addTile(int _x, int _y, int _rootId)
    {
        tiles.add(new BarrierTile(_x, _y, _rootId));
    }
    public void enable()
    {
        for (BarrierTile tile: tiles)
        {
            sLevel.placeTile(tile.x, tile.y, tile.mRootId);
        }
    }
    public void disable()
    {
        for (BarrierTile tile: tiles)
        {
            sLevel.getTileGrid().mTiles[tile.x][tile.y].destroyFixture();
            //sLevel.placeTile(tile.x, tile.y, 0);
        }
    }
}
