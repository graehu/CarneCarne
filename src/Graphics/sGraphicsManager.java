/*
 * This abstract class is the root of all evil (read: graphics implementations)
 *
 */

package Graphics;

import Events.WindowResizeEvent;
import Events.sEvents;
import Graphics.Sprites.iSprite;
import Utils.sFontLoader;
import World.sWorld;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 *
 * @author aaron
 */
public class sGraphicsManager {

    private static Vec2 mNativeScreenDimentions = new Vec2(0,0);
    private static Vec2 mScreenDimensions = new Vec2(800,600);
    private static Rectangle mClip;
    private static boolean mAllowTransform = false;
    private static boolean mIsFullScreen = false;
    private static DisplayMode mLastDisplayMode = null;
    
    private static ArrayList<iSprite> mManagedSprites = new ArrayList<iSprite>();
    private static AppGameContainer mGameContainer;
    
    public static Vec2 RelativeToAbsoluteScreen(Vec2 _rel)
    {
         return new Vec2(Display.getDisplayMode().getWidth() * _rel.x, Display.getDisplayMode().getHeight() * _rel.y);
    }
    public static Vec2 getScreenDimensions()
    {
        return mScreenDimensions.clone();
    }
    public static Vec2 getTrueScreenDimensions()
    {
        return new Vec2(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
    }        
    public static void setScreenDimensions(Vec2 _screenDimensions)
    {
        mScreenDimensions = _screenDimensions.clone();
    }
  
    private sGraphicsManager()
    {
    }
    public static void onResize()
    {
        sEvents.triggerEvent(new WindowResizeEvent(new Vec2(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight())));
    }
    static public void init(AppGameContainer _gc) throws SlickException
    {
        mGameContainer = _gc;
        mLastDisplayMode = Display.getDisplayMode();
        setScreenDimensions(new Vec2(mLastDisplayMode.getWidth(), mLastDisplayMode.getHeight()));
        mNativeScreenDimentions.x = Display.getDesktopDisplayMode().getWidth();
        mNativeScreenDimentions.y = Display.getDesktopDisplayMode().getHeight();
        sFontLoader.setDefaultFont("default");
    }
    public static void beginTransform()
    {
        mAllowTransform = true;
        mGameContainer.getGraphics().pushTransform();
    }
    public static void endTransform()
    {
        mAllowTransform = false;
        mGameContainer.getGraphics().popTransform();
        mGameContainer.getGraphics().setClip(0,0,(int)mNativeScreenDimentions.x,(int)mNativeScreenDimentions.y);
        //mGameContainer.getGraphics().clearClip();                
    }
    public static void setClip(Rectangle _rect)
    {
        mGameContainer.getGraphics().setClip(_rect);
        mClip = _rect;
    }
    public static Rectangle getClip()
    {
        return mClip;
    }
    public static void translate(float _x, float _y)
    {
        if(mAllowTransform)
            mGameContainer.getGraphics().translate(_x, _y);
    }
    public static void rotate(float _x, float _y, float _angle)
    {
        if(mAllowTransform)
            mGameContainer.getGraphics().rotate(_x, _y, _angle);
    }
    public static void fill(Shape shape, ShapeFill fill)
    {
        mGameContainer.getGraphics().fill(shape, fill);
    }
    public static void addSprite(iSprite _sprite)
    {
        mManagedSprites.add(_sprite);
    }
    
    public static void renderManagedSprites()
    {
        Vec2 worldTrans = sWorld.getPixelTranslation();
        iSprite sprite;
        for(int i = 0; i < mManagedSprites.size(); i++)
        {
            sprite = mManagedSprites.get(i);
            sprite.render(worldTrans.x, worldTrans.y);
            //FIXME: check is dead and remove
        }
    }
    
    /*
     * Draw string in current view port
     * _x: X position in screen space 0.0f-1.0f
     * _y: Y position in screen space 0.0f-1.0f
     */
    public static void drawString(String _str, float _x, float _y)
    {
        drawString(_str, _x, _y, Color.white);
    }
    public static void drawString(String _str, float _x, float _y, Color _color)
    {
        float xPos = (mScreenDimensions.x * _x);
        float yPos = (mScreenDimensions.y * _y);
        Color temp = mGameContainer.getGraphics().getColor();
        mGameContainer.getGraphics().setColor(_color);
        mGameContainer.getGraphics().drawString(_str, xPos, yPos);
        mGameContainer.getGraphics().setColor(temp);
    }

    public static void toggleFullscreen()
    {
        try 
        {
            mIsFullScreen = !mIsFullScreen;
            if(mIsFullScreen)
            {
                mLastDisplayMode = Display.getDisplayMode();
                mGameContainer.setDisplayMode((int)mNativeScreenDimentions.x, (int)mNativeScreenDimentions.y, true);
            }
            else
            {
                mGameContainer.setDisplayMode(mLastDisplayMode.getWidth(), mLastDisplayMode.getHeight(), false);
            }
        }
        catch(SlickException ex){Logger.getLogger(sGraphicsManager.class.getName()).log(Level.SEVERE, null, ex);}
        onResize();
    }
}
