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
public class BlockTile extends RootTile
{
    public BlockTile(int _id, sLevel.TileType _tileType, boolean _regrows, boolean _anchor, int _maxHealth)
    {
        super(TileShape.eBlock, _id, _tileType, _anchor, _maxHealth);
        mRegrows = _regrows;
    }
    public Body createPhysicsBody(int _xTile, int _yTile, HashMap _parameters)
    {
        _parameters.put("position", new Vec2(_xTile,_yTile));
        _parameters.put("TileType", mTileType);
        return sWorld.useFactory("TileFactory",_parameters);
    }
    public Fixture createFixture(int _xTile, int _yTile)
    {
        HashMap parameters = new HashMap();
        parameters.put("position", new Vec2(_xTile,_yTile));
        parameters.put("TileType", mTileType);
        parameters.put("isDynamic", true);
        sWorld.useFactory("TileFactory",parameters);
        return null;
    }
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        boolean ULDR[] = new boolean[4];

        ULDR[3] = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown, mTileType);//(id != 0);

        ULDR[2] = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight, mTileType);

        ULDR[1] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);

        ULDR[0] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft, mTileType);

        int textureUnit = 0;
        for (int i = 0; i < 4; i++)
        {
            if (!ULDR[i])
                textureUnit |= (1 << i);
        }
        _stack.push(_xTile);
        _stack.push(_yTile);
        _stack.push(mId+textureUnit);
    }
    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        return _materialEdges.check(_tileType, mTileType);
    }
}
