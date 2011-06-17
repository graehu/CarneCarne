/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Physics.sPhysics;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
/**
 *
 * @author alasdair
 */
public class sLevel {
    private static TiledMap mTiledMap;
    private static LevelEditor mLevelEditor;
    private sLevel()
    {
    }
    public static void init() throws SlickException
    {
        mTiledMap = new TiledMap("data/Test_map3ready.tmx");
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
        Vec2 translation = sPhysics.getPixelTranslation();
        mTiledMap.render((int)translation.x,(int)translation.y);
    }
}
