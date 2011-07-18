/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package HUD;

import Entities.AIEntity;
import Graphics.Sprites.iSprite;
import Graphics.Sprites.sSpriteFactory;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author 
 */
public class Reticle {
    private iSprite mReticleSprite = null;
    private AIEntity mPlayer = null;
    private Vec2 mCurrentDir = new Vec2(1,0);
    private Vec2 mLastPosition = new Vec2(0,0);
    private float mOffset = 200;
    private float mDistance = 200;
    private boolean mByPosition = false;
    public Reticle(AIEntity _player)
    {
        mPlayer = _player;
        HashMap params = new HashMap();
        params.put("ref", "CrossHair2");
        mReticleSprite = sSpriteFactory.create("simple", params, false);
        //mLastPosition = 
    }
    public void update()
    {
            if(mByPosition)
            {
                //translate player to screen space and offset to centre
                Vec2 playerPos = mPlayer.getBody().getPosition().mul(64).add(sWorld.getPixelTranslation());
                mCurrentDir = mLastPosition.sub(playerPos);
                mDistance = mCurrentDir.normalize();
                mReticleSprite.setPosition(mLastPosition);
            }
            else
            {
                Vec2 playerPixelPosition = mPlayer.getBody().getPosition().mul(64);
                Vec2 reticlePos = playerPixelPosition.add(mCurrentDir.mul(mDistance));
                mReticleSprite.setPosition(reticlePos); //20,20 offset to center sprite
            }
    }
    public void updateDirection(Vec2 dir)
    {
        mCurrentDir = dir;
        mCurrentDir.normalize();
        mDistance = mOffset;
        mByPosition = false;
    }
    public void setWorldPosition(Vec2 _pos)
    {
        mCurrentDir = _pos.sub(sWorld.translateToWorld(mPlayer.getBody().getPosition()));
        mDistance = mCurrentDir.normalize();
        //mDistance = Math.min(mDistance,mOffset);
        mByPosition = true;
    }
    public void setScreenPosition(Vec2 _pos)
    {
        mLastPosition = _pos;
        mByPosition = true;
    }
    
    public Vec2 getPlayerDirection()
    {
        return mCurrentDir.clone();
    }
    public void render()
    {
        if(mByPosition)
        {
            mReticleSprite.render(0, 0);
        }
        else
        {
            Vec2 pixelPosition = sWorld.getPixelTranslation();
            mReticleSprite.render(pixelPosition.x,pixelPosition.y);
        }
    }
    
}
