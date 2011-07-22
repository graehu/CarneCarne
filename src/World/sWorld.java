/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.AIEntity;
import World.PhysicsFactories.SeeSawBodyFactory;
import World.PhysicsFactories.SpatBlockBodyFactory;
import World.PhysicsFactories.FireParticleBody;
import World.PhysicsFactories.MovingPlatformBodyFactory;
import World.PhysicsFactories.CircleCharFactory;
import World.PhysicsFactories.TileArrayFactory;
import World.PhysicsFactories.NonEdibleTileFactory;
import World.PhysicsFactories.TileFactory;
import World.PhysicsFactories.iPhysicsFactory;
import World.PhysicsFactories.BoxCharFactory;
import Entities.Entity;
import Entities.SpatBlock;
import Events.AreaEvents.AreaEvent;
import Events.PlayerSwingEvent;
import Events.TileDestroyedEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Camera.FreeCamera;
import Graphics.Camera.iCamera;
import Graphics.sGraphicsManager;
import Level.FakeTile;
import Level.RootTile;
import Level.Tile;
import Level.sLevel;
import Level.sLevel.TileType;
import World.PhysicsFactories.GroundBodyFactory;
import World.PhysicsFactories.PlayerFactory;
import java.util.HashMap;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.structs.collision.RayCastInput;
import org.jbox2d.structs.collision.RayCastOutput;
import org.newdawn.slick.Graphics;
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
    private static ResizeListener mResizeListener;

    public static void destroy()
    {
        try
        {
            while (true)
            {
                mWorld.destroyBody(mWorld.getBodyList());
            }
        }
        catch (NullPointerException e)
        {
            
        }
        mWorld = new World(new Vec2(0,9.8f),true);   
        mWorld.setContactListener(new WorldContactListener());
        Vec2 s = sGraphicsManager.getTrueScreenDimensions();
        mCamera = new FreeCamera(new Rectangle(0,0,s.x, s.y));
    }
    static class ResizeListener implements iEventListener
    {
        ResizeListener()
        {
            sEvents.subscribeToEvent("WindowResizeEvent", this);
        }
        public boolean trigger(iEvent _event)
        {
            if(_event.getType().equals("WindowResizeEvent"))
            {
                Vec2 s = sGraphicsManager.getTrueScreenDimensions();
                sWorld.resizeViewport(new Rectangle(0,0,s.x, s.y));
            }
            return true;
        }
    }


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
        eEtherealEnemy,
        eBodyCategoriesMax;
    }
    private sWorld()
    {
           
    }
    public static void init()
    {
        Vec2 s = sGraphicsManager.getTrueScreenDimensions();
        mWorld = new World(new Vec2(0,9.8f),true);   
        mWorld.setContactListener(new WorldContactListener());
        factories = new HashMap<String, iPhysicsFactory>();
        factories.put("TileFactory", new TileFactory());
        factories.put("PlayerFactory", new PlayerFactory());
        factories.put("BoxCharFactory", new BoxCharFactory());
        factories.put("CircleCharFactory", new CircleCharFactory());
        factories.put("NonEdibleTileFactory", new NonEdibleTileFactory());
        factories.put("SpatBlockFactory", new SpatBlockBodyFactory());
        factories.put("TileArrayFactory", new TileArrayFactory());
        factories.put("CheckPointFactory", new CheckPointFactory());
        factories.put("SeeSawBodyFactory", new SeeSawBodyFactory());
        factories.put("MovingPlatformBodyFactory", new MovingPlatformBodyFactory());
        factories.put("FireParticleBody", new FireParticleBody());
        factories.put("GroundBody", new GroundBodyFactory());
        mCamera = new FreeCamera(new Rectangle(0,0,s.x, s.y));        
        resizeViewport(new Rectangle(0,0,s.x, s.y));    
        mResizeListener = new ResizeListener();
    }
    
    public static Body useFactory(String _factory, HashMap _parameters)
    {
        return factories.get(_factory).useFactory(_parameters, mWorld);
    }
    private static class AABBCallback implements QueryCallback
    {
        int mCollisionMask;
        Body mBody;
        public AABBCallback(int _collisionMask)
        {
            mCollisionMask = _collisionMask;
            mBody = null;
        }
        public boolean reportFixture(Fixture _fixture)
        {
            if ((_fixture.m_filter.categoryBits & mCollisionMask) != 0 ||
                    _fixture.getBody().getType().equals(BodyType.KINEMATIC))
            {
                mBody = _fixture.getBody();
                return false;
            }
            return true;
        }
    }
    private static class TongueCallback implements RayCastCallback
    {
        Fixture fixture;
        Vec2 start, end;
        int collisionMask;
        public TongueCallback(Vec2 _start, Vec2 _end)
        {
            start = _start;
            end = _end;
            collisionMask = (1 << BodyCategories.eEdibleTiles.ordinal())|
                    (1 << BodyCategories.eNonEdibleTiles.ordinal())|
                    (1 << BodyCategories.eGum.ordinal())|
                    (1 << BodyCategories.eSpatTiles.ordinal())|
                    (1 << BodyCategories.ePlayer.ordinal())|
                    (1 << BodyCategories.eEnemy.ordinal())|
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
    private static TongueAnchor mLastTongueAnchor;
    public static Tile eatTiles(Vec2 start, Vec2 end)
    {
        TongueCallback callback = new TongueCallback(start, end);
        mWorld.raycast(callback, start, end);
        if (callback.getFixture() == null)
        {
            return null;
        }
        mLastHit = callback.getFixture();
        if (callback.getFixture().m_filter.categoryBits == (1 << BodyCategories.eSpatTiles.ordinal()))
        {
            int id = ((SpatBlock)callback.getFixture().getBody().getUserData()).getRootId();
            RootTile tile = sLevel.getRootTile(id);
            mWorld.destroyBody(callback.getFixture().getBody());
            return new Tile(id, tile, null, -1, -1);
        }
        if (callback.getFixture().m_filter.categoryBits == (1 << BodyCategories.ePlayer.ordinal())||
                callback.getFixture().m_filter.categoryBits == (1 << BodyCategories.eEnemy.ordinal()))
        {
            mLastHit = callback.getFixture();
            mLastTongueAnchor = new PlayerTongueAnchor(mLastHit, new Vec2(0.0f, 0.0f));
            return new FakeTile(mLastHit.getBody());
        }
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
                    Tile hitTile = (Tile)mLastHit.getUserData();
                    mLastTongueAnchor = new MovingBodyTongueAnchor(hitTile.getFixture(), hitTile.getLocalPosition());
                    break;
                }
            }
        }
        else
        {
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
        if (callback.getFixture().m_filter.categoryBits == (1 << BodyCategories.ePlayer.ordinal())||
                callback.getFixture().m_filter.categoryBits == (1 << BodyCategories.eEnemy.ordinal()))
        {
            Vec2 direction = end.sub(start);
            direction.normalize();
            ((AIEntity)callback.getFixture().getBody().getUserData()).stun(direction);
        }
        else
        {
            Tile tile = (Tile)callback.getFixture().getUserData();
            if (tile != null) /// FIXME might be able to remove this check
            {
                TileType tileType = tile.getTileType();
                switch (tileType)
                {
                    case eSwingable:
                    case eEdible:
                    case eMelonSkin:
                    {
                        if (tile.damageTile())
                        {
                            sEvents.triggerEvent(new TileDestroyedEvent((int)callback.getFixture().m_body.getPosition().x, (int)callback.getFixture().m_body.getPosition().y));
                            tile.destroyFixture();
                        }
                        break;
                    }
                    case eIce:
                    case eEmpty:
                    case eIndestructible:
                    {
                        break;
                    }
                }
            }
        }
        return true;
    }
    public static iCamera switchCamera(iCamera _newCamera)
    {
        iCamera camera = mCamera;
        mCamera = _newCamera;
        return camera;
    }
    public static Body searchAABB(AABB _aabb, int _collisionMask)
    {
        AABBCallback callback = new AABBCallback(_collisionMask);
        mWorld.queryAABB(callback, _aabb);
        return callback.mBody;
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
    public static TongueAnchor getLastTongueHit(int _player)
    {
        sEvents.triggerEvent(new PlayerSwingEvent(_player));
        return mLastTongueAnchor;
    }
    public static DistanceJoint createTongueJoint(Body _body)
    {
        DistanceJointDef def = new DistanceJointDef();
        def.initialize(_body, mLastHit.m_body, _body.getPosition(), mLastTongueAnchor.getPosition());
        def.collideConnected = true;
        Vec2 direction = _body.getPosition().sub(mLastTongueAnchor.getPosition());
        def.length = direction.normalize();
        def.length = 0.0f;
        def.frequencyHz = 1.0f;
        def.dampingRatio = 1.0f;
        return (DistanceJoint)mWorld.createJoint(def);
    }
    public static Body createAreaEvent(int _x, int _y, int _x2, int _y2, AreaEvent _event)
    {
        BodyDef def = new BodyDef();
        def.position = new Vec2(((_x2-_x)*0.5f)+_x+0.5f, ((_y2-_y)*0.5f)+_y+0.5f);
        def.userData = _event;
        FixtureDef fixture = new FixtureDef();
        fixture.filter.categoryBits = (1 << BodyCategories.eCheckPoint.ordinal());
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(((float)_x2+1-_x)*0.5f, ((float)_y2+1-_y)*0.5f);
        fixture.shape = shape;
        fixture.isSensor = true;
        Body body = mWorld.createBody(def);
        body.createFixture(fixture);
        return body;
    }
    public static void destroyBody(Body _body)
    {
        if (mWorld.isLocked())
        {
            throw new UnsupportedOperationException("World is locked");
        }
        mWorld.destroyBody(_body);
    }
    public static void destroyJoint(Joint _joint)
    {
        if (mWorld.isLocked())
        {
            throw new UnsupportedOperationException("World is locked");
        }
        mWorld.destroyJoint(_joint);
    }
    public static void destroyMouseJoint(Joint _joint)
    {
        if (mWorld.isLocked())
        {
            throw new UnsupportedOperationException("World is locked");
        }
        mWorld.destroyJoint(_joint);
        //mWorld.destroyBody(groundBody);
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
    public static void update(Graphics _graphics, float _time)
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
        mCamera.update(_graphics);
    }

    public static iCamera getCamera()
    {
        return mCamera;
    }
    private static class RenderCallback implements QueryCallback
    {
        public boolean reportFixture(Fixture _fixture)
        {
            Entity entity = (Entity)_fixture.getBody().getUserData();
            if (entity != null)
                entity.render();
            return true;
        }
    }
    public static void render()
    {
        AABB aabb = new AABB(sWorld.translateToPhysics(new Vec2(0,0)), sWorld.translateToPhysics(sGraphicsManager.getScreenDimensions()));
        mWorld.queryAABB(new RenderCallback(), aabb);
        mWorld.drawDebugData();
    }
}






























