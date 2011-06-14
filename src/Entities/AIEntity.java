/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iAIController;
import Graphics.iSkin;
import Physics.sPhysics;
import org.jbox2d.common.Vec2;
/**
 *
 * @author alasdair
 */
public class AIEntity extends Entity {
    
    iAIController mController;
    public AIEntity(iSkin _skin)
    {
        super(_skin);
    }
    public void update()
    {
        
    }
    public void walkLeft()
    {
        mBody.applyImpulse(new Vec2(-0.1f,0), new Vec2(0,0));
    }
    public void walkRight()
    {
        mBody.applyImpulse(new Vec2(0.1f,0), new Vec2(0,0));
    }
    public void jump()
    {
        mBody.applyImpulse(new Vec2(0,-0.1f), new Vec2(0,0));
    }
    public void crouch()
    {
    }
    public void render()
    {
        //int xPixel = (int)(mBody.getPosition().x*64.0f);
        //int yPixel = (int)(mBody.getPosition().y*64.0f);
        Vec2 pixelPosition = sPhysics.translate(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }
}