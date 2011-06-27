/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.PlayerInputController;
import Events.PlayerCreatedEvent;
import Events.sEvents;
import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;
import Graphics.Skins.sSkinFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
/**
 *
 * @author alasdair
 */
public class PlayerFactory implements iEntityFactory {
    
    boolean used;
    private int players;
    public PlayerFactory()
    {
        used = false;
        players = 0;
    }
    public Entity useFactory(HashMap _parameters)
    {
        /// NOTE maybe put a event trigger in here
        Vec2 position = (Vec2)_parameters.get("position");
        //if (!used)
        {
            used = true;
            
            //declare face animation names
            ArrayList<String> face = 
                    new ArrayList(Arrays.asList("n","nbe","nne","nebn","ne","nebe","ene","ebn",
                                                "e","ebs","ese","sebe","se","sebs","sse","sbe", 
                                                "s","sbw","ssw","swbs","sw","swbw","wsw","wbs", 
                                                "w","wbn","wnw","nwbw","nw","nwbn","nnw","nbw"));
            //derive hat names from face
            ArrayList<String> hat = (ArrayList<String>)face.clone();
            for(int i = 0; i < hat.size(); i++)
            {
                hat.set(i, "h" + hat.get(i));
            }
            //derive mouth names from face
            ArrayList<String> mouth = (ArrayList<String>)face.clone();
            for(int i = 0; i < mouth.size(); i++)
            {
                mouth.set(i, "m" + mouth.get(i));
            }
            //derive mouthHat names from face
            ArrayList<String> mouthHat = (ArrayList<String>)face.clone();
            for(int i = 0; i < mouthHat.size(); i++)
            {
                mouthHat.set(i, "mh" + mouthHat.get(i));
            }
            //create final list in render order
            ArrayList<String> charAnims = new ArrayList(Arrays.asList("bdy", "edi", "gum", "wtr", "flt", "jly", "shn"));
            charAnims.addAll(face);
            charAnims.addAll(hat);
            charAnims.addAll(mouth);
            charAnims.addAll(mouthHat);
            charAnims.add("tng");
            charAnims.add("tngend");
            
            HashMap animDef = new HashMap();
            animDef.put("ref", "ss_1");     //declare name of .def file
            animDef.put("anims",charAnims); //declare all animations
            iSkin skin = sSkinFactory.create("character", animDef);
            //initialise facing, body and tongue animations
            skin.startAnim("e", false, 0.0f);
            skin.setOffset("tng", new Vec2(32,32));
            
            //offsets for sprites bigger than 64x64
            String[] t = {"","h","m","mh"};                          //prefixes
            Vec2[] v = {new Vec2(-9,0), new Vec2(-24,-28), new Vec2(-9,0), new Vec2(-24,-28)}; //offsets relative to above
            String[] p = {"n","s"};     //north and south
            String[] q = {"e","w"};     //east and west
            for(int k = 0; k < 4; k++)
            {
                for(int j = 0; j < 2; j++) //for north and south
                {
                    for(int i = 0; i < 2; i++) //for east and west
                    {
                        //in the format: (prefix + compass direction, offset)
                        skin.setOffset(t[k] + p[j],                     v[k]);
                        skin.setOffset(t[k] + p[j] + "b"  +q[i],        v[k]);
                        skin.setOffset(t[k] + p[j] + p[j] +q[i],        v[k]);
                        skin.setOffset(t[k] + p[j] + q[i] +"b" +p[j],   v[k]);
                        skin.setOffset(t[k] + p[j] + q[i],              v[k]);
                        skin.setOffset(t[k] + p[j] + q[i] +"b" +q[i],   v[k]);
                        skin.setOffset(t[k] + q[i] + p[j] +q[i],        v[k]);
                        skin.setOffset(t[k] + q[i] + "b"  +p[j],        v[k]);
                        skin.setOffset(t[k] + q[i],                     v[k]);
                    }
                }
            }
            AIEntity entity = new PlayerEntity(skin, position);
            HashMap parameters = new HashMap();
            parameters.put("position", position);
            parameters.put("aIEntity", entity);
            parameters.put("category", sWorld.BodyCategories.ePlayer);
            entity.mBody = sWorld.useFactory("CharacterFactory",parameters);
            PlayerInputController controller = new PlayerInputController(entity, players);
            entity.mController = controller;
            sWorld.addPlayer(entity.mBody);
            sEvents.triggerEvent(new PlayerCreatedEvent(entity));
            players++;
            return entity;
        }
    }
}
