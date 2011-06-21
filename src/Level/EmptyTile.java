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
public class EmptyTile extends RootTile{
    
    public EmptyTile()
    {
        super(TileShape.eEmpty,0, sLevel.TileType.eTileTypesMax);
    }
    public void createPhysicsBody(int _xTile, int _yTile)
    {
        
    }
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        
    }

    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        return false;
    }
}
