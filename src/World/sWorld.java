/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.Entity;
import Events.TileDestroyedEvent;
import Events.sEvents;
import Graphics.FreeCamera;
import Graphics.iCamera;
import Graphics.sGraphicsManager;
import Level.Tile;
import Level.sLevel.TileType;
import java.util.HashMap;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.structs.collision.RayCastInput;
import org.jbox2d.structs.collision.RayCastOutput;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
/**
 *
 * @author alasdair
 */
public class sWorld
{
    private static World mWorld;
    private static iCamera mCamera;
    private static HashMap<String,iPhysicsFactory> factories;


    public enum BodyCategories
    {
        ePlayer,
        eEnemy,
        eEdibleTiles,
        eNonEdibleTiles,
        eSpatTiles,
        eWater,
        eIce,
        eGum,
        eTar,
        eCheckPoint,
        eSpikes,
        eFire,
        eBodyCategoriesMax;
    }
    private sWorld()
    {
           
    }
    public static void init()
    {
        mWorld = new World(new Vec2(0,9.8f),true);   
        mWorld.setContactListener(new WorldContactListener());
        factories = new HashMap<String, iPhysicsFactory>();
        factories.put("TileFactory", new TileFactory());
        factories.put("CharacterFactory", new CharacterFactory());
        factories.put("NonEdibleTileFactory", new NonEdibleTileFactory());
        factories.put("SpatBlockFactory", new SpatBlockBodyFactory());
        factories.put("TileArrayFactory", new TileArrayFactory());
        factories.put("CheckPointFactory", new CheckPointFactory());
        factories.put("SeeSawBodyFactory", new SeeSawBodyFactory());
        factories.put("MovingPlatformBodyFactory", new MovingPlatformBodyFactory());
        factories.put("FireParticleBody", new FireParticleBody());
        mCamera = new FreeCamera( new Rectangle(0,0,sGraphicsManager.getTrueScreenDimensions().x, sGraphicsManager.getTrueScreenDimensions().y));        
    }
    
