/*
 * This abstract class is the root of all evil (read: graphics implementations)
 *
 */

package Graphics;

import Graphics.Skins.iSkin;
import Graphics.Sprites.iSprite;
import World.sWorld;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author aaron
 */
public class sGraphicsManager {
    //this class should manage the rendering of all objects 
    //provide a function for skins to add themselves to the render lists which are sorted by spritesheet
    private static boolean mIsInit = false;
    private static Vec2 mScreenDimensions = new Vec2(0,0);
    public static Vec2 getScreenDimensions()
    {
        return mScreenDimensions.clone();
    }
    //returns old dimensions
    public static Vec2 setScreenDimensions(Vec2 _s)
    {
        Vec2 temp = mScreenDimensions;
        mScreenDimensions = _s;
        return temp;
    }
    

    private static HashMap<String, SpriteSheet> mSpriteSheets = new HashMap<String, SpriteSheet>();
    private static HashMap<String, Set<iSkin>> mRenderLists = new HashMap<String, Set<iSkin>>();
    private static ArrayList<iSprite> mManagedSprites = new ArrayList<iSprite>();
          
    private sGraphicsManager()
    {
        
    }
    
    static public void init(int _sx, int _sy) throws SlickException //should be called by sSkinFactory init
    {
        if(mIsInit == false) //only allow initialisation once
        {
            mIsInit = true;
            //initialise for all spritesheets (could do this from file perhaps)
            mScreenDimensions.x = _sx;
            mScreenDimensions.y = _sy;
        }
    }
    
    public static void addSprite(iSprite _sprite)
    {
        mManagedSprites.add(_sprite);
    }
    
    public static void renderManagedSprites()
    {
        Vec2 worldTrans = sWorld.translateToWorld(new Vec2(0,0));
        iSprite sprite;
        for(int i = 0; i < mManagedSprites.size(); i++)
        {
            sprite = mManagedSprites.get(i);
            sprite.render(worldTrans.x, worldTrans.y);
            //skin.getIsDead
        }
    }

    
}
