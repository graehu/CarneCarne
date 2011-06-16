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
    public BodyCamera(Body _body)
    {
        mBody = _body;
    }
    //FIXME change constants (300,400) to half resolution hight and width respectively
    public Vec2 translateToWorld(Vec2 _physicsSpace)
    {
        Vec2 worldSpace = new Vec2(_physicsSpace.x*64.0f,_physicsSpace.y*64.0f);
        worldSpace.x -= mBody.getPosition().x*64.0f;
        worldSpace.y -= mBody.getPosition().y*64.0f;
        worldSpace.x += 400.0f;//64.0f;
        worldSpace.y += 300.0f;//64.0f;        
        return worldSpace;
    }
    public Vec2 translateToPhysics(Vec2 _worldSpace)
    {
        Vec2 physicsSpace = new Vec2(_worldSpace.x/64.0f,_worldSpace.y/64.0f);
        physicsSpace.x += mBody.getPosition().x;
        physicsSpace.y += mBody.getPosition().y;
        physicsSpace.x -= 400.0f/64.0f;
        physicsSpace.y -= 300.0f/64.0f;
        return physicsSpace;
    }
    public Vec2 getPixelTranslation()
    {
        return new Vec2(400.0f+(mBody.getPosition().x*-64.0f),300.0f+(mBody.getPosition().y*-64.0f));
    }
}
