/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Level.sLevel.TileType;

/**
 *
 * @author alasdair
 */
public class LeftDownSlope extends SlopeTile
{
    public LeftDownSlope(int _id, sLevel.TileType _tileType, String _animationsNames[], boolean _regrows, boolean _anchor, boolean _isFlammable, int _maxHealth)
    {
        super(_id, 1, _tileType, _animationsNames, _regrows, _anchor, _isFlammable, _maxHealth);
    }
    
    public void getEdges(boolean _boundaries[], int _xTile, int _yTile, TileGrid _tileGrid)
    {
        _boundaries[0] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);
        _boundaries[1] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft, mTileType);
    }

    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        if (_direction == Direction.eFromDown || _direction == Direction.eFromRight)
        {
            return _materialEdges.check(_tileType, mTileType);
        }
        return false;
    }
}
