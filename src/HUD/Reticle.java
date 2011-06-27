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
    private float mOffset = 100;
    public Reticle(AIEntity _player)
    {
        mPlayer = _player;
        HashMap params = new HashMap();
        params.put("ref", "CrossHair2");
        mReticleSprite = sSpriteFactory.create("simple", params, false);
    }
    public void updateDirection(Vec2 dir)
    {
        mCurrentDir = dir;
        mCurrentDir.normalize();
        Vec2 wBodyPos = mPlayer.mBody.getPosition().mul(64);
        mReticleSprite.setPosition(wBodyPos.add(mCurrentDir.mul(mOffset))); //20,20 offset to center sprite
    }
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(new Vec2(0,0));
        mReticleSprite.render(pixelPosition.x,pixelPosition.y);
    }
    
}
