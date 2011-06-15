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
    private static int layerIndex;
    private sLevel()
    {
        
    }
    public static void init() throws SlickException
    {
        mTiledMap = new TiledMap("data/Test_map.tmx");
        layerIndex = mTiledMap.getLayerIndex("Level");
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < mTiledMap.getWidth(); i++)
        {
            for (int ii = 0; ii < mTiledMap.getHeight(); ii++)
            {
                int id = mTiledMap.getTileId(i, ii, layerIndex);
                if (id%16 != 1 && id != 0)
                {
                    throw new SlickException("gay");
                }
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
                    checkEdges(i,ii, id, stack);
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
    private static void checkEdges(int _xTile, int _yTile, int _id, Stack<Integer> _stack)
    {
        int id;
        boolean ULDR[] = new boolean[4];
        
        id = mTiledMap.getTileId(_xTile, _yTile-1, layerIndex);
        ULDR[3] = (id != 0);
        
        id = mTiledMap.getTileId(_xTile-1, _yTile, layerIndex);
        ULDR[2] = (id != 0);
        
        id = mTiledMap.getTileId(_xTile, _yTile+1, layerIndex);
        ULDR[1] = (id != 0);
        
        id = mTiledMap.getTileId(_xTile+1, _yTile, layerIndex);
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
    private static int getRootTile(int _x, int _y)
    {
        int id = mTiledMap.getTileId(_x, _y, layerIndex);
        id--;
        id /= 16;
        id++;
        return id;
    }
    public static void destroyTile(int _x, int _y)
    {
        Stack<Integer> stack = new Stack<Integer>();
        mTiledMap.setTileId(_x, _y, layerIndex, 0);
        int x;
        int y;
        int rootId;
        
        x = _x-1;
        y = _y;
        rootId = getRootTile(x, y);
        if (mTiledMap.getTileId(x, y, layerIndex) != 0)
            checkEdges(x,y,rootId,stack);
        x = _x+1;
        y = _y;
        rootId = getRootTile(x, y);
        if (mTiledMap.getTileId(x, y, layerIndex) != 0)
            checkEdges(x,y,rootId,stack);
        x = _x;
        y = _y-1;
        rootId = getRootTile(x, y);
        if (mTiledMap.getTileId(x, y, layerIndex) != 0)
            checkEdges(x,y,rootId,stack);
        x = _x;
        y = _y+1;
        rootId = getRootTile(x, y);
        if (mTiledMap.getTileId(x, y, layerIndex) != 0)
            checkEdges(x,y,rootId,stack);
        while (!stack.empty())
        {
            int id = stack.pop();
            int yTile = stack.pop();
            int xTile = stack.pop();
            mTiledMap.setTileId(xTile, yTile, layerIndex, id);
        }
    }
    public static void render()
    {
        Vec2 translation = sPhysics.getPixelTranslation();
        mTiledMap.render((int)translation.x,(int)translation.y);
    }
}
