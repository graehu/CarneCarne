/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import Entities.AIEntity;
import Entities.Entity;
import Events.TileDestroyedEvent;
import Events.sEvents;
import Graphics.BodyCamera;
import Graphics.iCamera;
import Level.sLevel;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.structs.collision.RayCastInput;
import org.jbox2d.structs.collision.RayCastOutput;
/**
 *
 * @author alasdair
 */
public class sPhysics {
    private static World mWorld;
    private static iCamera mCamera;
    public enum BodyCategories
    {
        ePlayer,
        eTiles,
        eBodyCategoriesMax
    }
    private sPhysics()
    {

    }
    public static void init()
    {
        mWorld = new World(new Vec2(0,9.8f),true);
    }
    
    private static class TongueCallback implements RayCastCallback
    {
        Fixture fixture;
        Vec2 start, end;
        public TongueCallback(Vec2 _start, Vec2 _end)
        {
            start = _start;
            end = _end;
        }
        public float reportFixture(Fixture _fixture, Vec2 _p1, Vec2 _p2, float _fraction)
        {
            if (_fixture.m_filter.categoryBits == (1 << BodyCategories.eTiles.ordinal()))
            {
                RayCastInput input = new RayCastInput();
                input.p1.x = start.x;
                input.p1.y = start.y;
                input.p2.x = end.x;
                input.p2.y = end.y;
                RayCastOutput output = new RayCastOutput();
                //if (_fixture.raycast(output, input))
                {
                    fixture = _fixture;
                    return _fraction;
                }
            }
            return 1.0f;
        }
        
        public Fixture getFixture()
        {
            return fixture;
        }
    }
    public static boolean rayCastTiles(Vec2 start, Vec2 end)
    {
        TongueCallback callback = new TongueCallback(start, end);
        mWorld.raycast(callback, start, end);
        if (callback.getFixture() == null)
        {
            return false;
        }
        mWorld.destroyBody(callback.getFixture().m_body);
        sLevel.destroyTile((int)callback.getFixture().m_body.getPosition().x, (int)callback.getFixture().m_body.getPosition().y);
        sEvents.triggerEvent(new TileDestroyedEvent((int)callback.getFixture().m_body.getPosition().x, (int)callback.getFixture().m_body.getPosition().y));
        return true;
    }
    public static void createBodyCamera(Body _body)
    {
        mCamera = new BodyCamera(_body);
    }
    public static Vec2 translateToWorld(Vec2 _position)
    {
        return mCamera.translateToWorld(_position);
    }
    public static Vec2 translateToPhysics(Vec2 _position)
    {
        return mCamera.translateToPhysics(_position);
    }
    public static Vec2 getPixelTranslation()
    {
        return mCamera.getPixelTranslation();
    }
    public static Body createPlayerBody(AIEntity _entity, Vec2 _position)
    {
        CircleShape wheelShape = new CircleShape();
        FixtureDef circleFixture = new FixtureDef();
        wheelShape.m_radius = 0.45f;
        circleFixture.density = 4;
        circleFixture.friction = 50;
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
        def.userData = _entity;
        def.position = _position;
        
        Body body = mWorld.createBody(def);
        body.createFixture(circleFixture);
        
        def.fixedRotation = true;
        def.userData = null;
        Body axelBody = mWorld.createBody(def);
        body.createFixture(axelFixture);
        
        RevoluteJointDef wheelJoint = new RevoluteJointDef();
        wheelJoint.collideConnected = false;
        wheelJoint.maxMotorTorque = 5000.0f;
        wheelJoint.enableMotor = true;
        wheelJoint.bodyA = body;
        wheelJoint.bodyB = axelBody;
        wheelJoint.collideConnected = false;
        RevoluteJoint joint = (RevoluteJoint)mWorld.createJoint(wheelJoint);
        _entity.mJoint = joint;
        return body;
    }
    
    public static Body createTile(String _name, Vec2 _position)
    { 
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.filter.categoryBits = (1 << BodyCategories.eTiles.ordinal());
        fixture.filter.maskBits = (1 << BodyCategories.eTiles.ordinal()) | (1 << BodyCategories.ePlayer.ordinal());
        BodyDef def = new BodyDef();
        //def.userData = _entity;
        def.position = new Vec2((_position.x),(_position.y));
        
        Body body = mWorld.createBody(def);
        body.createFixture(fixture);
        return body;
    }
    
    public static void update(float _time)
    {
        mWorld.step(_time/1000.0f, 8, 8);
        Body body = mWorld.getBodyList();
        while (body != null)
        {
            Entity entity = (Entity)body.getUserData();
            if (entity != null)
                entity.update();
            body = body.getNext();
        }
    }
    
    public static void render()
    {
        Body body = mWorld.getBodyList();
        while (body != null)
        {
            Entity entity = (Entity)body.getUserData();
            if (entity != null)
                entity.render();
            body = body.getNext();
        }
        mWorld.drawDebugData();      
    }
}






























