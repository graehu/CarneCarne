/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Level.Tile;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

/**
 *
 * @author alasdair
 */
public class TileFactory implements iPhysicsFactory {
    
    public Body useFactory(HashMap _parameters, World _world)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        Tile tile = (Tile)_parameters.get("Tile");
        boolean dynamic = ((Boolean)_parameters.get("isDynamic")).booleanValue();
        float angle = ((Float)_parameters.get("angle")).floatValue();
        
        BodyDef def = new BodyDef();
        if (dynamic)
        {
            def.type = BodyType.DYNAMIC;
            def.angle = angle;
        }
        def.position.x = position.x;
        def.position.y = position.y;
        //def.fixedRotation = true;
        Body body = _world.createBody(def);
        return body;        
    }
}
