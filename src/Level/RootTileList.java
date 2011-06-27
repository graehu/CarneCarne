/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.MelonSkinTile.SkinDirection;
import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author A203946
 */
public class RootTileList {
    
    private static ArrayList<RootTile> mRootTiles;
    
    public RootTileList(TiledMap _tiledMap)
    {
        HashMap<String, sLevel.TileType> typeMap = new HashMap<String, sLevel.TileType>();
        typeMap.put("Ice", sLevel.TileType.eIce);
        typeMap.put("Spikes", sLevel.TileType.eSpikes);
        typeMap.put("Acid", sLevel.TileType.eAcid);
        typeMap.put("Swim", sLevel.TileType.eWater);
        typeMap.put("Edible", sLevel.TileType.eEdible);
        typeMap.put("Swingable", sLevel.TileType.eSwingable);
        typeMap.put("NonEdible", sLevel.TileType.eIndestructible);
        typeMap.put("Bouncy", sLevel.TileType.eBouncy);
        typeMap.put("Gum", sLevel.TileType.eGum);
        typeMap.put("Tar", sLevel.TileType.eTar);
        typeMap.put("Chilli", sLevel.TileType.eChilli);
        typeMap.put("MelonFlesh", sLevel.TileType.eMelonFlesh);
        typeMap.put("MelonSkin", sLevel.TileType.eMelonSkin);
        HashMap<String, SkinDirection> directionMap = new HashMap<String, SkinDirection>();
        directionMap.put("Right", SkinDirection.eRight);
        directionMap.put("Down", SkinDirection.eDown);
        directionMap.put("Left", SkinDirection.eLeft);
        directionMap.put("Up", SkinDirection.eUp);
        directionMap.put("DownLeft", SkinDirection.eDownLeft);
        directionMap.put("DownRight", SkinDirection.eDownRight);
        directionMap.put("UpLeft", SkinDirection.eUpLeft);
        directionMap.put("UpRight", SkinDirection.eUpRight);
        int idsSize = 0;
        for (int i = 0; i < _tiledMap.getTileSetCount(); i++)
        {
            idsSize += _tiledMap.getTileSet(i).lastGID - _tiledMap.getTileSet(i).firstGID;
        }
        mRootTiles = new ArrayList<RootTile>();
        mRootTiles.add(new EmptyTile());
        int slopeCounter = 0;
        for (int i = 1; i < idsSize;)
        {
            String shape = _tiledMap.getTileProperty(i, "Type", "None");
            String typeString = _tiledMap.getTileProperty(i, "Material", "Edible");
            int maxHealth = new Integer(_tiledMap.getTileProperty(i, "MaxHealth", "1")).intValue();
            sLevel.TileType type = typeMap.get(typeString);
            int assertion = type.ordinal();
            if (shape.equals("Block"))
            {
                boolean regrows = Boolean.valueOf(_tiledMap.getTileProperty(i,"Regrows","true")).booleanValue();
                boolean anchor = Boolean.valueOf(_tiledMap.getTileProperty(i, "Anchor", "false")).booleanValue();
                String flammableString = _tiledMap.getTileProperty(i, "Flammable", "false");
                boolean isFlammable = Boolean.valueOf(flammableString).booleanValue();
                int size = (isFlammable) ? 32 : 16;
                for (int rootId = i; i < rootId + 16; i++)
                {
                    mRootTiles.add(new BlockTile(rootId, type, regrows, anchor, isFlammable, maxHealth));
                }
            }
            else if (shape.equals("Slope"))
            {
                for (int rootId = i; i < rootId + 4; i++)
                    mRootTiles.add(new RightDownSlope(i,type, maxHealth)); 
                for (int rootId = i; i < rootId + 4; i++)
                    mRootTiles.add(new LeftDownSlope(i,type, maxHealth)); 
                for (int rootId = i; i < rootId + 4; i++)
                    mRootTiles.add(new LeftUpSlope(i,type, maxHealth)); 
                for (int rootId = i; i < rootId + 4; i++)
                    mRootTiles.add(new RightUpSlope(i,type, maxHealth));
            }
            else if (shape.equals("NonEdible"))
            {
                boolean anchor = Boolean.valueOf(_tiledMap.getTileProperty(i, "Anchor", "true")).booleanValue();
                mRootTiles.add(new NonEdibleTile(i, type, anchor));
                i++;
            }
            else if (shape.equals("Water"))
            {
                for (int rootId = i; i < rootId + 4; i++)
                {
                    mRootTiles.add(new WaterTile(rootId, type));
                }                
            }
            else if (shape.equals("MelonSkin"))
            {
                for (int rootId = i; i < rootId + 16; i++)
                {
                    String directionString = _tiledMap.getTileProperty(i, "Direction", "Right");
                    SkinDirection direction = directionMap.get(directionString);
                    mRootTiles.add(new MelonSkinTile(i, type, direction, maxHealth));
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
        if (index >= mRootTiles.size())
        {
            return mRootTiles.get(0);
        }
        return mRootTiles.get(index);
    }
}
