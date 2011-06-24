/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Level.sLevel.TileType;
import java.util.HashMap;
import java.util.Stack;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author alasdair
 */
public class EmptyTile extends RootTile{
    
    public EmptyTile()
    {
        super(TileShape.eEmpty,0, sLevel.TileType.eEmpty, false, 0);
    }
    public Body createPhysicsBody(int _xTile, int _yTile, HashMap _parameters)
    {
        return null;
    }
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        
    }

    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        return false;
    }
    public Fixture createFixture(int _xTile, int _yTile)
    {
        return null;
    }
}
