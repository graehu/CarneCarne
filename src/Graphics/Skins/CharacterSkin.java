/*
 * 
 */
package Graphics.Skins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.PackedSpriteSheet;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Aaron
 */
public class CharacterSkin implements iSkin
{
    List<iSkin> mSkins = new ArrayList<iSkin>();
    HashMap<String, Integer> mSkinNames = new HashMap<String, Integer>();
    Queue<Integer> mCurrentSkins = new PriorityQueue<Integer>();
    List<Vec2> mOffsets = new ArrayList<Vec2>();
    CharacterSkin(String _packedSpriteSheet, List<String> _spriteSheets, int _frameDuration) throws SlickException
    {
        //load packed sprite sheet
        PackedSpriteSheet pss = new PackedSpriteSheet(_packedSpriteSheet, new Color(0,0,0));
        
        //get spritesheets and create skins
        int i = 0;
        for(String string : _spriteSheets)
        {
            SpriteSheet temp = pss.getSpriteSheet(string);
            //if 1x1 - is staticSkin
            if(temp.getHorizontalCount() == 1 && temp.getVerticalCount() == 1)
            {
                mSkins.add(new StaticSkin(temp.getSprite(0, 0)));
                mSkinNames.put(string,i);
            }
            else //is animatedSkin
            {
                mSkins.add(new AnimatedSkin(temp, _frameDuration));
                mSkinNames.put(string,i);
            }
            //initialise offset
            mOffsets.add(new Vec2(0,0));
            i++;
        }
        //if no delcared animations, throw exception
        if(_spriteSheets.isEmpty())
        {
            throw new SlickException("No animations declared");
        }
        //else use first in the list
        else
        {
            startAnim(_spriteSheets.get(0), true, 1.0f);
        }
    }
    public void render(float _x, float _y)
    {
        for(Integer i : mCurrentSkins)
            mSkins.get(i).render(_x + mOffsets.get(i).x, _y + mOffsets.get(i).y);
    }
    
    public void setRotation(float _radians)
    {
        for(iSkin skin : mSkins)
        {
            skin.setRotation(_radians);
        }
    }

    public final float startAnim(String _animation, boolean _isLooping, float _speed) {    
        if(mSkinNames.containsKey(_animation))
        {
            int ref = mSkinNames.get(_animation);
            if(mCurrentSkins.contains(ref))//return if already playing
                return 0;
            iSkin skin = mSkins.get(ref);
            skin.setIsLooping(_isLooping);
            skin.setSpeed(_speed); 
            skin.restart();
            mCurrentSkins.add(ref);
            return skin.getDuration();
        }
        else return 0;
    }

    public final void stopAnim(String _animation) {
        mCurrentSkins.remove(mSkinNames.get(_animation));
    }

    public void restart(String _animation) {
        mSkins.get(mSkinNames.get(_animation)).restart();
    }

    public void setRotation(String _animation, float _radians) {
        Integer ref = mSkinNames.get(_animation);
        mSkins.get(ref).setRotation(_radians);
    }
    
    public void restart() {
        for(iSkin skin : mSkins)
        {
            skin.restart();
        }
    }
    
    public void setDimentions(String _animation, float _w, float _h) {
        mSkins.get(mSkinNames.get(_animation)).setDimentions(_w, _h);
    }
    
    public void setOffset(String _animation, Vec2 _offset) {
        Integer ref = mSkinNames.get(_animation);
        if(ref != null)
            mOffsets.set(ref, _offset);
    }
    public Vec2 getOffset(String _animation) {
        Integer ref = mSkinNames.get(_animation);
        if(ref != null)
            return mOffsets.get(ref);
        else return null;
    }

    public void setIsLooping(boolean _isLooping) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSpeed(float _speed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public float getDuration() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDimentions(float _w, float _h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

}
