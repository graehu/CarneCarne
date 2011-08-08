/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.PlayerInputController;
import Events.AreaEvents.CheckPointZone;
import Events.PlayerCreatedEvent;
import Events.sEvents;
import Graphics.Skins.CharacterSkin;
import Graphics.Skins.CharacterSkin.CharacterSubSkin;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import World.sWorld;
import org.jbox2d.common.Vec2;
import java.util.ArrayList;
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
        CheckPointZone checkPoint = (CheckPointZone)_parameters.get("checkPoint");
        //if (!used)
        {
            used = true;
            
            //create a CharacterSubSkin for each layer of the character skin
            ArrayList<CharacterSubSkin> subSkins = new ArrayList<CharacterSubSkin>();
            //they must be in render order
            ArrayList<String> bodies = new ArrayList(Arrays.asList("bdy","gum","edi","spi","wtr"));
            for(String body : bodies) 
            {
                subSkins.add(new CharacterSkin.CharacterSubSkin(body, CharacterSubSkin.SubType.eStatic, 64, 64, new Vec2(0,0)));
            }
            subSkins.add(new CharacterSkin.CharacterSubSkin("carne_fly", CharacterSubSkin.SubType.eAnimated, 198, 139, new Vec2(-70,-60)));
            subSkins.add(new CharacterSkin.CharacterSubSkin("idle_burp2", CharacterSubSkin.SubType.eAnimated, 130, 115, new Vec2(-33,-36)));
            subSkins.add(new CharacterSkin.CharacterSubSkin("idle_burp2_right", CharacterSubSkin.SubType.eAnimated, 130, 115, new Vec2(-33,-36)));
            subSkins.add(new CharacterSkin.CharacterSubSkin("face", CharacterSubSkin.SubType.e32Dir, 84, 79, new Vec2(-9,-1)));
            subSkins.add(new CharacterSkin.CharacterSubSkin("faceOpen", CharacterSubSkin.SubType.e32Dir, 84, 79, new Vec2(-9,-1)));
            subSkins.add(new CharacterSkin.CharacterSubSkin("hat", CharacterSubSkin.SubType.e32Dir, 130, 115, new Vec2(-33,-36)));
            subSkins.add(new CharacterSkin.CharacterSubSkin("pea_stun_large", CharacterSubSkin.SubType.eAnimated, 107, 39, new Vec2(-21.5f,0)));
            //draw tongue last
            subSkins.add(new CharacterSkin.CharacterSubSkin("tng", CharacterSubSkin.SubType.eStatic, 5, 5, new Vec2(32,32)));
            int player = (Integer)_parameters.get("playerNumber");
            subSkins.add(new CharacterSkin.CharacterSubSkin("Player" + (player+1), CharacterSubSkin.SubType.eStatic, 64, 64, new Vec2(0,-64)));
            
            
            HashMap params = new HashMap();
            
            params.put("subSkins", subSkins);
            
            iSkin skin = sSkinFactory.create("character", params);
            
            skin.activateSubSkin("bdy", false, 0);

            PlayerEntity entity = new PlayerEntity(skin, checkPoint);
            HashMap parameters = new HashMap();
            parameters.put("position", position);
            parameters.put("aIEntity", entity);
            parameters.put("category", sWorld.BodyCategories.ePlayer);
            entity.setBody(sWorld.useFactory("PlayerFactory",parameters));
            PlayerInputController controller = new PlayerInputController(entity, player);
            entity.setController(controller);
            sWorld.addPlayer(entity.getBody());
            sEvents.triggerEvent(new PlayerCreatedEvent(entity, player));
            return entity;
        }
    }
}
