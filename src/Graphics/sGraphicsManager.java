/*
 * This abstract class is the root of all evil (read: graphics implementations)
 *
 */

package Graphics;

import java.util.HashMap;
import java.util.LinkedList;
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
    protected static Vec2 mTrueScreenDimensions = new Vec2(0,0);
    protected static Vec2 mScreenDimensions = mTrueScreenDimensions;
    public static Vec2 getScreenDimensions()
    {
        return mScreenDimensions.clone();
    }
    static Vec2 getTrueScreenDimensions()
    {
        return mTrueScreenDimensions.clone();
    }
    static void setScreenDimensions(Vec2 _screenDimensions)
    {
        mScreenDimensions = _screenDimensions;
    }

    private static HashMap<String, SpriteSheet> mSpriteSheets = new HashMap<String, SpriteSheet>();
    private static HashMap<String, Set<iSkin>> mRenderLists = new HashMap<String, Set<iSkin>>();
    private static LinkedList<iSprite> mSprites = new LinkedList<iSprite>();
    
    private sGraphicsManager()
    {
        
    }
    private static boolean mIsInit = false;
    static public void init(int _sx, int _sy) throws SlickException //should be called by sSkinFactory init
    {
    if(mIsInit == false) //only allow initialisation once
    {
        mIsInit = true;
        //initialise for all spritesheets (could do this from file perhaps)
        mTrueScreenDimensions.x = _sx;
        mTrueScreenDimensions.y = _sy;
    }
    //initialise for all spritesheets (could do this from file perhaps)
    mTrueScreenDimensions.x = _sx;
    mTrueScreenDimensions.y = _sy;
    }
    
    public void addSprite(iSprite _sprite)
    {
        mSprites.add(_sprite);
    }
    
    static void batchRender(iSkin _skin)
    {
        
    }

    
}
