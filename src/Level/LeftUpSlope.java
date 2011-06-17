/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;

/**
 *
 * @author alasdair
 */
public class LeftUpSlope extends SlopeTile
{
    public LeftUpSlope(int _id)
    {
        super(_id, 2);
    }

    public void getEdges(boolean _boundaries[], int _xTile, int _yTile, TileGrid _tileGrid)
    {
        _boundaries[0] = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown);
        _boundaries[1] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft);
    }
}
