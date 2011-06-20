/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author A203946
 */
public class BodyCamera implements iCamera {
    
    Body mBody;
    Vec2 mPosition;
    Vec2 mTranslation;
    public BodyCamera(Body _body)
    {
        mBody = _body;
    }
    public Vec2 translateToWorld(Vec2 _physicsSpace)
    {
        calculatePosition();
        Vec2 worldSpace = new Vec2(_physicsSpace.x*64.0f,_physicsSpace.y*64.0f);
        worldSpace.x -= mPosition.x*64.0f;
        worldSpace.y -= mPosition.y*64.0f;
        worldSpace.x += mTranslation.x;//64.0f;
        worldSpace.y += mTranslation.y;//64.0f;        
        return worldSpace;
    }
    public Vec2 translateToPhysics(Vec2 _worldSpace)
    {
        calculatePosition();
        Vec2 physicsSpace = new Vec2(_worldSpace.x/64.0f,_worldSpace.y/64.0f);
        physicsSpace.x += mPosition.x;
        physicsSpace.y += mPosition.y;
        physicsSpace.x -= mTranslation.x/64.0f;
        physicsSpace.y -= mTranslation.y/64.0f;
        return physicsSpace;
    }
    public Vec2 getPixelTranslation()
    {
        calculatePosition();
        return new Vec2(mTranslation.x+(mPosition.x*-64.0f),mTranslation.y+(mPosition.y*-64.0f));
    }
    //FIXME change constants (300,400) to half resolution hight and width respectively
    private void calculatePosition()
    {
        mPosition = mBody.getPosition();
        mPosition = mPosition.add(new Vec2(1.0f,1.0f));
        mTranslation = new Vec2((400.0f/64.0f)+1.0f,(300.0f/64.0f)+1.0f);
        if (mPosition.x < mTranslation.x)
        {
            mTranslation.x -= ((mTranslation.x)-mPosition.x);
        }
        if (mPosition.y < mTranslation.y)
        {
            mTranslation.y -= ((mTranslation.y)-mPosition.y);
        }        
        mTranslation = mTranslation.mul(64.0f);
    }
}
