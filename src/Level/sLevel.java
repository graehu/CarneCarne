/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Physics.sPhysics;
import java.util.Hashtable;
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
    private static int lowestSlope;
    private sLevel()
    {
        
    }
    public static void init() throws SlickException
    {
        lowestSlope = 1000000000;
        mTiledMap = new TiledMap("data/Test_map3ready.tmx");
        layerIndex = mTiledMap.getLayerIndex("Level");
        Stack<Integer> stack = new Stack<Integer>();
        Hashtable parameters = new Hashtable();
        for (int i = 0; i < mTiledMap.getWidth(); i++)
        {
            for (int ii = 0; ii < mTiledMap.getHeight(); ii++)
            {
                int id = mTiledMap.getTileId(i, ii, layerIndex);
                /*if (id%4 != 1 && id != 0)
                {
                    throw new SlickException("gay");
                }*/
                String type = mTiledMap.getTileProperty(id, "Type", "None");
                if (type.equals("Block"))
                {
                    parameters.put("position", new Vec2(i,ii));
                    sPhysics.useFactory("TileFactory",parameters);
                    if (i > 0 && ii > 0 && i < mTiledMap.getWidth()-1 && ii < mTiledMap.getHeight()-1 && id != 0)
                        checkTileEdges(i,ii, id, stack);
                }
                else if (type.equals("Slope"))
                {
                    if (id < lowestSlope)
                    {
                        lowestSlope = id;
                    }
                    parameters.put("position", new Vec2(i,ii));
                    parameters.put("slopeType",getSlopeType(i, ii));
                    sPhysics.useFactory("SlopeFactory",parameters);
                    if (i > 0 && ii > 0 && i < mTiledMap.getWidth()-1 && ii < mTiledMap.getHeight()-1 && id != 0)
                        checkSlopeEdges(i,ii,id,stack);
                }
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
    private static void checkSlopeEdges(int _xTile, int _yTile, int _id, Stack<Integer> _stack)
    {
        int id;
        boolean LD[] = new boolean[2];
        
        int slopeType = getSlopeType(_xTile, _yTile);
        
        switch (slopeType)
        {
            case 0:
            {
                LD[0] = boundaryFrom(_xTile, _yTile+1, Direction.eFromUp);
                LD[1] = boundaryFrom(_xTile-1, _yTile, Direction.eFromRight);
                break;
            }
            case 1:
            {
                LD[0] = boundaryFrom(_xTile, _yTile+1, Direction.eFromUp);
                LD[1] = boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft);
                break;
            }
            case 2:
            {
                LD[0] = boundaryFrom(_xTile, _yTile-1, Direction.eFromDown);
                LD[1] = boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft);
                break;
            }
            case 3:
            {
                LD[0] = boundaryFrom(_xTile, _yTile-1, Direction.eFromDown);
                LD[1] = boundaryFrom(_xTile-1, _yTile, Direction.eFromRight);
                break;
            }
        }
        
        int textureUnit = 0;
        for (int i = 0; i < 2; i++)
        {
            if (!LD[i])
                textureUnit |= (1 << i);
        }
        _stack.push(_xTile);
        _stack.push(_yTile);
        _stack.push(_id+textureUnit);
    }
    enum Direction
    {
        eFromUp,
        eFromLeft,
        eFromRight,
        eFromDown,
        eDirectionsMax
    }
    private static boolean boundaryFrom(int _xTile, int _yTile, Direction _direction)
    {
        int id = mTiledMap.getTileId(_xTile, _yTile, layerIndex);
        if (id == 0)
        {
            return false;
        }
        if (id < lowestSlope)
        {
            return true;
        }
        int slope = getSlopeType(_xTile, _yTile);
        switch (slope)
        {
            case 0:
            {
                if (_direction == Direction.eFromDown || _direction == Direction.eFromLeft)
                {
                    return true;
                }
                return false;
            }
            case 1:
            {
                if (_direction == Direction.eFromDown || _direction == Direction.eFromRight)
                {
                    return true;
                }
                return false;
            }
            case 2:
            {
                if (_direction == Direction.eFromUp || _direction == Direction.eFromRight)
                {
                    return true;
                }
                return false;
            }
            case 3:
            {
                if (_direction == Direction.eFromUp || _direction == Direction.eFromLeft)
                {
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    private static void checkTileEdges(int _xTile, int _yTile, int _id, Stack<Integer> _stack)
    {
        int id;
        boolean ULDR[] = new boolean[4];
        
        ULDR[3] = boundaryFrom(_xTile, _yTile-1, Direction.eFromDown);//(id != 0);
        
        ULDR[2] = boundaryFrom(_xTile-1, _yTile, Direction.eFromRight);
        
        ULDR[1] = boundaryFrom(_xTile, _yTile+1, Direction.eFromUp);
        
        ULDR[0] = boundaryFrom(_xTile+1, _yTile, Direction.eFromLeft);
        
        int textureUnit = 0;
        for (int i = 0; i < 4; i++)
        {
            if (!ULDR[i])
                textureUnit |= (1 << i);
        }

        _stack.push(_xTile);
        _stack.push(_yTile);
        _stack.push(_id+textureUnit);
    }
    private static int getRootTile(int _x, int _y)
    {
        int id = mTiledMap.getTileId(_x, _y, layerIndex);
        if (id < lowestSlope)
        {
            id--;
            id /= 16;
            id *= 16;
            id++;
            return id;
        }
        else
        {
            id--;
            id /= 4;
            id *= 4;
            id++;
            return id;
        }
    }
    private static int getRootSlope(int _x, int _y)
    {
        int id = mTiledMap.getTileId(_x, _y, layerIndex);
        id--;
        id /= 16;
        id *= 16;
        id /= 4;
        id *= 4;
        id++;
        return id;
    }
    private static int getSlopeType(int _x, int _y)
    {
        int id = mTiledMap.getTileId(_x, _y, layerIndex);
        id--;
        id %= 16;
        id /= 4;
        return id;
    }
    private static void checkEdges(int _x, int _y, int _id, Stack<Integer> _stack)
    {
        int id = mTiledMap.getTileId(_x, _y, layerIndex);
        if (id != 0)
        {
            if (id < lowestSlope)
            {
                checkTileEdges(_x,_y,_id, _stack);
            }
            else
            {
                checkSlopeEdges(_x,_y,_id,_stack);
            }
        }
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
        checkEdges(x,y,rootId,stack);
        x = _x+1;
        y = _y;
        rootId = getRootTile(x, y);
        checkEdges(x,y,rootId,stack);
        x = _x;
        y = _y-1;
        rootId = getRootTile(x, y);
        checkEdges(x,y,rootId,stack);
        x = _x;
        y = _y+1;
        rootId = getRootTile(x, y);
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
