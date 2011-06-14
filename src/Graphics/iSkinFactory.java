/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import java.util.HashMap;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Almax
 */
public interface iSkinFactory {
    public iSkin useFactory(HashMap _params) throws SlickException;
}
