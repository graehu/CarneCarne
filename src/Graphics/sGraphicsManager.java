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
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 *
 * @author aaron
 */
public class sGraphicsManager {
    //this class should manage the rendering of all objects 
    //provide a function for skins to add themselves to the render lists which are sorted by spritesheet

    protected static Vec2 mTrueScreenDimensions = new Vec2(0,0);
    protected static Vec2 mScreenDimensions = mTrueScreenDimensions;
    private static boolean mIsInit = false;
    private static boolean mAllowTransform = false;
    
    public static Vec2 getScreenDimensions()
    {
        return mScreenDimensions.clone();
    }
    public static Vec2 getTrueScreenDimensions()
    {
        return mTrueScreenDimensions.clone();
    }
    public static void setTrueScreenDimensions(Vec2 _screenDimensions)
    {
        mTrueScreenDimensions = _screenDimensions;
    }
        
    public static void setScreenDimensions(Vec2 _screenDimensions)
    {
        mScreenDimensions = _screenDimensions;
    }
    

    private static HashMap<String, SpriteSheet> mSpriteSheets = new HashMap<String, SpriteSheet>();
    private static HashMap<String, Set<iSkin>> mRenderLists = new HashMap<String, Set<iSkin>>();
    private static ArrayList<iSprite> mManagedSprites = new ArrayList<iSprite>();
    private static Graphics mGraphics;
  
    private sGraphicsManager()
    {
    }
    
    static public void init(int _sx, int _sy) throws SlickException
    {
        mTrueScreenDimensions.x = _sx;
        mTrueScreenDimensions.y = _sy;
    }
    public static void setGraphics(Graphics _graphics)
    {
        mGraphics = _graphics;
    }
    public static void beginTransform()
    {
        mAllowTransform = true;
        mGraphics.pushTransform();
    }
    public static void endTransform()
    {
        mAllowTransform = false;
        mGraphics.popTransform();
    }
    private static Rectangle mClip;
    public static void setClip(Rectangle _rect)
    {
        mGraphics.setClip(_rect);
        mClip = _rect;
    }
    public static Rectangle getClip()
    {
        return mClip;
    }
    public static void translate(float _x, float _y)
    {
        if(mAllowTransform)
            mGraphics.translate(_x, _y);
        //else
            //throw new SlickException("Must call beginTrasform first");
    }
    public static void rotate(float _x, float _y, float _angle)
    {
        if(mAllowTransform)
            mGraphics.rotate(_x, _y, _angle);
        //else
            //throw new SlickException("Must call beginTrasform first");
    }
    public static void fill(Shape shape, ShapeFill fill)
    {
        mGraphics.fill(shape, fill);
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
