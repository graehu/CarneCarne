/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.BroccoliController;
import AI.CarrotController;
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
 * @author Graham
 */
public class BroccoliFactory implements iEntityFactory {

    public BroccoliFactory() {
    }

    public Entity useFactory(HashMap _parameters)
    {
        Vec2 position = (Vec2)_parameters.get("position");
        HashMap animDef = new HashMap();
        animDef.put("ref", "ss_1");
        animDef.put("anims", Arrays.asList("broc_1"));
        iSkin skin = sSkinFactory.create("character", animDef);
        skin.startAnim("broc_1", true, 1.0f);
        skin.setOffset("broc_1", new Vec2(-46/2,-46));
        AIEntity entity = new Broccoli(skin);
        HashMap parameters = new HashMap();
        parameters.put("position", position);
        parameters.put("aIEntity", entity);
        parameters.put("category", sWorld.BodyCategories.eEnemy);
        entity.mBody = sWorld.useFactory("BoxCharFactory",parameters);
        
        BroccoliController controller = new BroccoliController(entity);
        entity.mController = controller;
        return entity;
    }
    
}
