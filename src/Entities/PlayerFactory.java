/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.PlayerInputController;
import Events.PlayerCreatedEvent;
import Events.sEvents;
import Graphics.iSkin;
import Physics.sPhysics;
import org.jbox2d.common.Vec2;
import Graphics.sSkinFactory;
import java.util.Arrays;
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
            animDef.put("ref", "ss_1");
            animDef.put("anims", Arrays.asList("spy", "body"));
            iSkin skin = sSkinFactory.create("character", animDef);
            skin.startAnim("body");
            AIEntity entity = new AIEntity(skin);
            entity.mBody = sPhysics.createPlayerBody(entity, position);
            PlayerInputController controller = new PlayerInputController(entity);
            entity.mController = controller;
            sPhysics.createBodyCamera(entity.mBody);
            sEvents.triggerEvent(new PlayerCreatedEvent(entity));
            return entity;
        }
        /*Entity entity = new Player(sSkinFactory.create("static"));
        entity.mBody = sPhysics.createTile(entity, "GregIsGay", position);
        //entity.mBody = sPhysics.create(entity, position);
        return entity;*/
    }
}
