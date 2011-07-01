/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import java.util.Stack;

/**
 *
 * @author alasdair
 */
public class LineTile extends BlockTile
{
    Direction mDirection;
    LineTile(int _id, sLevel.TileType _tileType, Direction _direction, boolean _regrows, boolean _anchor, boolean _isFlammable, int _maxHealth)
    {
        super(_id, _tileType, _regrows, _anchor, _isFlammable, _maxHealth, TileShape.eEdge);
        mDirection = _direction;
    }
    
    public @Override
    void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        boolean edges[] = new boolean[2];
        edges[1] = true;
        switch (mDirection)
        {
            case eFromLeft:
            {
                edges[0] = _tileGrid.boundaryFrom(_xTile+1, _yTile, mDirection, mTileType);
                edges[1] = _tileGrid.boundaryFrom(_xTile-1, _yTile, mDirection, mTileType);
                break;
            }
            case eFromRight:
            {
                edges[0] = _tileGrid.boundaryFrom(_xTile-1, _yTile, mDirection, mTileType);
                edges[1] = _tileGrid.boundaryFrom(_xTile+1, _yTile, mDirection, mTileType);
                break;
            }
            case eFromDown:
            {
                edges[0] = _tileGrid.boundaryFrom(_xTile, _yTile-1, mDirection, mTileType);
                edges[1] = _tileGrid.boundaryFrom(_xTile, _yTile+1, mDirection, mTileType);
                break;
            }
            case eFromUp:
            {
                edges[0] = _tileGrid.boundaryFrom(_xTile, _yTile+1, mDirection, mTileType);
                edges[1] = _tileGrid.boundaryFrom(_xTile, _yTile-1, mDirection, mTileType);
                break;
            }
            default:
                assert(false);
        }
        int textureUnit = 0;
        for (int i = 0; i < 2; i++)
        {
            if (!edges[i])
                textureUnit |= (1 << i);
        }
        _stack.push(_xTile);
        _stack.push(_yTile);
        _stack.push(mId+textureUnit);
    }
}
