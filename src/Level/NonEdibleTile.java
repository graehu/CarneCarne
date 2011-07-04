/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.Tile.Direction;
import Level.sLevel.TileType;
import World.sWorld.BodyCategories;
import java.util.Stack;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author alasdair
 */
class NonEdibleTile extends RootTile
{
    static final String constMaterial = "Meat";
    public NonEdibleTile(int _id, sLevel.TileType _tileType, boolean _anchor)
    {
        super(TileShape.eBlock, _id, _tileType, constMaterial, false, _anchor, false, 1);
    }
    @Override
    Fixture createPhysicsBody(int _xTile, int _yTile, Body _body, Tile _tile)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f, new Vec2((_xTile),(_yTile)), 0);
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.filter.groupIndex = mTileType.ordinal();
        fixture.filter.categoryBits = (1 << BodyCategories.eEdibleTiles.ordinal());
        fixture.filter.maskBits = Integer.MAX_VALUE;
        fixture.userData = _tile;
        if (mTileType.equals(TileType.eSpikes))
        {
            fixture.filter.categoryBits = (1 << BodyCategories.eSpikes.ordinal());
            fixture.filter.maskBits = Integer.MAX_VALUE;            
        }
        
        return _body.createFixture(fixture);
    }
    public Fixture createFixture(int _xTile, int _yTile)
    {
        return null;
    }
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        
    }

    boolean boundaryFrom(Direction _direction, TileType _tileType, MaterialEdges _materialEdges)
    {
        return _materialEdges.check(_tileType, mTileType);
    }
}
