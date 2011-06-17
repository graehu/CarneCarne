/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import java.util.Stack;

/**
 *
 * @author alasdair
 */
abstract public class RootTile
{
    TileShape mShape;
    int mId;
    int mSlopeType;
    public RootTile(TileShape _shape, int _id)
    {
        mShape = _shape;
        mId = _id;
    }
    public RootTile(TileShape _shape, int _id, int _slopeType)
    {
        mShape = _shape;
        mId = _id;
        mSlopeType = _slopeType;
    }
    public enum TileShape
    {
        eEmpty,
        eBlock,
        eSlope,
        eUndefined,
        eTileIdMax
    }
    abstract public void createPhysicsBody(int _xTile, int _yTile);
    abstract public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid);
}
