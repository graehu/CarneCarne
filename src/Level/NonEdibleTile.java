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
class NonEdibleTile extends RootTile
{
    public NonEdibleTile(int _id, sLevel.TileType _tileType)
    {
        super(TileShape.eBlock, _id, _tileType, true);
    }
    public Body createPhysicsBody(int _xTile, int _yTile)
    {
        HashMap parameters = new HashMap();
        parameters.put("position", new Vec2(_xTile,_yTile));
        parameters.put("TileType", mTileType);
        return sWorld.useFactory("NonEdibleTileFactory",parameters);
    }
    public Fixture createFixture(int _xTile, int _yTile)
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
}
