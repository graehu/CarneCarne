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
    String mAnimationsName;
    RootTile mNext;
    public RootTile(TileShape _shape, int _id, sLevel.TileType _tileType, String _animationsName, boolean _regrows, boolean _anchor, boolean _isFlammable, int _maxHealth)
    {
        mShape = _shape;
        mId = _id;
        mTileType = _tileType;
        mAnimationsName = _animationsName;
        mRegrows = _regrows;
        mAnchor = _anchor;
        mNext = null;
        mIsFlammable = _isFlammable;
        mMaxHealth = _maxHealth;
    }
    public RootTile(TileShape _shape, int _id, sLevel.TileType _tileType, String _animationsName, int _slopeType, int _maxHealth)
    {
        mShape = _shape;
        mId = _id;
        mTileType = _tileType;
        mSlopeType = _slopeType;
        mAnimationsName = _animationsName;
        mRegrows = true;
        mMaxHealth = _maxHealth;
    }
    
    public RootTile getNext()
    {
        return mNext;
    }
    public void setNext(RootTile _next)
    {
        mNext = _next;
    }

    abstract boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges);
    public enum TileShape
    {
        eEmpty,
        eBlock,
        eSlope,
        eWater,
        eEdge,
        eUndefined,
        eTileIdMax
    }
    abstract Fixture createPhysicsBody(int _xTile, int _yTile, Body _body, Tile _tile);
    abstract void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid);
}
