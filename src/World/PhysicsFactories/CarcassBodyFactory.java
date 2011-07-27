/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World.PhysicsFactories;

import Entities.AIEntity;
import Level.FakeTile;
import World.sWorld.BodyCategories;
import java.util.HashMap;
import org.jbox2d.collision.shapes.CircleShape;
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
public class CarcassBodyFactory implements iPhysicsFactory
{
    public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        Object userData = _parameters.get("userData");
        CircleShape wheelShape = new CircleShape();
        FixtureDef circleFixture = new FixtureDef();
        wheelShape.m_radius = 0.45f;
        circleFixture.density = 4;
        //circleFixture.friction = 5;
        circleFixture.filter.categoryBits = (1 << BodyCategories.eCarcass.ordinal());
        circleFixture.filter.maskBits = Integer.MAX_VALUE;
        circleFixture.shape = wheelShape;
        
        BodyDef def = new BodyDef();
        def.type = BodyType.STATIC;
        def.userData = userData;
        def.position = position;
        def.angle = (Float)_parameters.get("rotation");
        def.linearVelocity = (Vec2)_parameters.get("linearVelocity");
        def.angularVelocity = (Float)_parameters.get("angularVelocity");
        
        Body body = _world.createBody(def);
        body.createFixture(circleFixture);

        return body;
    }
    
}
