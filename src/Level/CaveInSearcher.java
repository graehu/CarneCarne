/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Events.CaveInEvent;
import Events.sEvents;
import Level.Tile.Direction;
import Level.sLevel.TileType;
import java.util.ArrayList;
import java.util.Stack;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TileSet;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
public class CaveInSearcher {
    
    TileGrid mTileGrid;
    TiledMap mTiledMap;
    int mLayerIndex;
    enum Checked
    {
        eUnchecked,
        eAnchor,
        eNoAnchor,
        eCheckedMax,
    }
    Checked mChecked[][];
    int lowestX, lowestY, highestX, highestY;
    Vec2 mOffset;
    float mAngle;
    public CaveInSearcher(TileGrid _tileGrid, TiledMap _tiledMap, int _layerIndex, Vec2 _offset, float _angle)
    {
        mOffset = _offset;
        mAngle = _angle;
        lowestX = lowestY = Integer.MAX_VALUE;
        highestX = highestY = Integer.MIN_VALUE;
        mTileGrid = _tileGrid;
        mTiledMap = _tiledMap;
        mLayerIndex = _layerIndex;
        mChecked = new Checked[_tiledMap.getWidth()][_tiledMap.getHeight()];
        for (int i = 0; i < _tiledMap.getWidth(); i++)
            for (int ii = 0; ii < _tiledMap.getHeight(); ii++)
                mChecked[i][ii] = Checked.eUnchecked;
    }
    
    protected class TileIndex
    {
        int x, y;
        TileType mTileType;
        TileIndex(int _x, int _y, TileType _tileType)
        {
            x = _x;
            y = _y;
            mTileType = _tileType;
        }
    }
    public void destroy(int _x, int _y, TileType _tileType)
    {
        Stack<TileIndex> workingSetSets = new Stack<TileIndex>();
        Stack<TileIndex> gridSets = new Stack<TileIndex>();
        //mChecked[_x][_y] = true;
        check(_x-1,_y, Direction.eFromRight,_tileType,workingSetSets,gridSets);
        calculate(workingSetSets, gridSets);
        
        check(_x+1,_y, Direction.eFromLeft,_tileType,workingSetSets,gridSets);
        calculate(workingSetSets, gridSets);
        
        check(_x,_y-1, Direction.eFromDown,_tileType,workingSetSets,gridSets);
        calculate(workingSetSets, gridSets);
        
        check(_x,_y+1, Direction.eFromUp,_tileType,workingSetSets,gridSets);
        calculate(workingSetSets, gridSets);
    }
    private void calculate(Stack<TileIndex> workingSet, Stack<TileIndex> grid)
    {
        while (!workingSet.isEmpty())
        {
            TileIndex tile = workingSet.pop();
            if (check(tile.x-1,tile.y, Direction.eFromRight, tile.mTileType, workingSet,grid)
                    || check(tile.x+1,tile.y,Direction.eFromLeft, tile.mTileType,workingSet,grid)
                    || check(tile.x,tile.y-1,Direction.eFromDown, tile.mTileType,workingSet,grid)
                    || check(tile.x,tile.y+1,Direction.eFromUp, tile.mTileType,workingSet,grid))
            {
                while (!grid.isEmpty())
                {
                    TileIndex anchorTile = grid.pop();
                    mChecked[anchorTile.x][anchorTile.y] = Checked.eAnchor;
                }
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
    
    public boolean check(int _x, int _y, Direction _direction, TileType _tileType, Stack<TileIndex> _workingSet, Stack<TileIndex> _thisBlock) /// Returns true if this is an anchor
    {
        Tile tile;
        try
        {
            tile = mTileGrid.get(_x, _y);
        }
        catch (ArrayIndexOutOfBoundsException e) /// FIXME algorithm shouldn't reach here
        {
            return false;
        }
        if ((tile.mRootId.mAnchor || mChecked[_x][_y].equals(Checked.eAnchor)))
        {
            if (tile.boundaryFrom(_direction, _tileType, MaterialEdges.AnchorEdges))
                return true;
        }
        if (!mChecked[_x][_y].equals(Checked.eNoAnchor))
        {
            mChecked[_x][_y] = Checked.eNoAnchor;
            if (tile.mFixture != null && tile.boundaryFrom(_direction, _tileType, MaterialEdges.AnchorEdges))
            {
                _workingSet.add(new TileIndex(_x, _y, tile.getTileType()));
                _thisBlock.add(new TileIndex(_x, _y, tile.getTileType()));
            }
        }
        return false;
    }
    public class TempTile
    {
        public int x, y;
        private int id;
        public Image image;
        public TempTile(int _x, int _y, int _id)
        {
            x = _x;
            y = _y;
            id = _id;
            TileSet tileSet = mTiledMap.findTileSet(id);
            int idDiff = id - tileSet.firstGID;
            image = tileSet.tiles.getSubImage(idDiff%tileSet.tilesAcross, idDiff/tileSet.tilesAcross);
        }
        public int getId()
        {
            return id;
        }
        public void setId(int _id)
        {
            id = _id;
            if (id == 0)
            {
                image = null;
            }
            else
            {
                TileSet tileSet = mTiledMap.findTileSet(id);
                int idDiff = id - tileSet.firstGID;
                image = tileSet.tiles.getSubImage(idDiff%tileSet.tilesAcross, idDiff/tileSet.tilesAcross);
            }
        }
    }
    ArrayList<TempTile> tiles = new ArrayList<TempTile>();
    Body mLastBody = null;
    private void add(int _x, int _y)
    {
        int id = mTileGrid.getTileId(_x, _y);
        if (id != 0)
        {
            if (_x < lowestX)
                lowestX = _x;
            if (_x > highestX)
                highestX = _x;
            if (_y < lowestY)
                lowestY = _y;
            if (_y > highestY)
                highestY = _y;
            tiles.add(new TempTile(_x,_y, id));
            //mTileGrid.get(_x, _y).destroyFixture();
            mTileGrid.mBody.destroyFixture(mTileGrid.get(_x, _y).mFixture);
            mTileGrid.set(_x, _y, 0);
        }
    }
    private void finish()
    {
        if (!tiles.isEmpty())
        {
            /// Individual tiles
            /// One tile
            CaveInTileGrid newTileGrid = new CaveInTileGrid(mTileGrid.rootTiles, mTiledMap, lowestX, lowestY, 1+highestX-lowestX, 1+highestY-lowestY, mLayerIndex, mOffset, mAngle);
            sEvents.triggerDelayedEvent(new CaveInEvent(newTileGrid, tiles.size()));
            newTileGrid.finish(tiles);
            tiles = new ArrayList<TempTile>();
            lowestX = lowestY = Integer.MAX_VALUE;
            highestX = highestY = Integer.MIN_VALUE;
        }
    }
}
