/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Graphics.sGraphicsManager;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.SlickException;
/**
 *
 * @author alasdair
 */
public class sLevel {
    
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
    private static int layerIndex;  
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
        if (type == TileType.eTileTypesMax)
        {
            return PathInfo.eAir;
        }
        else return PathInfo.eNotPassable;
    }
    private sLevel()
    {
    }
    public static void init() throws SlickException
    {
        mTiledMap = new AnimatedTiledMap("assets/TestMap.tmx");
        mTiledMap.initAnimationlayer("assets/TileAnimation.def");
        mLevelEditor = new LevelEditor(mTiledMap);
        midLayer = mTiledMap.getLayerIndex("Level");
        mParralaxXScale = new float[mTiledMap.getLayerCount()];
        mParralaxYScale = new float[mTiledMap.getLayerCount()];
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
    public static void renderBackground()
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        xTiles = (int)(s.x/64.0f)+2;
        yTiles = (int)(s.y/64.0f)+2;
        Vec2 translation = sWorld.getPixelTranslation();
        for (int i = 0; i < midLayer; i++)
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
        mTiledMap.renderAnimatedLayer(translation.x,translation.y,s.x,s.y);
    }
    public static void renderForeground()
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        xTiles = (int)(s.x/64.0f)+2;
        yTiles = (int)(s.y/64.0f)+2;
        Vec2 translation = sWorld.getPixelTranslation();
        for (int i = midLayer; i < mTiledMap.getLayerCount(); i++)
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
}
