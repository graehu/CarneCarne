/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import java.util.HashMap;
import java.util.Vector;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author A203946
 */
public class RootTileList {
    
    private static Vector<RootTile> mRootTiles;
    private static HashMap<String, sLevel.TileType> typeMap;
    
    public RootTileList(TiledMap _tiledMap)
    {
        typeMap = new HashMap<String, sLevel.TileType>();
        typeMap.put("Swim", sLevel.TileType.eWater);
        typeMap.put("Edible", sLevel.TileType.eEdible);
        typeMap.put("Swingable", sLevel.TileType.eSwingable);
        typeMap.put("NonEdible", sLevel.TileType.eIndestructible);
        int idsSize = 0;
        for (int i = 0; i < _tiledMap.getTileSetCount(); i++)
        {
            idsSize += _tiledMap.getTileSet(i).lastGID - _tiledMap.getTileSet(i).firstGID;
        }
        mRootTiles = new Vector<RootTile>();
        mRootTiles.add(new EmptyTile());
        int slopeCounter = 0;
        for (int i = 1; i < idsSize;)
        {
            String shape = _tiledMap.getTileProperty(i, "Type", "None");
            String typeString = _tiledMap.getTileProperty(i, "Material", "Edible");
            sLevel.TileType type = typeMap.get(typeString);
            int assertion = type.ordinal();
            if (shape.equals("Block"))
            {
                for (int rootId = i; i < rootId + 16; i++)
                {
                    mRootTiles.add(new BlockTile(rootId, type));
                }
            }
            else if (shape.equals("Slope"))
            {
                for (int rootId = i; i < rootId + 4; i++)
                    mRootTiles.add(new RightDownSlope(i,type)); 
                for (int rootId = i; i < rootId + 4; i++)
                    mRootTiles.add(new LeftDownSlope(i,type)); 
                for (int rootId = i; i < rootId + 4; i++)
                    mRootTiles.add(new LeftUpSlope(i,type)); 
                for (int rootId = i; i < rootId + 4; i++)
                    mRootTiles.add(new RightUpSlope(i,type));
            }
            else if (shape.equals("NonEdible"))
            {
                mRootTiles.add(new NonEdibleTile(i, type));
                i++;
            }
            else if (shape.equals("Water"))
            {
                for (int rootId = i; i < rootId + 16; i++)
                {
                    mRootTiles.add(new WaterTile(rootId, type));
                }                
            }
            else
            {
                mRootTiles.add(new EmptyTile());
                i++;
            }
        }
    }

    public sLevel.TileType getTileType(int _id)
    {
        return sLevel.TileType.eEdible;
    }
    public RootTile get(int index)
    {
        return mRootTiles.get(index);
    }
}
