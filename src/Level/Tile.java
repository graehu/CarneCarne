/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Graphics.Particles.sParticleManager;
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
public class Tile
{
    RootTile mRootId;
    int mId;
    Fixture mFixture;
    int mHealth; /// Aliases as water height
    TileGrid mTileGrid;
    int mXTile,mYTile;
    boolean isOnFire;
    public Tile(int _id, RootTile _rootId, TileGrid _tileGrid, int _xTile, int _yTile)
    {
        mId = _id;
        mRootId = _rootId;
        if (mRootId != null)
            mHealth = mRootId.mMaxHealth;
        else
            mHealth = -1;
        mTileGrid = _tileGrid;
        mXTile = _xTile;
        mYTile = _yTile;
        isOnFire = false;
    }
    public TileType getTileType()
    {
        return mRootId.mTileType;
    }
    public String getAnimationsName()
    {
        return mRootId.mAnimationsName;
    }
    public void setOnFire()
    {
        /*try
        {
            if (mRootId.mAnimationsName != null)
                sParticleManager.createSystem(mRootId.mAnimationsName + "FireHit", mTileGrid.mBody.getWorldPoint(new Vec2(mXTile,mYTile)).mul(64.0f), 120);
        }
        catch (NullPointerException e) /// FIXME lol jk this is working PERFECTLY
        {
            /// Booo
        }*/
        if (!isOnFire)
        {
            isOnFire = true;
            if (mRootId.mIsFlammable)
            {
                try /// FIXME not sure why this is happening, possibly to do with cave ins, Tiles shouldn't be transfered in cave ins without changing coordinates
                {
                    mTileGrid.placeTileNoBody(mXTile, mYTile, mId + 16);
                    mTileGrid.mTileFire.addTile(this);
                }
                catch (ArrayIndexOutOfBoundsException e)
                {

                }
            }
            else if (mRootId.mTileType.equals(TileType.eIce))
            {
                destroyFixture();
            }
        }
    }
    public boolean damageTile()
    {
        if (mHealth > 1)
        {
            mRootId = mRootId.getNext();
            //mId += 16;
            mHealth--;
            //mRootId = rootTiles.get(tile.mId);
            Stack<Integer> stack = new Stack<Integer>();
            mTileGrid.mTiles[mXTile][mYTile].checkEdges(stack, mTileGrid);
            sParticleManager.createSystem(mRootId.mAnimationsName + "DamageParticle", mTileGrid.mBody.getWorldPoint(new Vec2(mXTile,mYTile)).mul(64.0f), 120);
            while (!stack.empty())
            {
                int id = stack.pop();
                int yTile = stack.pop();
                int xTile = stack.pop();
                mTileGrid.setTileId(xTile, yTile, id);
            }
            return false;
        }
        return true;
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
        return new Tile(mId, mRootId, mTileGrid,-1,-1);
    }

    public boolean isOnFire()
    {
        return isOnFire;
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
        mFixture = null;
        mTileGrid.destroyTile(mXTile, mYTile);
    }
    public void createPhysicsBody(HashMap _parameters, Body _body)
    {
        _parameters.put("Tile", this);
        mFixture = mRootId.createPhysicsBody(mXTile, mYTile, _body, this);
    }
    public void checkEdges(Stack<Integer> _stack, TileGrid _tileGrid)
    {
        mRootId.checkEdges(mXTile, mYTile, _stack, _tileGrid);
    }
    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        return mRootId.boundaryFrom(_direction, _tileType, _materialEdges);
    }
}
