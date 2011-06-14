/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

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
        return new AnimatedSkin(_params.containsKey("ref") ? "data/" + (String)_params.get("ref") : null,
                                _params.containsKey("x1") ? (Integer)_params.get("x1") : 0,
                                _params.containsKey("y1") ? (Integer)_params.get("y1") : 0,
                                _params.containsKey("x2") ? (Integer)_params.get("x2") : 0,
                                _params.containsKey("y2") ? (Integer)_params.get("y2") : 0,
                                _params.containsKey("hScan") ? (Boolean)_params.get("hScan") : false,
                                _params.containsKey("duration") ? (Integer)_params.get("duration") : 41); //~24fps
    }
}
