/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Level.sLevel.TileType;
import World.sWorld;
import World.sWorld.BodyCategories;
import java.util.HashMap;
import java.util.Stack;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author alasdair
 */
public class BlockTile extends RootTile
{
    public BlockTile(int _id, sLevel.TileType _tileType, boolean _regrows, boolean _anchor, int _maxHealth)
    {
        super(TileShape.eBlock, _id, _tileType, _regrows, _anchor, _maxHealth);
        mRegrows = _regrows;
    }
    public Fixture createPhysicsBody(int _xTile, int _yTile, Body _body, Tile _tile)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f, new Vec2(_xTile,_yTile), 0);
        FixtureDef fixture = new FixtureDef();
        fixture.filter.categoryBits = (1 << BodyCategories.eEdibleTiles.ordinal());
        fixture.filter.maskBits = Integer.MAX_VALUE;
        if (mTileType.equals(TileType.eIce))
        {
            fixture.friction = 0.001f;
            fixture.filter.categoryBits = (1 << BodyCategories.eIce.ordinal());
            fixture.filter.maskBits = Integer.MAX_VALUE;
        }
        else if (mTileType.equals(TileType.eBouncy))
        {
            fixture.restitution = 2.0f;
        }
        else if (mTileType.equals(TileType.eTar))
        {
            fixture.filter.categoryBits = (1 << BodyCategories.eTar.ordinal());
            fixture.restitution = 0.0f;
            fixture.friction = 1000.0f;
        }
        else if (mTileType.equals(TileType.eSpikes))
        {
            fixture.filter.categoryBits = (1 << BodyCategories.eSpikes.ordinal());
            fixture.filter.maskBits = Integer.MAX_VALUE;            
        }
        fixture.shape = shape;
        fixture.userData = _tile; /// FIXME make this body data instead
        if (_body.m_type.equals(BodyType.DYNAMIC))
        {
            fixture.density = 1.0f;
        }
        return _body.createFixture(fixture);
    }
    public Fixture createFixture(int _xTile, int _yTile)
    {
        HashMap parameters = new HashMap();
        parameters.put("position", new Vec2(_xTile,_yTile));
        parameters.put("TileType", mTileType);
        parameters.put("isDynamic", true);
        sWorld.useFactory("TileFactory",parameters);
        return null;
    }
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        boolean ULDR[] = new boolean[4];
        
        try
        {
            ULDR[3] = _tileGrid.boundaryFrom(_xTile, _yTile-1, Direction.eFromDown, mTileType);//(id != 0);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }

        try
        {
            ULDR[2] = _tileGrid.boundaryFrom(_xTile-1, _yTile, Direction.eFromRight, mTileType);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }

        try
        {
            ULDR[1] = _tileGrid.boundaryFrom(_xTile, _yTile+1, Direction.eFromUp, mTileType);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }

        try
        {
            ULDR[0] = _tileGrid.boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft, mTileType);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }

        int textureUnit = 0;
        for (int i = 0; i < 4; i++)
        {
            if (!ULDR[i])
                textureUnit |= (1 << i);
        }
        _stack.push(_xTile);
        _stack.push(_yTile);
        _stack.push(mId+textureUnit);
    }
    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        return _materialEdges.check(_tileType, mTileType);
    }
}
