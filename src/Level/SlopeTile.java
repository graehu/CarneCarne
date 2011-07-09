/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import World.sWorld.BodyCategories;
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
abstract public class SlopeTile extends RootTile{
    
    public SlopeTile(int _id, int _slopeType, sLevel.TileType _tileType, String _animationsName, int _maxHealth)
    {
        super (TileShape.eSlope, _id, _tileType, _animationsName, _slopeType, _maxHealth);
    }
    @Override
    Fixture createPhysicsBody(int _xTile, int _yTile, Body _body, Tile _tile)
    {
        PolygonShape shape = new PolygonShape();
        shape.m_vertexCount = 3;
        float hx = 0.5f;
        float hy = 0.5f;
        switch (mSlopeType)
        {
            case 0:
            {
                shape.m_vertices[0] = new Vec2(-hx, -hy);
                shape.m_vertices[1] = new Vec2( hx, hy);
                shape.m_vertices[2] = new Vec2(-hx, hy);
                shape.m_normals[0] = new Vec2(1.0f/1.414f, -1.0f/1.414f);
                shape.m_normals[1] = new Vec2(0.0f, 1.0f);
                shape.m_normals[2] = new Vec2(-1.0f, 0.0f);
                break;
            }
            case 1:
            {
                shape.m_vertices[0] = new Vec2( hx, -hy);
                shape.m_vertices[1] = new Vec2( hx, hy);
                shape.m_vertices[2] = new Vec2(-hx, hy);
                shape.m_normals[0] = new Vec2(1.0f, 0.0f);
                shape.m_normals[1] = new Vec2(0.0f, 1.0f);
                shape.m_normals[2] = new Vec2(-1.0f/1.414f, -1.0f/1.414f);
                break;
            }
            case 2:
            {
                shape.m_vertices[0] = new Vec2(-hx, -hy);
                shape.m_vertices[1] = new Vec2( hx, -hy);
                shape.m_vertices[2] = new Vec2( hx, hy);
                shape.m_normals[0] = new Vec2(0.0f, -1.0f);
                shape.m_normals[1] = new Vec2(1.0f, 0.0f);
                shape.m_normals[2] = new Vec2(-1.0f/1.414f, 1.0f/1.414f);
                break;
            }
            case 3:
            {
                shape.m_vertices[0] = new Vec2(-hx, -hy);
                shape.m_vertices[1] = new Vec2( hx, -hy);
                shape.m_vertices[2] = new Vec2(-hx, hy);
                shape.m_normals[0] = new Vec2(0.0f, -1.0f);
                shape.m_normals[1] = new Vec2(1.0f/1.414f, 1.0f/1.414f);
                shape.m_normals[2] = new Vec2(-1.0f, 0.0f);
                break;
            }
        }
        shape.m_centroid.x = _xTile;
        shape.m_centroid.y = _yTile;
        for (int i = 0; i < shape.m_vertexCount; ++i)
        {
            shape.m_vertices[i] = shape.m_vertices[i].add(shape.m_centroid);
        }
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        //fixture.filter.groupIndex = mTileType.ordinal();
        fixture.filter.categoryBits = (1 << BodyCategories.eEdibleTiles.ordinal());
        fixture.filter.maskBits = Integer.MAX_VALUE ^ (1 << BodyCategories.eEtherealEnemy.ordinal());
        if (_body.m_type.equals(BodyType.DYNAMIC))
        {
            fixture.density = 1.0f;
        }
        fixture.userData = _tile;
        return _body.createFixture(fixture);
    }
    public Fixture createFixture(int _xTile, int _yTile)
    {
        return null;
    }
    abstract public void getEdges(boolean _boundaries[], int _xTile, int _yTile, TileGrid _tileGrid);
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        boolean boundaries[] = new boolean[2];
        
        getEdges(boundaries, _xTile, _yTile, _tileGrid);

        int textureUnit = 0;
        for (int i = 0; i < 2; i++)
        {
            if (!boundaries[i])
                textureUnit |= (1 << i);
        }
        _stack.push(_xTile);
        _stack.push(_yTile);
        _stack.push(mId+textureUnit);
    }
}
