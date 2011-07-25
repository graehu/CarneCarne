/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Graphics.sGraphicsManager;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
/**
 *
 * @author alasdair
 */
public class sLevel
{
    public static void placeTile(int _x, int _y, int _rootId)
    {
        mLevelEditor.placeTile(_x, _y, _rootId);
    }
    public static void placeTileNoBody(int _x, int _y, int _rootId)
    {
        mLevelEditor.placeTileNoBody(_x, _y, _rootId);
    }

    static TileGrid getTileGrid()
    {
        return mLevelEditor.tileGrid;
    }

    
    public static enum TileType
    {
        eEmpty,
        eEdible,
        eSwingable,
        eIndestructible,
        eWater,
        eAcid,
        eIce,
        eBouncy,
        eGum,
        eTar,
        eChilli,
        eBurningTar,
        eMelonFlesh,
        eMelonSkin,
        eSpikes,
        eTileTypesMax,
    }
    public static TileType getTileType(int _id)
    {
        return mLevelEditor.getTileType(_id);
    }
    private static float mParralaxXScale[];
    private static float mParralaxYScale[];
    private static AnimatedTiledMap mTiledMap;
    private static LevelEditor mLevelEditor;
    private static int flagsLayer; /// Ignore this one
    private static int midLayer; 
    private static int xTiles, yTiles;
    public enum PathInfo
    {
        eNotPassable,
        eAir,
        eLeftSlope,
        eRightSlope,
        ePathable
    };
    
    public static int getTileSizeInMetres()
    {
        return 1; //just incase we want to make tiles smaller than a meter.
    }
    
    public static PathInfo getPathInfo(int _xTile, int _yTile)
    {
        TileType type = mLevelEditor.getTileType(_xTile,_yTile);
        if (type == TileType.eEmpty)
        {
            return PathInfo.eAir;
        }
        else return PathInfo.eNotPassable;
    }
    private sLevel()
    {
    }
    public static RootTile getRootTile(int _rootId)
    {
        return mLevelEditor.rootTiles.get(_rootId);
    }
    
    static String nextMap = "Tiles/Maps/RaceReloaded";
    
    
    void radiusDestroy(Vec2 _position, float _radius)
    {
        //(x-posx)^2 + (y-posy)^2 = r^2;
        
        int centerX = (int)_position.x;
        int CenterY = (int)_position.y;
        
        
        
        /*if((x - centerX)^2 + (y - centerX)^2 < _radius^2)
        {
            
        }*/
    }
    
    public void destroyTile(int _xTile, int _yTile)
    {
        mLevelEditor.destroyTile(_xTile, _yTile);
    }
    
    public static void init()
    {
        try
        {
            mTiledMap = new AnimatedTiledMap("assets/Tiles/Maps/RaceReloaded.tmx");
        }
        catch (SlickException e)
        {
            throw new UnsupportedOperationException(nextMap + " loading failed");
        }
        mLevelEditor = new LevelEditor(mTiledMap);
        mParralaxXScale = new float[mTiledMap.getLayerCount()];
        mParralaxYScale = new float[mTiledMap.getLayerCount()];
        for (int i = 0; i < mParralaxXScale.length; i++)
        {
            mParralaxXScale[i] = 1.0f;
            mParralaxYScale[i] = 1.0f;
        }
        flagsLayer = mTiledMap.getLayerIndex("Flags");
        nextMap = mTiledMap.getMapProperty("NextMap", nextMap);
    }
    public static void newLevel()
    {
        try
        {
            mTiledMap = new AnimatedTiledMap("assets/" + nextMap + ".tmx");
            mTiledMap.initAnimationlayer("assets/TileAnimation.def");
        }
        catch (SlickException e)
        {
            throw new UnsupportedOperationException(nextMap + " loading failed");
        }
        boolean destroy = true;
        if (mLevelEditor == null)
        {
            destroy = false;
            mLevelEditor = new LevelEditor(mTiledMap);
        }
        mParralaxXScale = new float[mTiledMap.getLayerCount()];
        mParralaxYScale = new float[mTiledMap.getLayerCount()];
        for (int i = 0; i < mParralaxXScale.length; i++)
        {
            mParralaxXScale[i] = 1.0f;
            mParralaxYScale[i] = 1.0f;
        }
        flagsLayer = mTiledMap.getLayerIndex("Flags");
        mLevelEditor.newLevel(mTiledMap, destroy);
        midLayer = mTiledMap.getLayerIndex("Level");
        for (int i = 0; i < mParralaxXScale.length; i++)
        {
            mParralaxXScale[i] = new Float(mTiledMap.getLayerProperty(i, "ScaleX", "1.0"));
            mParralaxYScale[i] = new Float(mTiledMap.getLayerProperty(i, "ScaleY", "1.0"));
        }
    }
    public static void update()
    {
        mLevelEditor.update();
    }
    public static void renderBackground(Graphics _graphics)
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        xTiles = (int)(s.x/64.0f)+2;
        yTiles = (int)(s.y/64.0f)+2;
        Vec2 translation = sWorld.getPixelTranslation();
        for (int i = 0; i < midLayer; i++)
        {
            if (i != flagsLayer)
            {
                Vec2 myTranslation = translation.clone();
                myTranslation.x = myTranslation.x *(mParralaxXScale[i]);
                myTranslation.y = myTranslation.y *(mParralaxYScale[i]);
                int xStart = -(int)(myTranslation.x/64.0f);
                int yStart = -(int)(myTranslation.y/64.0f);
                int transX = (int)myTranslation.x;
                int transY = (int)myTranslation.y;
                transX = transX % 64;
                transY = transY % 64;
                mTiledMap.render(transX,transY, xStart,yStart, xTiles,yTiles, i, false);
            }
        }
        int xStart = -(int)(translation.x/64.0f);
        int yStart = -(int)(translation.y/64.0f);
        mTiledMap.renderAnimatedLayer(translation.x,translation.y,xStart,yStart,xTiles,yTiles);
    }
    public static void renderForeground()
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        xTiles = (int)(s.x/64.0f)+2;
        yTiles = (int)(s.y/64.0f)+2;
        Vec2 translation = sWorld.getPixelTranslation();
        for (int i = midLayer; i < mTiledMap.getLayerCount(); i++) /// I fixed it you terrible, terrible person
        {
            if (i != flagsLayer)
            {
                Vec2 myTranslation = translation.clone();
                myTranslation.x = myTranslation.x *(mParralaxXScale[i]);
                myTranslation.y = myTranslation.y *(mParralaxYScale[i]);
                int xStart = -(int)(myTranslation.x/64.0f);
                int yStart = -(int)(myTranslation.y/64.0f);
                int transX = (int)myTranslation.x;
                int transY = (int)myTranslation.y;
                transX = transX % 64;
                transY = transY % 64;
                mTiledMap.render(transX,transY, xStart,yStart,xTiles,yTiles, i, false);
            }
        }     
        
    }
}
