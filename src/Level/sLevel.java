/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Physics.sPhysics;
import java.util.Stack;
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
        mTiledMap = new TiledMap("data/Test_map.tmx");
        int layerIndex = mTiledMap.getLayerIndex("Level");
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < mTiledMap.getWidth(); i++)
        {
            for (int ii = 0; ii < mTiledMap.getHeight(); ii++)
            {
                int id = mTiledMap.getTileId(i, ii, layerIndex);
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
                if (i > 0 && ii > 0 && i < mTiledMap.getWidth()-1 && ii < mTiledMap.getHeight()-1 && id != 0)
                    checkEdges(i,ii, id, layerIndex, stack);
            }
        }
        while (!stack.empty())
        {
            int id = stack.pop();
            int yTile = stack.pop();
            int xTile = stack.pop();
            mTiledMap.setTileId(xTile, yTile, layerIndex, id);
        }
    }
    private static void checkEdges(int _xTile, int _yTile, int _id, int _layerIndex, Stack<Integer> _stack)
    {
        int id;
        boolean ULDR[] = new boolean[4];
        
        id = mTiledMap.getTileId(_xTile, _yTile-1, _layerIndex);
        ULDR[3] = (id != 0);
        
        id = mTiledMap.getTileId(_xTile-1, _yTile, _layerIndex);
        ULDR[2] = (id != 0);
        
        id = mTiledMap.getTileId(_xTile, _yTile+1, _layerIndex);
        ULDR[1] = (id != 0);
        
        id = mTiledMap.getTileId(_xTile+1, _yTile, _layerIndex);
        ULDR[0] = (id != 0);
        
        int textureUnit = 0;
        for (int i = 0; i < 4; i++)
        {
            if (!ULDR[i])
                textureUnit |= (1 << i);
        }
        String texture = mTiledMap.getTileProperty(_id, "Filename", "Error");

        _stack.push(_xTile);
        _stack.push(_yTile);
        _stack.push(_id+textureUnit);
    }
    public static void render()
    {
        Vec2 translation = sPhysics.getPixelTranslation();
        mTiledMap.render((int)translation.x,(int)translation.y);
    }
}
