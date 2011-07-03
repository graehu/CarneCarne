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
            Vec2 playerPixelPosition = mPlayer.mBody.getPosition().mul(64);
            Vec2 reticlePos = playerPixelPosition.add(mCurrentDir.mul(mDistance));
            mReticleSprite.setPosition(reticlePos); //20,20 offset to center sprite
            if(mDistance == mOffset)
            {
                //Vec2 pixelPosition = sWorld.translateToWorld(new Vec2(0,0));
               // sInput.setCursorPos(pixelPosition.sub(reticlePos).sub(mCurrentDir.mul(20)));
            }
    }
    public void updateDirection(Vec2 dir)
    {
        mCurrentDir = dir;
        mCurrentDir.normalize();
        mDistance = mOffset;
    }
    public void setWorldPosition(Vec2 _pos)
    {
        mCurrentDir = _pos.sub(sWorld.translateToWorld(mPlayer.mBody.getPosition())).sub(new Vec2(32,32));
        mDistance = mCurrentDir.normalize();
        mDistance = Math.min(mDistance,mOffset);
    }
    public void render()
    {
        Vec2 pixelPosition = sWorld.getPixelTranslation();
        mReticleSprite.render(pixelPosition.x,pixelPosition.y);
    }
    
}
