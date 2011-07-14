/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iPlatformController;
import Graphics.Skins.iSkin;
import Graphics.sGraphicsManager;
import Level.Tile;
import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class CaveIn extends Entity
{
    iPlatformController mPlatformController;
    int mWidth, mHeight;
    CaveIn(iSkin _skin, int _width, int _height)
    {
        super(_skin);
        mPlatformController = null;
        mWidth = _width;
        mHeight = _height;
    }
    
    public void setPlatformController (iPlatformController _controller)
    {
        mPlatformController = _controller;
    }
    @Override
    public void update()
    {
        if (mPlatformController != null)
        {
            mPlatformController.update();
        }
        ((Tile)mBody.getFixtureList().getUserData()).updateSystems();
    }
    @Override
    public void render()
    { 
        sGraphicsManager.beginTransform();
            Vec2 axis = sWorld.translateToWorld(new Vec2(mBody.getPosition().x+0.5f,mBody.getPosition().y+0.5f));
            sGraphicsManager.rotate(axis.x, axis.y, mBody.getAngle()*180.0f/(float)Math.PI);
            mSkin.render(mBody.getPosition().x,mBody.getPosition().y);
        sGraphicsManager.endTransform();
    }
    
    public int getWidth()
    {
        return mWidth;
    }
    public int getHeight()
    {
        return mHeight;
    }
    
}
