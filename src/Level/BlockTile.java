/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Physics.sPhysics;
import java.util.HashMap;
import java.util.Stack;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class BlockTile extends RootTile
{
    public BlockTile(int _id)
    {
        super(TileShape.eBlock, _id);
    }
    public void createPhysicsBody(int _xTile, int _yTile)
    {
        HashMap parameters = new HashMap();
        parameters.put("position", new Vec2(_xTile,_yTile));
        sPhysics.useFactory("TileFactory",parameters);
    }
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        boolean ULDR[] = new boolean[4];

        ULDR[3] = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown);//(id != 0);

        ULDR[2] = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight);

        ULDR[1] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp);

        ULDR[0] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft);

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
}
