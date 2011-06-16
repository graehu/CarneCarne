/*
 * 
 */
package Graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
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
            startAnim(_spriteSheets.get(0));
        }
    }
    public void render(float _x, float _y)
    {
        for(Integer i : mCurrentSkins)
            mSkins.get(i).render(_x, _y);
    }
    public void render(float _x, float _y, float _w, float _h)
    {
        for(Integer i : mCurrentSkins)
            mSkins.get(i).render(_x, _y, _w, _h);
    }
    public void setRotation(float _radians)
    {
        for(iSkin skin : mSkins)
        {
            skin.setRotation(_radians);
        }
    }

    public final void startAnim(String _animation) {
        mCurrentSkins.add(mSkinNames.get(_animation));
    }

    public final void stopAnim(String _animation) {
        mCurrentSkins.remove(mSkinNames.get(_animation));
    }

    public void restart(String _animation) {
        mSkins.get(mSkinNames.get(_animation)).restart();
    }

    public void setRotation(String _animation, float _radians) {
        mSkins.get(mSkinNames.get(_animation)).setRotation(_radians);
    }
    
    public void restart() {
        for(iSkin skin : mSkins)
        {
            skin.restart();
        }
    }
}
