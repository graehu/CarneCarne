/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import Entities.AIEntity;
import Entities.Entity;
import Graphics.BodyCamera;
import Graphics.iCamera;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
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
    
    public static boolean rayCast(Vec2 start, Vec2 end)
    {
        if (start.x > end.x)
        {
            float x = start.x;
            start.x = end.x;
            end.x = x;
        }
        if (start.y > end.y)
        {
            float y = start.y;
            start.y = end.y;
            end.y = y;
        }
        //AABB aabb = new AABB(start,end);
        //Shape[] shapes = mWorld.query(aabb, 100000); /// FIXME try setting this to 0 or -1
        //Shape[] shapes = mWorld.
        Shape closestHit;
        float distance;
        /*for (int i = 0; i < shapes.length; i++)
        {
            shapes[i].
        }*/
        return false;
    }
    public static void createBodyCamera(Body _body)
    {
        mCamera = new BodyCamera(_body);
    }
    public static Vec2 translate(Vec2 _position)
    {
        return mCamera.translate(_position);
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






























