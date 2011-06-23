/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import java.util.Stack;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author alasdair
 */
public class Tile {

    RootTile mRootId;
    int mId;
    Body mBody;
    public Tile(int _id, RootTile _rootId)
    {
        mId = _id;
        mRootId = _rootId;
    }
    enum Direction
    {
        eFromUp,
        eFromLeft,
        eFromRight,
        eFromDown,
        eDirectionsMax
    }
    public void createPhysicsBody(int _xTile, int _yTile)
    {
        mBody = mRootId.createPhysicsBody(_xTile, _yTile);
    }
    public Fixture createFixture(int _xTile, int _yTile)
    {
        return mRootId.createFixture(_xTile, _yTile);
    }
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        mRootId.checkEdges(_xTile, _yTile, _stack, _tileGrid);
    }
}
