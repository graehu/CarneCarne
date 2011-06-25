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
public class RightUpSlope extends SlopeTile
{
    public RightUpSlope(int _id, sLevel.TileType _tileType, int _maxHealth)
    {
        super(_id, 3, _tileType, _maxHealth);
    }

    public void getEdges(boolean _boundaries[], int _xTile, int _yTile, TileGrid _tileGrid)
    {
        _boundaries[0] = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown, mTileType);
        _boundaries[1] = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight, mTileType);
    }

    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        if (_direction == Direction.eFromUp || _direction == Direction.eFromLeft)
        {
            return _materialEdges.check(_tileType, mTileType);
        }
        return false;
    }
}
