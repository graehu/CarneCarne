/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Level.Tile;
import Level.sLevel;
import Level.sLevel.TileType;
import World.sWorld.BodyCategories;
import java.util.HashMap;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author alasdair
 */
public class TileFactory implements iPhysicsFactory {
    
    public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        Object tile = _parameters.get("Tile");
        boolean dynamic;
        try
        {
            dynamic = ((Boolean)_parameters.get("isDynamic")).booleanValue();
        }
        catch (NullPointerException e)
        {
            dynamic = false;
        }
        sLevel.TileType tileType = ((Tile)tile).getTileType();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        FixtureDef fixture = new FixtureDef();
        fixture.filter.categoryBits = (1 << BodyCategories.eEdibleTiles.ordinal());
        fixture.filter.maskBits = Integer.MAX_VALUE;
        if (tileType.equals(TileType.eIce))
        {
            fixture.friction = 0.001f;
            fixture.filter.categoryBits = (1 << BodyCategories.eIce.ordinal());
            fixture.filter.maskBits = Integer.MAX_VALUE;
        }
        else if (tileType.equals(TileType.eBouncy))
        {
            fixture.restitution = 2.0f;
        }
        else if (tileType.equals(TileType.eTar))
        {
            fixture.filter.categoryBits = (1 << BodyCategories.eTar.ordinal());
            fixture.restitution = 0.0f;
            fixture.friction = 1000.0f;
        }
        fixture.shape = shape;
        fixture.userData = tile;
        BodyDef def = new BodyDef();
        if (dynamic)
        {
            def.type = BodyType.DYNAMIC;
            fixture.density = 1.0f;
        }
        def.position = new Vec2((position.x),(position.y));
        
        Body body = _world.createBody(def);
        body.createFixture(fixture);
        return body;        
    }
}
