/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import org.jbox2d.dynamics.Body;
import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.SlickException;
/**
 *
 * @author alasdair
 */
abstract public class Entity {
    
    public Body mBody;
    public iSkin mSkin;
    protected int mWaterHeight;
    protected int mWaterTiles;
    protected int mTar;
    
    public Entity(iSkin _skin)
    {
        mSkin = _skin;
        mWaterHeight = 0;
        mWaterTiles = 0;
    }
    
    abstract public void update();
    
    public void render() throws SlickException
    {
        Vec2 pixelPosition = sWorld.translateToWorld(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }
    
    public void submerge(int _height)
    {
        mWaterHeight = _height;
        mWaterTiles++;
    }
    public void unsubmerge()
    {
        mWaterTiles--;
    }
    
    public void tar()
    {
        mTar++;
    }
    public void untar()
    {
        mTar--;
    }

    public void kill()
    {
    }
}
