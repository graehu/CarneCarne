/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.sLevel.TileType;
import java.util.Stack;

/**
 *
 * @author alasdair
 */
public class Tile {

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
        mRootId.createPhysicsBody(_xTile, _yTile);
    }
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        mRootId.checkEdges(_xTile, _yTile, _stack, _tileGrid);
    }
    RootTile mRootId;
    int mId;
}
