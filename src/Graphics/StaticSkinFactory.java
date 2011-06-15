/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Graphics.StaticSkin;
import Graphics.iSkin;
import java.util.HashMap;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Almax
 */
class StaticSkinFactory implements iSkinFactory
{
    public iSkin useFactory(HashMap _params) throws SlickException
    {
        return new StaticSkin("data" + (String)_params.get("ref"));
    }
}
