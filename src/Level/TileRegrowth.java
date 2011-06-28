/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author alasdair
 */
public class TileRegrowth
{
    private TileGrid mTileGrid;
    private int mFrames;
    private PriorityQueue<RegrowingTile> timingTiles = new PriorityQueue<RegrowingTile>(10, new TileComparer());
    //private PriorityQueue<RegrowingTile> activeTiles = new PriorityQueue<RegrowingTile>(10, new TileComparer());
    private RegrowingTile[][] inactiveTiles;
    public TileRegrowth(TileGrid _tileGrid, int _width, int _height)
    {
        mTileGrid = _tileGrid;
        mFrames = 0;
        inactiveTiles = new RegrowingTile[_width][_height];
    }
    public void add (int _x, int _y, RootTile _rootId)
    {
        timingTiles.add(new RegrowingTile(_x,_y, _rootId ,mFrames+180));
    }
    public void update()
    {
        mFrames++;
        RegrowingTile tile = timingTiles.peek();
        if (tile != null && tile.timer < mFrames)
        {
            timingTiles.remove(tile);
            if (isSupported(tile))
            {
                placeTile(tile);
            }
            else
            {
                inactiveTiles[tile.x][tile.y] = tile;
            }
        }
    }
    private void placeTile(RegrowingTile _tile)
    {
        mTileGrid.placeTile(_tile.x, _tile.y, _tile.mRootId.mId);
        checkInactive(_tile.x-1,_tile.y);
        checkInactive(_tile.x+1,_tile.y);
        checkInactive(_tile.x,_tile.y-1);
        checkInactive(_tile.x,_tile.y+1);
    }
    private void checkInactive(int _x, int _y)
    {
        RegrowingTile tile = inactiveTiles[_x][_y];
        if (tile != null)
        {
            inactiveTiles[_x][_y] = null;
            tile.timer = mFrames + 30;
            timingTiles.add(tile);
            //placeTile(tile);
        }
    }
    private boolean isSupported(RegrowingTile _tile)
    {
        if (_tile.mRootId.mAnchor)
        {
            return true;
        }
        return (mTileGrid.boundaryFrom(_tile.x, _tile.y-1, Direction.eFromDown, _tile.mRootId.mTileType) ||
                (mTileGrid.boundaryFrom(_tile.x-1, _tile.y, Direction.eFromRight, _tile.mRootId.mTileType)) ||
                (mTileGrid.boundaryFrom(_tile.x, _tile.y+1, Direction.eFromUp, _tile.mRootId.mTileType)) ||
                (mTileGrid.boundaryFrom(_tile.x+1, _tile.y, Direction.eFromLeft, _tile.mRootId.mTileType)));
    }
    private class RegrowingTile {

        public RegrowingTile(int _x, int _y, RootTile _rootId, int _timer)
        {
            x = _x;
            y = _y;
            mRootId = _rootId;
            timer = _timer;
        }
        int x, y;
        RootTile mRootId;
        int timer;
    }

    private static class TileComparer implements Comparator<RegrowingTile> 
    {
        public int compare(RegrowingTile x, RegrowingTile y)
        {
            if (x.timer < y.timer)
            {
                return -1;
            }
            if (x.timer > y.timer)
            {
                return 1;
            }
            return 0;
        }
    }
}
