/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import java.util.HashMap;
import org.newdawn.slick.Animation;
import org.newdawn.slick.PackedSpriteSheet;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Aaron
 */
public class AnimatedSkin implements iSkin{
    HashMap<String, Animation> mAnimations;
    Animation mCurrentAnim;
    PackedSpriteSheet mPackedSpriteSheet;
    
    //constructor public to graphics package only
    AnimatedSkin(String _packedSpriteSheet, int _duration) throws SlickException
    {
        mAnimations = new HashMap<String, Animation>();
        mPackedSpriteSheet = new PackedSpriteSheet(_packedSpriteSheet);
        //set default animation !!!this needs to be called default in every pack!!!
        Animation newAnim = new Animation(mPackedSpriteSheet.getSpriteSheet("wlk"), 41); //~24fps
        mAnimations.put("wlk", newAnim);
        mCurrentAnim = newAnim;
        mCurrentAnim.restart();
    }
    public void render(float _x, float _y)
    {
        //add to render list under given sprite sheet (batch rending)
        mCurrentAnim.draw(_x, _y); //quick fix for render
    }
    public void render(float _x, float _y, float _w, float _h)
    {
        mCurrentAnim.draw(_x, _y, _w, _h);
    }
    //internal graphics function for batch rendering
    void renderInUse(float _x, float _y)
    {
        //mAnimation.renderInUse(_x, _y);
    }
    //sets animation to play and returns the expected duration
    int setAnimation(String anim)
    {
        if(mAnimations.containsKey(anim))
        {
            mCurrentAnim = mAnimations.get(anim);
            mCurrentAnim.restart();
        }
        else
        {
            SpriteSheet ss = mPackedSpriteSheet.getSpriteSheet(anim);
            Animation newAnim = new Animation(ss, 41); //~24fps
            mAnimations.put(anim, newAnim);
            mCurrentAnim = newAnim;
            mCurrentAnim.restart();
        }
        return mCurrentAnim.getFrameCount()*mCurrentAnim.getDuration(0);
    }
    
}
