/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Level.sLevel.TileType;
import java.util.Stack;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
public class PlatformCaveInSearcher extends CaveInSearcher
{
    public PlatformCaveInSearcher(TileGrid _tileGrid, TiledMap _tiledMap, int _layerIndex, Body _body)
    {
        super(_tileGrid, _tiledMap, _layerIndex, _body);
    }
    @Override
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
        if (!mChecked[_x][_y].equals(Checked.eNoAnchor))
        {
            if (tile.mFixture != null && tile.boundaryFrom(_direction, _tileType, MaterialEdges.AnchorEdges))
            {
                mChecked[_x][_y] = Checked.eNoAnchor;
                _workingSet.add(new TileIndex(_x, _y, tile.getTileType()));
                _thisBlock.add(new TileIndex(_x, _y, tile.getTileType()));
            }
        }
        return false;
    }
    @Override
    protected CaveInTileGrid createTileGrid(RootTileList _rootTiles, TiledMap _tiledMap, int _xTrans, int _yTrans, int _width, int _height, int _layerIndex, Vec2 _position, float _angle, Vec2 _linearVelocity, float _angularVelocity)
    {
        return new PlatformTileGrid(_rootTiles, _tiledMap, _xTrans, _yTrans, _width, _height, _layerIndex, _position, _angle, _linearVelocity, _angularVelocity);
    }
    
    public Body getCreatedBody()
    {
        return mCreatedBody;
    }
}
