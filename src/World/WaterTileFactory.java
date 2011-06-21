/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Level.sLevel;
import World.sWorld.BodyCategories;
import java.util.HashMap;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author alasdair
 */
class WaterTileFactory implements iPhysicsFactory
{
    private static Integer highestSurface;
    public WaterTileFactory()
    {
    }

    public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        sLevel.TileType tileType = (sLevel.TileType)_parameters.get("TileType");
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;
        fixture.filter.categoryBits = (1 << BodyCategories.eWater.ordinal());
        fixture.filter.maskBits = Integer.MAX_VALUE;
        fixture.userData = highestSurface;
        BodyDef def = new BodyDef();
        //def.userData = _entity;
        def.position = new Vec2((position.x),(position.y));
        
        Body body = _world.createBody(def);
        body.createFixture(fixture);
        return body;  
    }

    void setWaterHeight(int _highestSurface)
    {
        highestSurface = _highestSurface;
    }
}
