/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Camera;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author alasdair
 */
public abstract class iCamera {
    
    protected Rectangle mViewPort;
    iCamera(Rectangle _viewPort)
    {
        mViewPort = _viewPort;
    }
    abstract public Vec2 translateToWorld(Vec2 _physicsSpace);
    abstract public Vec2 translateToPhysics(Vec2 _worldSpace);
    abstract public Vec2 getPixelTranslation();
    abstract public void render();
    abstract public void update();
    public void resize(Rectangle _viewPort)
    {
        mViewPort = _viewPort;
    }
    public iCamera addPlayer(Body _body)
    {
        return this;
    }
}
