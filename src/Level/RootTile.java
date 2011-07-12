/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Level.sLevel.TileType;
import World.sWorld.BodyCategories;
import java.util.Stack;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

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
        mRegrows = false;
        mAnchor = false;
        mIsFlammable = false;
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
    protected Fixture fixtureWithMaterial(Shape _shape, Body _body, Object _userData)
    {
        FixtureDef fixture = new FixtureDef();
        fixture.filter.categoryBits = (1 << BodyCategories.eEdibleTiles.ordinal());
        fixture.filter.maskBits = Integer.MAX_VALUE;
        if (mTileType.equals(TileType.eIce))
        {
            fixture.friction = 0.01f;
            fixture.filter.categoryBits = (1 << BodyCategories.eIce.ordinal());
            fixture.filter.maskBits = Integer.MAX_VALUE ^(1 << BodyCategories.eEtherealEnemy.ordinal());
        }
        else if (mTileType.equals(TileType.eBouncy))
        {
            fixture.restitution = 1.0f;
        }
        else if (mTileType.equals(TileType.eTar))
        {
            fixture.filter.categoryBits = (1 << BodyCategories.eTar.ordinal());
            fixture.restitution = 0.0f;
            fixture.friction = 10f;
        }
        fixture.shape = _shape;
        fixture.userData = _userData; /// FIXME make this body data instead
        if (_body.m_type.equals(BodyType.DYNAMIC))
        {
            fixture.density = 100.0f;
        }
        return _body.createFixture(fixture);
    }
}
