/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import Graphics.sGraphicsManager;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.SlickException;

/**
 *
 * @author alasdair
 */
public class CaveIn extends Entity
{
    CaveIn(iSkin _skin)
    {
        super(_skin);
    }
    @Override
    public void update()
    {
    }
    public void render()
    { 
        sGraphicsManager.beginTransform();
            Vec2 axis = sWorld.translateToWorld(new Vec2(mBody.getPosition().x+0.5f,mBody.getPosition().y+0.5f));
            sGraphicsManager.rotate(axis.x, axis.y, mBody.getAngle()*180.0f/(float)Math.PI);
            mSkin.render(mBody.getPosition().x,mBody.getPosition().y);
        sGraphicsManager.endTransform();
    }
    
}
