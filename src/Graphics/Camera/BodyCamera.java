/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Camera;

import AI.PlayerInputController;
import Entities.AIEntity;
import Entities.PlayerEntity;
import Events.CaveInEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Particles.sParticleManager;
import Graphics.sGraphicsManager;
import Level.sLevel;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.Color;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author alasdair
 */
public class BodyCamera extends iCamera implements iEventListener{
    
    Body mBody;
    Vec2 mPosition = new Vec2(0,0);
    Vec2 mTranslation = new Vec2(0,0);
    boolean mLookDirection;
    Vec2 mLookOffset;
    float mXLookVelocity;
    int mLookChangeTimer;
    Vec2 mShake = new Vec2(0,0);
    boolean mTopSplit;
    float mTimer;
    CaveInEvent mCaveInEvent;
    CameraGrabber mGrabber = null;
    public BodyCamera(Body _body, Rectangle _viewPort, boolean _topSplit)
    {
        super(_viewPort);
        mBody = _body;
        ((PlayerEntity)_body.getUserData()).setClip(_viewPort);
        mTopSplit = _topSplit;
        mTimer = 0;
        mLookChangeTimer = 0;
        mLookOffset = new Vec2(0,0);
        mXLookVelocity = 0.0f;
        mLookDirection = false;
        sEvents.subscribeToEvent("CaveInEvent", this);
        mGrabber = new CameraGrabber(new Vec2(34,11));
        
    }
    private static final float directionEpsilon = 0.6f;
    private static final float maxLookOffset = 0.05f;
    private static final float offsetMoveVelocity = 0.002f;
    private void calculateLookOffset()
    {
        mLookOffset.y = 0;
        float v = mBody.getLinearVelocity().x;
        v = ((PlayerInputController)((AIEntity)mBody.getUserData()).mController).mPlayerDir.x;
        boolean oldLookDirection = mLookDirection;
        float widthScale = mViewPort.getWidth()/1680.0f;
        if (v > 0.0f)
        {
            if (v > directionEpsilon)
            {
                mLookDirection = true;
            }
        }
        else
        {
            if (v < -directionEpsilon)
            {
                mLookDirection = false;
            }
        }
        if (oldLookDirection != mLookDirection)
        {
            mLookChangeTimer++;
            if (mLookChangeTimer < 60)
            {
                mLookDirection = !mLookDirection;
            }
        }
        else mLookChangeTimer = 0;
        if (mLookDirection)
        {
            if (mLookOffset.x > -maxLookOffset)
            {
                mXLookVelocity -= offsetMoveVelocity;
            }
            else mXLookVelocity *= 0.99f;
        }
        else
        {
            if (mLookOffset.x < maxLookOffset)
            {
                mXLookVelocity += offsetMoveVelocity;
            }
            else mXLookVelocity *= 0.99f;
        }
        mXLookVelocity *= 0.99f;
        mLookOffset.x += mXLookVelocity * widthScale;
        /*if (mGrabber != null)
        {
            mLookOffset = mGrabber.getOffset(mPosition);
            mGrabber = mGrabber.update();
        }*/
    }

    @Override
    public void resize(Rectangle _viewPort) 
    {
        super.resize(_viewPort);
        ((PlayerEntity)mBody.getUserData()).setClip(_viewPort);
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
        mTimer += 1.0f;
        /*if (mCaveInEvent != null)
        {
            float scale = mCaveInEvent.getScale(mBody.getPosition());
            if (scale != 0.0f)
            {
                mShake.x = scale*(float)Math.sin(mTimer*8.0f);
                mShake.y = scale*(float)Math.sin(mTimer*5.0f);
            }
            else
            {
                mCaveInEvent = null;
            }
        }*/
    }
    public void render()
    {
        sGraphicsManager.beginTransform();
            sGraphicsManager.translate(mViewPort.getX(),mViewPort.getY());
            sGraphicsManager.setClip(mViewPort);
            calculatePosition();
            ShapeFill fill = new GradientFill(new Vector2f(0,0), new Color(159,111,89), new Vector2f(mViewPort.getMaxX(),mViewPort.getMaxY()), new Color(186, 160, 149), false);
            Rectangle shape = new Rectangle(0,0, mViewPort.getWidth(),mViewPort.getHeight());
            sGraphicsManager.fill(shape, fill);
            sLevel.renderBackground();
            sWorld.render();
            sGraphicsManager.renderManagedSprites();
            sLevel.renderForeground();
            sParticleManager.render((int)getPixelTranslation().x, (int)getPixelTranslation().y, (int)mViewPort.getWidth(), (int)mViewPort.getHeight(),0);
            ((PlayerEntity)mBody.getUserData()).renderHUD();
        sGraphicsManager.endTransform();      
    }
    
    @Override
    public iCamera addPlayer(Body _body)
    {
        return new SplitScreenCamera(mBody, _body, mViewPort, mTopSplit);
    }
    protected void calculatePosition()
    {
        mPosition = mBody.getPosition();
        mPosition = mPosition.add(new Vec2(0.5f,0.5f));
        calculateLookOffset();
        Vec2 s = sGraphicsManager.getTrueScreenDimensions();
        s.x = (mViewPort.getMaxX()- mViewPort.getX());
        s.y = (mViewPort.getMaxY()- mViewPort.getY());
        mTranslation = new Vec2(( (s.x/2)/64.0f), ((s.y/2)/64.0f));
        mTranslation = mTranslation.add(mLookOffset);
        if (mPosition.x < mTranslation.x)
        {
            mTranslation.x -= ((mTranslation.x)-mPosition.x);
        }
        if (mPosition.y < mTranslation.y)
        {
            mTranslation.y -= ((mTranslation.y)-mPosition.y);
        }        
        mTranslation = mTranslation.mul(64.0f);
        mTranslation = mTranslation.add(mShake);
        sGraphicsManager.setScreenDimensions(s);
    }
    
    public void trigger(iEvent _event)
    {
        mCaveInEvent = (CaveInEvent)_event;
    }
}