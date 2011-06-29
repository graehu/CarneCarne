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
    int mMaxHealth;
    boolean mIsFlammable;
    public RootTile(TileShape _shape, int _id, sLevel.TileType _tileType, boolean _regrows, boolean _anchor, boolean _isFlammable, int _maxHealth)
    {
        mShape = _shape;
        mId = _id;
        mTileType = _tileType;
        mRegrows = _regrows;
        mAnchor = _anchor;
        mIsFlammable = _isFlammable;
        mMaxHealth = _maxHealth;
    }
    public RootTile(TileShape _shape, int _id, sLevel.TileType _tileType, int _slopeType, int _maxHealth)
    {
        mShape = _shape;
        mId = _id;
        mTileType = _tileType;
        mSlopeType = _slopeType;
        mRegrows = true;
        mMaxHealth = _maxHealth;
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
    abstract Fixture createPhysicsBody(int _xTile, int _yTile, Body _body, Tile _tile);
    abstract void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid);
}
