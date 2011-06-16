/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import Entities.Entity;
import Physics.sPhysics.BodyCategories;
import java.util.HashMap;
import org.jbox2d.collision.shapes.CircleShape;
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
class SpatBlockBodyFactory implements iPhysicsFactory {

    public SpatBlockBodyFactory() {
    }

    public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        Vec2 velocity = (Vec2)_parameters.get("velocity");
        Entity entity = (Entity)_parameters.get("entity");
        CircleShape shape = new CircleShape();
        shape.m_radius = 0.5f;
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.filter.categoryBits = (1 << BodyCategories.ePlayer.ordinal());
        fixture.filter.maskBits = (1 << BodyCategories.eEdibleTiles.ordinal())|
                (1 << BodyCategories.eNonEdibleTiles.ordinal())|
                (1 << BodyCategories.ePlayer.ordinal());
        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        def.userData = entity;
        def.position = new Vec2((position.x),(position.y));
        def.linearVelocity = velocity;
        
        Body body = _world.createBody(def);
        body.createFixture(fixture);
        return body;  
    }
    
}