    public static Body useFactory(String _factory, HashMap _parameters)
    {
        return factories.get(_factory).useFactory(_parameters, mWorld);
    }
    private static class TongueCallback implements RayCastCallback
    {
        Fixture fixture;
        Vec2 start, end;
        private int collisionMask;
        public TongueCallback(Vec2 _start, Vec2 _end)
        {
            start = _start;
            end = _end;
            collisionMask = (1 << BodyCategories.eEdibleTiles.ordinal())|
                    (1 << BodyCategories.eNonEdibleTiles.ordinal())|
                    (1 << BodyCategories.eGum.ordinal())|
                    (1 << BodyCategories.eSpatTiles.ordinal())|
                    (1 << BodyCategories.eIce.ordinal());
        }
        public float reportFixture(Fixture _fixture, Vec2 _p1, Vec2 _p2, float _fraction)
        {
            if ((_fixture.m_filter.categoryBits & collisionMask) != 0)
            {
                RayCastInput input = new RayCastInput();
                input.p1.x = start.x;
                input.p1.y = start.y;
                input.p2.x = end.x;
                input.p2.y = end.y;
                RayCastOutput output = new RayCastOutput();
                //if (_fixture.raycast(output, input)) Is this neccessary?
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
    private static Fixture mLastHit;
    public static Tile eatTiles(Vec2 start, Vec2 end)
    {
        TongueCallback callback = new TongueCallback(start, end);
        mWorld.raycast(callback, start, end);
        if (callback.getFixture() == null)
        {
            return null;
        }
        mLastHit = callback.getFixture();
        Tile tile = (Tile)callback.getFixture().getUserData();
        Tile ret = tile;
        if (tile != null)
        {
            TileType tileType = tile.getTileType();
            switch (tileType)
            {
                case eEdible:
                case eGum:
                case eMelonFlesh:
                case eChilli:
                {
                    ret = tile.clone();
                    sEvents.triggerEvent(new TileDestroyedEvent((int)callback.getFixture().m_body.getPosition().x, (int)callback.getFixture().m_body.getPosition().y));
                    tile.destroyFixture();
                    //if (callback.getFixture().m_filter.categoryBits != (1 << BodyCategories.eSpatTiles.ordinal()))
                    //    tile.getTileGrid().destroyTile((int)callback.getFixture().m_body.getPosition().x, (int)callback.getFixture().m_body.getPosition().y);
                    break;
                }
                case eSwingable:
                case eIce:
                case eIndestructible:
                {
                    break;
                }
            }
        }
        return ret;
    }
    public static boolean smashTiles(Vec2 start, Vec2 end)
    {
        TongueCallback callback = new TongueCallback(start, end);
        mWorld.raycast(callback, start, end);
        if (callback.getFixture() == null)
        {
            return false;
        }
        Tile tile = (Tile)callback.getFixture().getUserData();
        if (tile != null)
        {
            TileType tileType = tile.getTileType();
            switch (tileType)
            {
                case eSwingable:
                {
                    if (tile.getTileGrid().damageTile((int)callback.getFixture().m_body.getPosition().x, (int)callback.getFixture().m_body.getPosition().y))
                    {
                        //tile.getTileGrid().destroyTile((int)callback.getFixture()..getPosition().x, (int)callback.getFixture().m_body.getPosition().y);
                        sEvents.triggerEvent(new TileDestroyedEvent((int)callback.getFixture().m_body.getPosition().x, (int)callback.getFixture().m_body.getPosition().y));
                        tile.destroyFixture();
                    }
                    break;
                }
                case eEdible:
                case eIce:
                case eMelonSkin:
                {
                    //tile.getTileGrid().destroyTile((int)callback.getFixture().m_body.getPosition().x, (int)callback.getFixture().m_body.getPosition().y);
                    sEvents.triggerEvent(new TileDestroyedEvent((int)callback.getFixture().m_body.getPosition().x, (int)callback.getFixture().m_body.getPosition().y));
                    tile.destroyFixture();
                    break;
                }
                case eEmpty:
                case eIndestructible:
                {
                    break;
                }
            }
        }
        return true;        
    }
    static Body groundBody = null;
    public static Joint createMouseJoint(Vec2 _targetPosition, Body _body)
    { /// FIXME mouse joint doesn't work the same as in c++
        if (groundBody == null)
        {
            BodyDef bodyDef = new BodyDef();
            bodyDef.position = _targetPosition;
            groundBody = mWorld.createBody(bodyDef);
        }
        PrismaticJointDef def = new PrismaticJointDef();
        Vec2 axis = _body.getPosition().sub(_targetPosition);
        axis.normalize();
        def.initialize(_body, groundBody, _targetPosition, axis);
        def.enableMotor = true;
        def.motorSpeed = 10.0f;
        def.maxMotorForce = 5000.0f;
        Joint joint = mWorld.createJoint(def);
        return joint;
    }
    public static DistanceJoint createTongueJoint(Body _body)
    {
        DistanceJointDef def = new DistanceJointDef();
        Vec2 anchor = ((Tile)mLastHit.getUserData()).getPosition();
        def.initialize(_body, mLastHit.m_body, _body.getPosition(), anchor);
        def.collideConnected = true;
        Vec2 direction = _body.getPosition().sub(anchor);
        def.length = direction.normalize();
        //def.frequencyHz = 1.0f;
        //def.dampingRatio = 0.1f;
        def.frequencyHz = 30.0f;
        def.dampingRatio = 1.0f; /// Reduce these to make his tongue springy
        return (DistanceJoint)mWorld.createJoint(def);
    }
    public static void destroyBody(Body _body)
    {
        mWorld.destroyBody(_body);
    }
    public static void destroyJoint(Joint _joint)
    {
        mWorld.destroyJoint(_joint);
    }
    public static void destroyMouseJoint(Joint _joint)
    {
        mWorld.destroyJoint(_joint);
        mWorld.destroyBody(groundBody);
    }
    public static void addPlayer(Body _body)
    {
        mCamera = mCamera.addPlayer(_body);
    }
    public static void resizeViewport(Rectangle _viewPort)
    {
        mCamera.resize(_viewPort);
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
    /*public static Body createPlayerBody(AIEntity _entity, Vec2 _position)
    {
        Hashtable parameters = new Hashtable();
        parameters.put("position", _position);
        parameters.put("aIEntity", _entity);
        return useFactory("CharacterFactory",parameters);
    }*/
    public static void update(float _time)
    {
        float secondsPerFrame = 16.666f;
        try
        {
            mWorld.step(secondsPerFrame/1000.0f, 4, 2);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            
        }
        Body body = mWorld.getBodyList();
        while (body != null)
        {
            Entity entity = (Entity)body.getUserData();
            if (entity != null)
                entity.update();
            body = body.getNext();
        }
        mCamera.update();
    }

    public static iCamera getCamera()
    {
        return mCamera;
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






























