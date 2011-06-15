/*
 * 
 */
package Graphics;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
    LinkedHashMap<String, iSkin> mSkins; //needs to maintain order for rendering
    HashMap<String, iSkin> mCurrentSkins;
    CharacterSkin(String _packedSpriteSheet, List<String> _spriteSheets, int _frameDuration) throws SlickException
    {
        mSkins = new LinkedHashMap<String, iSkin>();
        mCurrentSkins = new HashMap<String, iSkin>();
        //load packed sprite sheet
        PackedSpriteSheet pss = new PackedSpriteSheet(_packedSpriteSheet, new Color(0,0,0));
        
        //get spritesheets and create skins
        for(String string : _spriteSheets)
        {
            SpriteSheet temp = pss.getSpriteSheet(string);
            //if 1x1 - is staticSkin
            if(temp.getHorizontalCount() == 1 && temp.getVerticalCount() == 1)
            {
                mSkins.put(string, new StaticSkin(temp.getSprite(0, 0)));
            }
            else //is animatedSkin
            {
                mSkins.put(string, new AnimatedSkin(temp, _frameDuration));
            }
        }
        //if no delcared animations, throw exception
        if(_spriteSheets.isEmpty())
        {
            throw new SlickException("No animations declared");
        }
        //else use first in the list
        else
        {
            mCurrentSkins.put(_spriteSheets.get(0),mSkins.get(_spriteSheets.get(0)));
        }
    }
    public void render(float _x, float _y)
    {
        for(iSkin skin : mCurrentSkins.values())
            skin.render(_x, _y);
    }
    public void render(float _x, float _y, float _w, float _h)
    {
        for(iSkin skin : mCurrentSkins.values())
            skin.render(_x, _y, _w, _h);
    }
    public void setRotation(float _radians)
    {
        for(iSkin skin : mCurrentSkins.values())
        {
            skin.setRotation(_radians);
        }
    }

    public void startAnim(String _animation) {
        mCurrentSkins.put(_animation,mSkins.get(_animation));
    }

    public void stopAnim(String _animation) {
        mCurrentSkins.remove(_animation);
    }

    public void restart(String _animation) {
        mCurrentSkins.get(_animation).restart();
    }

    public void setRotation(String _animation, float _radians) {
        mCurrentSkins.get(_animation).setRotation(_radians);
    }
    
    public void restart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
