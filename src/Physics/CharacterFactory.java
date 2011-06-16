/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import Entities.AIEntity;
import Physics.sPhysics.BodyCategories;
import java.util.Hashtable;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

/**
 *
 * @author A203946
 */
class CharacterFactory implements iPhysicsFactory {

    public CharacterFactory() {
    }

    public Body useFactory(Hashtable _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        AIEntity entity = (AIEntity)_parameters.get("aIEntity");
        CircleShape wheelShape = new CircleShape();
        FixtureDef circleFixture = new FixtureDef();
        wheelShape.m_radius = 0.45f;
        circleFixture.density = 4;
        circleFixture.friction = 500;
        circleFixture.filter.categoryBits = (1 << BodyCategories.ePlayer.ordinal());
        circleFixture.filter.maskBits = (1 << BodyCategories.eTiles.ordinal()) | (1 << BodyCategories.ePlayer.ordinal());
        circleFixture.shape = wheelShape;
        PolygonShape axelShape = new PolygonShape();
        FixtureDef axelFixture = new FixtureDef();
        axelShape.setAsBox(0.1f, 0.1f);
        axelFixture.density = 0.001f;
        axelFixture.filter.categoryBits = (1 << BodyCategories.ePlayer.ordinal());
        axelFixture.filter.maskBits = (1 << BodyCategories.eTiles.ordinal()) | (1 << BodyCategories.ePlayer.ordinal());
        axelFixture.filter.groupIndex = -100;
        axelFixture.shape = axelShape;
        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        def.userData = entity;
        def.position = position;
        
        Body body = _world.createBody(def);
        body.createFixture(circleFixture);
        
        def.fixedRotation = true;
        def.userData = null;
        Body axelBody = _world.createBody(def);
        body.createFixture(axelFixture);
        
        RevoluteJointDef wheelJoint = new RevoluteJointDef();
        wheelJoint.collideConnected = false;
        wheelJoint.maxMotorTorque = 5000.0f;
        wheelJoint.enableMotor = true;
        wheelJoint.bodyA = body;
        wheelJoint.bodyB = axelBody;
        wheelJoint.collideConnected = false;
        RevoluteJoint joint = (RevoluteJoint)_world.createJoint(wheelJoint);
        entity.mJoint = joint;
        return body;
    }
    
}
