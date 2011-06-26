/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import Graphics.sGraphicsManager;
import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class SeeSaw extends Entity
{
    public SeeSaw(iSkin _skin)
    {
        super(_skin);
    }
    @Override
    public void update()
    {
    }
    
    public void render()
    {
        //Vec2 axis = sWorld.translateToWorld(new Vec2(mBody.getPosition().x,mBody.getPosition().y+));
        //sGraphicsManager.rotate(mBody.getAngle()*180.0f/(float)Math.PI);
        Vec2 physPos = mBody.getPosition().clone();
        physPos.x -= 1.0f; //FIXME: offset to compensate 0.5f bug (assumes width = 3)
        Vec2 pos = sWorld.translateToWorld(physPos);
        mSkin.render(pos.x,pos.y); 
        mSkin.setRotation(mBody.getAngle()*180.0f/(float)Math.PI);
    }
}
