/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.sLevel.TileType;
import java.util.HashMap;
import java.util.Stack;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author alasdair
 */
public class Tile {

    RootTile mRootId;
    int mId;
    Fixture mFixture;
    int mHealth; /// Aliases as water height
    TileGrid mTileGrid;
    int mXTile,mYTile;
    public Tile(int _id, RootTile _rootId, TileGrid _tileGrid, int _xTile, int _yTile)
    {
        mId = _id;
        mRootId = _rootId;
        mHealth = mRootId.mMaxHealth;
        mTileGrid = _tileGrid;
        mXTile = _xTile;
        mYTile = _yTile;
    }
    public TileType getTileType()
    {
        return mRootId.mTileType;
    }
    public TileGrid getTileGrid()
    {
        return mTileGrid;
    }
    public Vec2 getPosition()
    {
        return new Vec2(mXTile, mYTile);
    }
    public int getRootId()
    {
        return mRootId.mId;
    }
    public Tile clone()
    {
        return new Tile(mId, mRootId, null,-1,-1);
    }
    enum Direction
    {
        eFromUp,
        eFromLeft,
        eFromRight,
        eFromDown,
        eDirectionsMax
    }
    public int getWaterHeight()
    {
        return mHealth;
    }
    public void setWaterHeight(int _height)
    {
        mHealth = _height;
    }
    public void createPhysicsBody(Body _body)
    {
        HashMap parameters = new HashMap();
        parameters.put("Tile", this);
        mFixture = mRootId.createPhysicsBody(mXTile, mYTile, _body, this);
    }
    public void destroyFixture()
    {
        mTileGrid.mBody.destroyFixture(mFixture);
        mTileGrid.destroyTile(mXTile, mYTile);
    }
    public void createPhysicsBody(int _xTile, int _yTile, HashMap _parameters, Body _body)
    {
        _parameters.put("Tile", this);
        mFixture = mRootId.createPhysicsBody(_xTile, _yTile, _body, this);
    }
    public Fixture createFixture()
    {
        return mRootId.createFixture(mXTile, mYTile);
    }
    public void checkEdges(Stack<Integer> _stack, TileGrid _tileGrid)
    {
        mRootId.checkEdges(mXTile, mYTile, _stack, _tileGrid);
    }
}
