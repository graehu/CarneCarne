/*
 * This abstract class is the root of all evil (read: graphics implementations)
 *
 */

package Graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author aaron
 */
public class sGraphicsManager {
    //this class should manage the rendering of all objects 
    //provide a function for skins to add themselves to the render lists which are sorted by spritesheet
    
    private static HashMap<String, SpriteSheet> mSpriteSheets;
    private static HashMap<String, Set<iSkin>> mRenderLists;
    
    private sGraphicsManager()
    {
        
    }
    
    public void init() throws SlickException //should be called by sSkinFactory init
    {
        mRenderLists = new HashMap<String, Set<iSkin>>();
        mSpriteSheets = new HashMap<String, SpriteSheet>();
        //initialise for all spritesheets (could do this from file perhaps)
        mSpriteSheets.put("char1", new SpriteSheet("data/char1.png", 64, 64));
        mRenderLists.put("char1", new LinkedHashSet<iSkin>());
        mSpriteSheets.put("AnimTest", new SpriteSheet("data/sprites.png", 32,32));
        mRenderLists.put("AnimTest", new LinkedHashSet<iSkin>());
    }
    
    static void batchRender(iSkin _skin)
    {
        
    }

    
}
