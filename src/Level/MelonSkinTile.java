/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Level.sLevel.TileType;
import java.util.Stack;

/**
 *
 * @author alasdair
 */
class MelonSkinTile extends BlockTile {

    public enum SkinDirection
    {
        eUp,
        eRight,
        eDown,
        eLeft,
        eSkinDirectionsSize,
    }
    SkinDirection mDirection;
    public MelonSkinTile(int _id, TileType type, SkinDirection _direction)
    {
        super(_id, type, false);
        mDirection = _direction;
    }
    
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        boolean next = false;
        switch (mDirection)
        {
            case eUp:
            {
                next = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown, mTileType);
                break;
            }
            case eRight:
            {
                next = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft, mTileType);
                break;
            }
            case eDown:
            {
                next = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);
                break;
            }
            case eLeft:
            {
                next = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight, mTileType);
                break;
            }
        }
        if (!next)
        {
            _stack.push(_xTile);
            _stack.push(_yTile);
            _stack.push(mId+16);
        }
    }
    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        switch (mDirection)
        {
            case eUp:
            {
                return !_direction.equals(Direction.eFromDown);
            }
            case eRight:
            {
                return !_direction.equals(Direction.eFromLeft);
            }
            case eDown:
            {
                return !_direction.equals(Direction.eFromUp);
            }
            case eLeft:
            {
                return !_direction.equals(Direction.eFromRight);
            }
        }
        return false;
    }
}
