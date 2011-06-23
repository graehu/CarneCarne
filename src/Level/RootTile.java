/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Level.sLevel.TileType;
import java.util.Stack;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author alasdair
 */
abstract class RootTile
{
    TileShape mShape;
    int mId;
    int mSlopeType;
    boolean mRegrows;
    boolean mAnchor;
    sLevel.TileType mTileType;
    public RootTile(TileShape _shape, int _id, sLevel.TileType _tileType, boolean _anchor)
    {
        mShape = _shape;
        mId = _id;
        mTileType = _tileType;
        mRegrows = true;
        mAnchor = _anchor;
    }
    public RootTile(TileShape _shape, int _id, sLevel.TileType _tileType, int _slopeType)
    {
        mShape = _shape;
        mId = _id;
        mTileType = _tileType;
        mSlopeType = _slopeType;
        mRegrows = true;
    }

    abstract boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges);
    public enum TileShape
    {
        eEmpty,
        eBlock,
        eSlope,
        eWater,
        eUndefined,
        eTileIdMax
    }
    abstract Body createPhysicsBody(int _xTile, int _yTile);
    abstract Fixture createFixture(int _xTile, int _yTile);
    abstract void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid);
}
