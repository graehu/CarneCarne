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
    public Vec2 translate(Vec2 _physicsSpace)
    {
        Vec2 ret = new Vec2(400.0f+((_physicsSpace.x-mBody.getPosition().x)*64.0f), 300.0f+((_physicsSpace.y-mBody.getPosition().y)*64.0f));
        //ret.mul(64.0f);
        return ret;
    }
    public Vec2 getPixelTranslation()
    {
        return new Vec2(400.0f+(mBody.getPosition().x*-64.0f),300.0f+(mBody.getPosition().y*-64.0f));
    }
}
