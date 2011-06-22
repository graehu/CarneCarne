/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.ZombieController;
import Events.PlayerCreatedEvent;
import Events.sEvents;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import World.sWorld;
import java.util.Arrays;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class ZombieFactory implements iEntityFactory {

    public ZombieFactory() {
    }

    public Entity useFactory(HashMap _parameters)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        //if (!used)
        HashMap animDef = new HashMap();
        animDef.put("ref", "ss_1");
        animDef.put("anims", Arrays.asList("body"));
        iSkin skin = sSkinFactory.create("character", animDef);
        skin.startAnim("body", true, 1.0f);
        AIEntity entity = new AIEntity(skin);
        HashMap parameters = new HashMap();
        parameters.put("position", position);
        parameters.put("aIEntity", entity);
        parameters.put("category", sWorld.BodyCategories.eEnemy);
        entity.mBody = sWorld.useFactory("CharacterFactory",parameters);
        ZombieController controller = new ZombieController(entity);
        entity.mController = controller;
        sEvents.triggerEvent(new PlayerCreatedEvent(entity));
        return entity;
    }
    
}
