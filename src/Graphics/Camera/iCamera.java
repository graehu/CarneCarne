/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Camera;

import ShaderUtils.LightingShader;
import ShaderUtils.Shader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

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
    final public void render(Graphics _graphics)
    {
        //render camera
        renderInternal(_graphics);
        
        //render HUD
        renderInternalHUD(_graphics);
        
    }
    abstract protected void renderInternal(Graphics _graphics);
    protected void renderInternalHUD(Graphics _graphics) {}
    abstract public void update(Graphics _graphics);
    public void resize(Rectangle _viewPort)
    {
        mViewPort = _viewPort;
    }
    public iCamera addPlayer(Body _body)
    {
        return this;
    }
}
