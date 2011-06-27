/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iPlatformController;
import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class MovingPlatform extends Entity
{
    iPlatformController mController;
    public MovingPlatform(iSkin _skin, iPlatformController _controller)
    {
        super(_skin);
        mController = _controller;
        mController.setPlatform(this);
    }
    
    public void update()
    {
        mController.update();
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
