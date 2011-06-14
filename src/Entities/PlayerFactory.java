/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Physics.sPhysics;
import org.jbox2d.common.Vec2;
import Graphics.sSkinFactory;
import java.util.HashMap;
/**
 *
 * @author alasdair
 */
public class PlayerFactory implements iEntityFactory {
    
    boolean used;
    public PlayerFactory()
    {
        used = false;
    }
    public Entity useFactory(HashMap _parameters)
    {
        /// NOTE maybe put a event trigger in here
        Vec2 position = (Vec2)_parameters.get("position");
        //if (!used)
        {
            used = true;
            HashMap animDef = new HashMap();
            animDef.put("ref", "sprites.png");
            animDef.put("x1", 0);
            animDef.put("y1", 1);
            animDef.put("x2", 6);
            animDef.put("y2", 1);
            Entity entity = new Player(sSkinFactory.create("animated", animDef));
            entity.mBody = sPhysics.create(entity, position);
            return entity;
        }
        /*Entity entity = new Player(sSkinFactory.create("static"));
        entity.mBody = sPhysics.createTile(entity, "GregIsGay", position);
        //entity.mBody = sPhysics.create(entity, position);
        return entity;*/
    }
}
