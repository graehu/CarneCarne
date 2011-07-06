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
public class RightDownSlope extends SlopeTile
{
    public RightDownSlope(int _id, sLevel.TileType _tileType, String _animationsName, int _maxHealth)
    {
        super(_id, 0, _tileType, _animationsName, _maxHealth);
    }
            
    public void getEdges(boolean _boundaries[], int _xTile, int _yTile, TileGrid _tileGrid)
    {
        _boundaries[0] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);
        _boundaries[1] = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight, mTileType);
    }

    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        if (_direction == Direction.eFromDown || _direction == Direction.eFromLeft)
        {
            return _materialEdges.check(_tileType, mTileType);
        }
        return false;
    }
}
