/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import World.sWorld;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
/**
 *
 * @author alasdair
 */
public class sLevel {
    
    public static enum TileType
    {
        eEdible,
        eSwingable,
        eIndestructible,
        eWater,
        eTypeTypesMax
    }
    public TileType getTileType(int _id)
    {
        return mLevelEditor.getTileType(_id);
    }
    private static AnimatedTiledMap mTiledMap;
    private static LevelEditor mLevelEditor;
    private static int layerIndex;   
    private enum PathInfo
    {
        eNotPassable,
        eAir,
        eLeftSlope,
        eRightSlope,
        ePathable
    };
    
    public int getTileSizeInMetres()
    {
        return 1; //just incase we want to make tiles smaller than a meter.
    }
    
    public static PathInfo getPathInfo(int _xTile, int _yTile)
    {
        int id = mTiledMap.getTileId(_xTile, _yTile, layerIndex);
        if (id == 0)
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
        mTiledMap = new AnimatedTiledMap("assets/Test_map3ready.tmx");
        mTiledMap.initAnimationlayer("assets/splashbig.def");
        mLevelEditor = new LevelEditor(mTiledMap);
    }
    public static void destroyTile(int _x, int _y)
    {
        mLevelEditor.destroyTile(_x, _y);
    }
    public static void update()
    {
        mLevelEditor.update();
    }
    public static void render()
    {
        Vec2 translation = sWorld.getPixelTranslation();
        mTiledMap.render((int)translation.x,(int)translation.y,0,0,20,20);
        mTiledMap.renderAnimatedLayer(translation.x,translation.y,800.0f,600.0f);
    }
}
