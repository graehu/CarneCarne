/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Level.sLevel.TileType;
import World.sWorld;
import java.util.HashMap;
import java.util.Stack;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author alasdair
 */
class WaterTile extends RootTile {

    public WaterTile(int _rootId, TileType _tileType)
    {
        super(TileShape.eBlock, _rootId, _tileType, false);
    }

    Body createPhysicsBody(int _xTile, int _yTile)
    {
        HashMap parameters = new HashMap();
        parameters.put("position", new Vec2(_xTile,_yTile));
        parameters.put("TileType", mTileType);
        return sWorld.useFactory("WaterTileFactory",parameters);
    }
    public Fixture createFixture(int _xTile, int _yTile)
    {
        return null;
    }

    void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        
    }

    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        return _materialEdges.check(_tileType, mTileType);
    }
    
}
