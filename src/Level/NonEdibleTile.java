/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import World.sWorld;
import java.util.HashMap;
import java.util.Stack;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class NonEdibleTile extends RootTile
{
    public NonEdibleTile(int _id, sLevel.TileType _tileType)
    {
        super(TileShape.eBlock, _id, _tileType);
    }
    public void createPhysicsBody(int _xTile, int _yTile)
    {
        HashMap parameters = new HashMap();
        parameters.put("position", new Vec2(_xTile,_yTile));
        parameters.put("TileType", mTileType);
        sWorld.useFactory("NonEdibleTileFactory",parameters);
    }
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        
    }
}
