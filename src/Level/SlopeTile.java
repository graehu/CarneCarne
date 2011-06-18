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
abstract public class SlopeTile extends RootTile{
    
    public SlopeTile(int _id, int _slopeType, sLevel.TileType _tileType)
    {
        super (TileShape.eSlope, _id, _tileType, _slopeType);
    }
    public void createPhysicsBody(int _xTile, int _yTile)
    {
        HashMap parameters = new HashMap();
        parameters.put("position", new Vec2(_xTile,_yTile));
        parameters.put("TileType", mTileType);
        parameters.put("slopeType",mSlopeType);
        sWorld.useFactory("SlopeFactory",parameters);
    }
    abstract public void getEdges(boolean _boundaries[], int _xTile, int _yTile, TileGrid _tileGrid);
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        boolean boundaries[] = new boolean[2];
        
        getEdges(boundaries, _xTile, _yTile, _tileGrid);

        int textureUnit = 0;
        for (int i = 0; i < 2; i++)
        {
            if (!boundaries[i])
                textureUnit |= (1 << i);
        }
        _stack.push(_xTile);
        _stack.push(_yTile);
        _stack.push(mId+textureUnit);
    }
}
