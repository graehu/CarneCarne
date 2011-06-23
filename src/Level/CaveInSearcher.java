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
        Stack<Stack<TileIndex>> workingSetSets = new Stack<Stack<TileIndex>>();
        Stack<TileIndex> thisBlock = new Stack<TileIndex>();
        workingSetSets.add(new Stack<TileIndex>());
        check(_x-1,_y,workingSetSets.peek(),thisBlock);
        check(_x+1,_y,workingSetSets.peek(),thisBlock);
        check(_x,_y-1,workingSetSets.peek(),thisBlock);
        check(_x,_y+1,workingSetSets.peek(),thisBlock);
        //workingSetSets.peek().add(new TileIndex(_x, _y));
        while (!workingSetSets.isEmpty())
        {
            if (workingSetSets.peek().isEmpty())
            {
                workingSetSets.pop();
                while (!thisBlock.isEmpty())
                {
                    TileIndex tile = thisBlock.pop();
                    add(tile.x,tile.y);
                }
                continue;
            }
            TileIndex tile = workingSetSets.peek().pop();
            boolean anchor = check(tile.x-1,tile.y,workingSetSets.peek(),thisBlock);
            anchor = anchor || check(tile.x+1,tile.y,workingSetSets.peek(),thisBlock);
            anchor = anchor || check(tile.x,tile.y-1,workingSetSets.peek(),thisBlock);
            anchor = anchor || check(tile.x,tile.y+1,workingSetSets.peek(),thisBlock);
            if (anchor)
            {
                thisBlock.clear();
                workingSetSets.pop();
            }
        }
        finish();
    }
    
    public boolean check(int _x, int _y, Stack<TileIndex> _workingSet, Stack<TileIndex> _thisBlock) /// Returns true if this is an anchor
    {
        
        if (!mChecked[_x][_y])
        {
            mChecked[_x][_y] = true;
            Tile tile = mTileGrid.get(_x, _y);
            if (tile.mRootId.mAnchor)
            {
                return true;
            }
            if (tile.mId != 0)
            {
                _workingSet.add(new TileIndex(_x, _y));
                _thisBlock.add(new TileIndex(_x, _y));
            }
        }
        return false;
    }
    
    ArrayList<CaveIn.Tile> tiles = new ArrayList<CaveIn.Tile>();
    private void add(int _x, int _y)
    {
        Image image = mTiledMap.getTileImage(_x, _y, mLayerIndex);
        tiles.add(new CaveIn.Tile(image,new Vec2(_x,_y)));
        sWorld.destroyBody(mTileGrid.get(_x, _y).mBody);
        mTileGrid.set(_x, _y, 0);
        /*tiles.add(new CaveIn.Tile(image,new Vec2(_x+1,_y)));
        tiles.add(new CaveIn.Tile(image,new Vec2(_x+1,_y+1)));*/
    }
    private void finish()
    {
        if (!tiles.isEmpty())
        {
            HashMap<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("tiles", tiles);
            sEntityFactory.create("CaveIn", parameters);        
        }
    }
}
