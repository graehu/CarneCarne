/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import org.newdawn.slick.SlickException;

/**
 *
 * @author Almax
 */
class StaticSkinFactory implements iSkinFactory
{
    public iSkin useFactory() throws SlickException
    {
        return new StaticSkin("data/temp.png");
    }
}
