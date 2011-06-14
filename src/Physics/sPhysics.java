/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import Entities.Entity;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import org.newdawn.slick.Animation;
/**
 *
 * @author alasdair
 */
public class sPhysics {
    private static World mWorld;
    private sPhysics()
    {

    }
    public static void init()
    {
        mWorld = new World(new AABB(new Vec2(-1000,-1000), new Vec2(1000, 1000)),new Vec2(0,9.8f),true);
        //mWorld.setDebugDraw(new DebugDraw());
    }
    
    public static Body create(Entity _entity, Vec2 _position)
    {
        PolygonDef shape = new PolygonDef();
        shape.setAsBox(2, 2);
        shape.density = 1;
        BodyDef def = new BodyDef();
        def.userData = _entity;
        def.massData = new MassData();
        def.massData.mass = 1;
        def.position = _position;
        Body body = mWorld.createBody(def);
        body.createShape(shape);
        
        body.setMassFromShapes();
        shape.setAsBox(30, 30);
        return body;
    }
    
    public static Body createTile(/*Entity _entity, */String _name, Vec2 _position)
    { 
        PolygonDef shape = new PolygonDef();
        shape.setAsBox(64, 64);
        BodyDef def = new BodyDef();
        //def.userData = _entity;
        def.position = new Vec2((_position.x*64),(_position.y*64));
        
        Body body = mWorld.createBody(def);
        body.createShape(shape);
        
        shape.setAsBox(30, 30);
        return body; 
    }
    
    public static void update(float _time)
    {
        mWorld.step(_time/1000.0f, 8);
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






























