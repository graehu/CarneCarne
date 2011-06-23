/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.CaveIn;
import java.util.ArrayList;
import java.util.HashMap;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author alasdair
 */
class TileArrayFactory implements iPhysicsFactory
{

    public TileArrayFactory() {
    }

    public Body useFactory(HashMap _parameters, World _world)
    {
        ArrayList<CaveIn.Tile> tiles = (ArrayList<CaveIn.Tile>)_parameters.get("tiles");
        Object entity = _parameters.get("entity");
        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        def.userData = entity;
        
        FixtureDef fixture = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        fixture.shape = shape;
        //fixture.density = 1.0f;
        Body body = _world.createBody(def);
        for (CaveIn.Tile tile: tiles)
        {
            shape.setAsBox(0.5f, 0.5f, tile.mPosition, 0);
            body.createFixture(fixture);
        }
        return body;
    }
    
}
