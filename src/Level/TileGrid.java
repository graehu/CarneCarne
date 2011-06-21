/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.RootTile.TileShape;
import Level.Tile.Direction;
import Level.sLevel.TileType;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author A203946
 */
public class TileGrid {
    
    private Tile[][] mTiles;
    private int mFrames;
    private TiledMap tiledMap;
    private RootTileList rootTiles;
    private int layerIndex;
    public TileGrid(TiledMap _tiledMap, RootTileList _rootTiles, int _layerIndex)
    {
        tiledMap = _tiledMap;
        rootTiles = _rootTiles;
        layerIndex = _layerIndex;
        WaterSearcher waterSearcher = new WaterSearcher(_tiledMap.getWidth(),_tiledMap.getHeight());
        mFrames = 0;
        mTiles = new Tile[_tiledMap.getWidth()][_tiledMap.getHeight()];
        for (int i = 0; i < _tiledMap.getWidth(); i++)
        {
            for (int ii = 0; ii < _tiledMap.getHeight(); ii++)
            {
                int id = _tiledMap.getTileId(i, ii, _layerIndex);
                if (_rootTiles.get(id).mTileType.equals(TileType.eWater))
                {
                    waterSearcher.addTile(i,ii,_rootTiles.get(id));
                    //mTiles[i][ii] = new Tile(id,_rootTiles.get(id));
                    //mTiles[i][ii].createPhysicsBody(i, ii);
                }
                else
                {
                    mTiles[i][ii] = new Tile(id,_rootTiles.get(id));
                    mTiles[i][ii].createPhysicsBody(i, ii);
                }
            }
        }
        waterSearcher.finish(mTiles);
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < mTiles.length; i ++)
        {
            for (int ii = 0; ii < mTiles[0].length; ii++)
            {
                if (i > 0 && ii > 0 && i < _tiledMap.getWidth()-1 && ii <_tiledMap.getHeight()-1)
                {
                    mTiles[i][ii].checkEdges(i, ii, stack, this);
                }
            }
        }
        while (!stack.empty())
        {
            int id = stack.pop();
            int yTile = stack.pop();
            int xTile = stack.pop();
            _tiledMap.setTileId(xTile, yTile, _layerIndex, id);
        }
        regrowingTiles = new PriorityQueue<RegrowingTile>(10, new TileComparer());
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
    private static PriorityQueue<RegrowingTile> regrowingTiles;
    void update()
    {
        mFrames++;
        RegrowingTile tile = regrowingTiles.peek();
        if (tile != null && tile.timer == mFrames)
        {
            regrowingTiles.remove(tile);
            placeTile(tile.x, tile.y, tile.mRootId);
        }
    }
    public void placeTile(int _x, int _y, int _rootId)
    {
        mTiles[_x][_y].mId = _rootId;
        mTiles[_x][_y].mRootId = rootTiles.get(_rootId);
        mTiles[_x][_y].createPhysicsBody(_x, _y);
        int x;
        int y;
        Stack<Integer> stack = new Stack<Integer>();

        x = _x;
        y = _y;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x-1;
        y = _y;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x+1;
        y = _y;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x;
        y = _y-1;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x;
        y = _y+1;
        mTiles[x][y].checkEdges(x, y, stack, this);
        while (!stack.empty())
        {
            int id = stack.pop();
            int yTile = stack.pop();
            int xTile = stack.pop();
            tiledMap.setTileId(xTile, yTile, layerIndex, id);
        }
    }
    private void set(int _x, int _y, int _gid)
    {
        tiledMap.setTileId(_x, _y, layerIndex, _gid);
        mTiles[_x][_y].mId = _gid;
        mTiles[_x][_y].mRootId = rootTiles.get(_gid);
        
    }
    public void destroyTile(int _x, int _y)
    {
        Stack<Integer> stack = new Stack<Integer>();
        
        regrowingTiles.add(new RegrowingTile(_x,_y, mTiles[_x][_y].mRootId.mId,mFrames+180));
        
        set(_x,_y,0);
        int x;
        int y;        
        
        x = _x-1;
        y = _y;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x+1;
        y = _y;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x;
        y = _y-1;
        mTiles[x][y].checkEdges(x, y, stack, this);
        x = _x;
        y = _y+1;
        mTiles[x][y].checkEdges(x, y, stack, this);
        while (!stack.empty())
        {
            int id = stack.pop();
            int yTile = stack.pop();
            int xTile = stack.pop();
            tiledMap.setTileId(xTile, yTile, layerIndex, id);
        }
    }
    
    public Tile get(int _x, int _y)
    {
        return mTiles[_x][_y];
    }
    private static MaterialEdges materialEdges = new MaterialEdges();
    public boolean boundaryFrom(int _xTile, int _yTile, Direction _direction, TileType _tileType)
    {
        Tile tile = get(_xTile, _yTile);
        TileShape shape = tile.mRootId.mShape;
        switch (shape)
        {
            case eEmpty:
            {
                return false;
            }
            case eBlock:
            {
                return materialEdges.check(_tileType, tile.mRootId.mTileType);
            }
            case eSlope:
            {
                return materialEdges.check(_tileType, tile.mRootId.mTileType);
            }
            case eUndefined:
            {
                break;
            }
        }
        int slope = tile.mRootId.mSlopeType;
        switch (slope)
        {
            case 0:
            {
                if (_direction == Direction.eFromDown || _direction == Direction.eFromLeft)
                {
                    return materialEdges.check(_tileType, tile.mRootId.mTileType);
                }
                return false;
            }
            case 1:
            {
                if (_direction == Direction.eFromDown || _direction == Direction.eFromRight)
                {
                    return materialEdges.check(_tileType, tile.mRootId.mTileType);
                }
                return false;
            }
            case 2:
            {
                if (_direction == Direction.eFromUp || _direction == Direction.eFromRight)
                {
                    return materialEdges.check(_tileType, tile.mRootId.mTileType);
                }
                return false;
            }
            case 3:
            {
                if (_direction == Direction.eFromUp || _direction == Direction.eFromLeft)
                {
                    return materialEdges.check(_tileType, tile.mRootId.mTileType);
                }
                return false;
            }
        }
        return false;
    }
}
