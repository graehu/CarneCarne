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
abstract class RootTile
{
    TileShape mShape;
    int mId;
    int mSlopeType;
    sLevel.TileType mTileType;
    public RootTile(TileShape _shape, int _id, sLevel.TileType _tileType)
    {
        mShape = _shape;
        mId = _id;
        mTileType = _tileType;
    }
    public RootTile(TileShape _shape, int _id, sLevel.TileType _tileType, int _slopeType)
    {
        mShape = _shape;
        mId = _id;
        mTileType = _tileType;
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
    abstract void createPhysicsBody(int _xTile, int _yTile);
    abstract void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid);
}
