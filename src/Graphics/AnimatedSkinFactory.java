/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Graphics.AnimatedSkin;
import Graphics.iSkin;
import java.util.HashMap;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Aaron
 */
//class public to graphics package only
class AnimatedSkinFactory implements iSkinFactory {
    public iSkin useFactory(HashMap _params) throws SlickException
    {
        return new AnimatedSkin(_params.containsKey("ref") ? "data/" + (String)_params.get("ref") + ".def" : null,
                                _params.containsKey("duration") ? (Integer)_params.get("duration") : 41); //~24fps
    }
}
