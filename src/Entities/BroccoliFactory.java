/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.BroccoliController;
import Graphics.Skins.CharacterSkin;
import Graphics.Skins.CharacterSkin.CharacterSubSkin;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import World.sWorld;
import java.util.ArrayList;
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
        
        ArrayList<CharacterSubSkin> subSkins = new ArrayList<CharacterSubSkin>();
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_1", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_2", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        subSkins.add(new CharacterSkin.CharacterSubSkin("broc_3", CharacterSubSkin.SubType.eAnimated, 110, 110, new Vec2(0,0)));
        
        animDef.put("subSkins", subSkins);
        iSkin skin = sSkinFactory.create("character", animDef);
        skin.activateSubSkin("broc_1", true, 0.5f);
        skin.setOffset("broc_1", new Vec2(-46/2,-46));
        skin.setOffset("broc_2", new Vec2(-46/2,-46));
        skin.setOffset("broc_3", new Vec2(-46/2,-46));
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
