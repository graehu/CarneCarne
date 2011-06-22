/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Level.sLevel;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author alasdair
 */
public class BodyCamera extends iCamera {
    
    Body mBody;
    Vec2 mPosition;
    Vec2 mTranslation;
    boolean mTopSplit;
    public BodyCamera(Body _body, Rectangle _viewPort, boolean _topSplit)
    {
        super(_viewPort);
        mBody = _body;
        mTopSplit = _topSplit;
    }
    public Vec2 translateToWorld(Vec2 _physicsSpace)
    {
        Vec2 worldSpace = new Vec2(_physicsSpace.x*64.0f,_physicsSpace.y*64.0f);
        worldSpace.x -= mPosition.x*64.0f;
        worldSpace.y -= mPosition.y*64.0f;
        worldSpace.x += mTranslation.x;//64.0f;
        worldSpace.y += mTranslation.y;//64.0f;        
        return worldSpace;
    }
    public Vec2 translateToPhysics(Vec2 _worldSpace)
    {
        Vec2 physicsSpace = new Vec2(_worldSpace.x/64.0f,_worldSpace.y/64.0f);
        physicsSpace.x += mPosition.x;
        physicsSpace.y += mPosition.y;
        physicsSpace.x -= mTranslation.x/64.0f;
        physicsSpace.y -= mTranslation.y/64.0f;
        return physicsSpace;
    }
    public Vec2 getPixelTranslation()
    {
        return new Vec2(mTranslation.x+(mPosition.x*-64.0f),mTranslation.y+(mPosition.y*-64.0f));
    }
    public void update()
    {
        calculatePosition();
    }
    public void render(Graphics _graphics)
    {
        _graphics.resetTransform();
        _graphics.translate(mViewPort.getX(),mViewPort.getY());
        _graphics.setClip(mViewPort);
        sLevel.renderBackground();
        
        sWorld.render();
        
        sLevel.renderForeground();
        
    }
    
    public iCamera addPlayer(Body _body)
    {
        return new SplitScreenCamera(mBody, _body, mViewPort, mTopSplit);
    }
    protected void calculatePosition()
    {
        mPosition = mBody.getPosition();
        mPosition = mPosition.add(new Vec2(1.0f,1.0f));
        Vec2 s = sGraphicsManager.getScreenDimensions();
        mTranslation = new Vec2(( (s.x/2)/64.0f) + 1.0f, ((s.y/2)/64.0f) + 1.0f);
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
