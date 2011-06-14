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
    
    private sLevel()
    {
        
    }
    public static void init() throws SlickException
    {
        mTiledMap = new TiledMap("data/GiantPoo.tmx");
        for (int i = 0; i < mTiledMap.getWidth(); i++)
        {
            for (int ii = 0; ii < mTiledMap.getHeight(); ii++)
            {
                int id = mTiledMap.getTileId(i, ii, 0);
                String type = mTiledMap.getTileProperty(id, "Type", "None");
                if (!type.equals("None"))
                {
                    float x = i;
                    float y = ii;
                    sPhysics.createTile(type,new Vec2(i,ii));
                    //Hashtable parameters = new Hashtable();
                    //parameters.put("position", new Vec2(i,ii));
                    //sEntityFactory.create("Player",parameters);
                }
            }
        }
    }
    public static void render()
    {
        Vec2 translation = sPhysics.getPixelTranslation();
        mTiledMap.render((int)translation.x,(int)translation.y);
    }
}
