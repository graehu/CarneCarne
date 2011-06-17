/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Aaron
 */
public class CharacterSkinFactory implements iSkinFactory {
    CharacterSkinFactory()
    {
        
    }
    public iSkin useFactory(HashMap _params) throws SlickException
    {
        return new CharacterSkin(   _params.containsKey("ref") ? "assets/" + (String)_params.get("ref") + ".def" : null,
                                    _params.containsKey("anims") ? (List<String>)_params.get("anims") : new ArrayList(),
                                    _params.containsKey("duration") ? (Integer)_params.get("duration") : 41); //~24fps
    }
}
