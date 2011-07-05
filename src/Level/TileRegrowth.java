/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Graphics.Particles.sParticleManager;
import Level.Tile.Direction;
import java.util.Comparator;
import java.util.PriorityQueue;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class TileRegrowth
{
    private static int nonAnimatedRegrowthTime = 120;
    private static int animatedRegrowthTime = 60;
    private LevelTileGrid mTileGrid;
    private int mFrames;
    private PriorityQueue<RegrowingTile> timingTiles = new PriorityQueue<RegrowingTile>(10, new TileComparer());
    private PriorityQueue<RegrowingTile> respawningTiles = new PriorityQueue<RegrowingTile>(10, new TileComparer());
    //private PriorityQueue<RegrowingTile> activeTiles = new PriorityQueue<RegrowingTile>(10, new TileComparer());
    private RegrowingTile[][] inactiveTiles;
    public TileRegrowth(TileGrid _tileGrid, int _width, int _height)
    {
        try
        {
            mTileGrid = (LevelTileGrid)_tileGrid;
        }
        catch (ClassCastException e)
        {
            mTileGrid = null;
        }
        mFrames = 0;
        inactiveTiles = new RegrowingTile[_width][_height];
    }
    public void add (int _x, int _y, RootTile _rootId)
    {
        //mTileGrid.tiledMap.createAnimatedTile(_x, _y, _rootId.mAnimationsName + "GrowBack");
        RegrowingTile tile = new RegrowingTile(_x,_y, _rootId ,mFrames+nonAnimatedRegrowthTime);
        boolean supported = false;
        try
        {
            supported = isSupported(tile);
        }
        catch(NullPointerException e)
        {
            
        }
        if (supported)
        {
            timingTiles.add(new RegrowingTile(_x,_y, _rootId ,mFrames+180));
        }
        else
        {
            inactiveTiles[tile.x][tile.y] = tile;
        }
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
                respawningTiles.add(tile);
                tile.timer += animatedRegrowthTime;
                mTileGrid.tiledMap.createAnimatedTile(tile.x, tile.y,tile.mRootId.mAnimationsName + "GrowBack");
            }
            else
            {
                inactiveTiles[tile.x][tile.y] = tile;
            }
        }
        tile = respawningTiles.peek();
        if (tile != null && tile.timer < mFrames)
        {
            respawningTiles.remove(tile);
            if (isSupported(tile))
            {
                placeTile(tile);
                sParticleManager.createSystem(tile.mRootId.mAnimationsName + "SpawnParticle", new Vec2(32.0f+(tile.x*64.0f), 32.0f+(tile.y*64.0f)), 600);
                mTileGrid.tiledMap.destroyAnimatedTile(tile.x, tile.y);
            }
            else
            {
                inactiveTiles[tile.x][tile.y] = tile;
            }
        }
    }
    private void placeTile(RegrowingTile _tile)
    {
        if (_tile.mRootId.getNext() == null)
            mTileGrid.placeTile(_tile.x, _tile.y, _tile.mRootId.mId);
        else
            mTileGrid.placeTile(_tile.x, _tile.y, _tile.mRootId.getNext().mId);
        mTileGrid.tiledMap.destroyAnimatedTile(_tile.x, _tile.y);
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
