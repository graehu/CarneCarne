/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import org.jbox2d.dynamics.Body;
import Graphics.iSkin;
/**
 *
 * @author alasdair
 */
public class Player extends Entity {
    
    public Player(iSkin _skin)
    {
        super(_skin);
    }
    public void update()
    {
        
    }
    public void render()
    {
        int xPixel = (int)mBody.getPosition().x;
        int yPixel = (int)mBody.getPosition().y;
        mSkin.render(xPixel,yPixel);
    }
}