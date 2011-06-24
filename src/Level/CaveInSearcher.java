/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Entities.CaveIn;
import Entities.sEntityFactory;
import World.sWorld;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
class CaveInSearcher {
    
    TileGrid mTileGrid;
    TiledMap mTiledMap;
    int mLayerIndex;
    private boolean mChecked[][];
    public CaveInSearcher(TileGrid _tileGrid, TiledMap _tiledMap, int _layerIndex)
    {
        mTileGrid = _tileGrid;
        mTiledMap = _tiledMap;
        mLayerIndex = _layerIndex;
        mChecked = new boolean[_tiledMap.getWidth()][_tiledMap.getHeight()];
        for (int i = 0; i < _tiledMap.getWidth(); i++)
            for (int ii = 0; ii < _tiledMap.getHeight(); ii++)
                mChecked[i][ii] = false;
    }
    
    private class TileIndex
    {
        int x, y;
        TileIndex(int _x, int _y)
        {
            x = _x;
            y = _y;
        }
    }
    public void destroy(int _x, int _y)
    {
        Stack<TileIndex> workingSetSets = new Stack<TileIndex>();
        Stack<TileIndex> gridSets = new Stack<TileIndex>();
        
        check(_x-1,_y,workingSetSets,gridSets);
        calculate(workingSetSets, gridSets);
        
        check(_x+1,_y,workingSetSets,gridSets);
        calculate(workingSetSets, gridSets);
        
        check(_x,_y-1,workingSetSets,gridSets);
        calculate(workingSetSets, gridSets);
        
        check(_x,_y+1,workingSetSets,gridSets);
        calculate(workingSetSets, gridSets);
    }
    private void calculate(Stack<TileIndex> workingSet, Stack<TileIndex> grid)
    {
        while (!workingSet.isEmpty())
        {
            TileIndex tile = workingSet.pop();
            if (check(tile.x-1,tile.y,workingSet,grid)
                    || check(tile.x+1,tile.y,workingSet,grid)
                    || check(tile.x,tile.y-1,workingSet,grid)
                    || check(tile.x,tile.y+1,workingSet,grid))
            {
                grid.clear();
                workingSet.clear();
                break;
            }
        }
        while (!grid.isEmpty())
        {
            TileIndex tile = grid.pop();
            add(tile.x,tile.y);
        }
        finish();
        grid.clear();
    }
    
    public boolean check(int _x, int _y, Stack<TileIndex> _workingSet, Stack<TileIndex> _thisBlock) /// Returns true if this is an anchor
    {
        Tile tile = mTileGrid.get(_x, _y);
        if (tile.mRootId.mAnchor)
        {
            return true;
        }
        if (!mChecked[_x][_y])
        {
            mChecked[_x][_y] = true;
            if (tile.mId != 0)
            {
                _workingSet.add(new TileIndex(_x, _y));
                _thisBlock.add(new TileIndex(_x, _y));
            }
        }
        return false;
    }
    
    ArrayList<CaveIn.Tile> tiles = new ArrayList<CaveIn.Tile>();
    Body mLastBody = null;
    private void add(int _x, int _y)
    {
        Image image = mTiledMap.getTileImage(_x, _y, mLayerIndex);
        HashMap parameters = new HashMap();
        parameters.put("isDynamic", true);
        parameters.put("img", image) ;
        
        /// Individual tiles
        /*Body body = mTileGrid.get(_x, _y).mRootId.createPhysicsBody(_x, _y, parameters);
        if (mLastBody != null)
        {
            sWorld.weld(body, mLastBody);
        }
        mLastBody = body;
        parameters.put("Body", body);
        sEntityFactory.create("CaveInTileFactory", parameters);
        tiles.add(new CaveIn.Tile(image, body, new Vec2(_x,_y)));*/
        /// One tile
        tiles.add(new CaveIn.Tile(image, null, new Vec2(_x,_y)));
        
        sWorld.destroyBody(mTileGrid.get(_x, _y).mBody);
        mTileGrid.set(_x, _y, 0);
    }
    private void finish()
    {
        if (!tiles.isEmpty())
        {
            /// Individual tiles
            /*CaveIn.Tile oldTile = null;
            for (CaveIn.Tile tile: tiles)
            {
                if (oldTile != null)
                {
                    sWorld.weld(tile.mBody, oldTile.mBody);
                }
                oldTile = tile;
            }*/
            /// One tile
            HashMap<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("tiles", tiles);
            sEntityFactory.create("CaveIn", parameters);
            tiles = new ArrayList<CaveIn.Tile>();
        }
    }
}
