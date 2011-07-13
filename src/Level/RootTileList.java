/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.MelonSkinTile.SkinDirection;
import Level.RootTile.AnimationType;
import Level.Tile.Direction;
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
        
        HashMap<String, Direction> fromDirection = new HashMap<String, Direction>();
        fromDirection.put("Left", Direction.eFromRight);
        fromDirection.put("Up", Direction.eFromDown);
        fromDirection.put("Right", Direction.eFromLeft);
        fromDirection.put("Down", Direction.eFromUp);
        int idsSize = 0;
        for (int i = 0; i < _tiledMap.getTileSetCount(); i++)
        {
            idsSize += _tiledMap.getTileSet(i).lastGID - _tiledMap.getTileSet(i).firstGID;
        }
        mRootTiles = new ArrayList<RootTile>();
        mRootTiles.add(new EmptyTile());
        for (int i = 1; i < idsSize;)
        {
            String shape = _tiledMap.getTileProperty(i, "Type", "None");
            String typeString = _tiledMap.getTileProperty(i, "Material", "Edible");
            int maxHealth = new Integer(_tiledMap.getTileProperty(i, "MaxHealth", "1")).intValue();
            String defaultAnimationsName = _tiledMap.getTileProperty(i, "Animationsname", "Default");
            String animationsNames[] = new String[RootTile.AnimationType.eAnimationsMax.ordinal()];
            animationsNames[AnimationType.eFireHit.ordinal()] = _tiledMap.getTileProperty(i, "FireHitAnimation", defaultAnimationsName);
            animationsNames[AnimationType.eDamage.ordinal()] = _tiledMap.getTileProperty(i, "DamageAnimation", defaultAnimationsName);
            animationsNames[AnimationType.eSpawn.ordinal()] = _tiledMap.getTileProperty(i, "SpawnAnimation", defaultAnimationsName);
            animationsNames[AnimationType.eSpit.ordinal()] = _tiledMap.getTileProperty(i, "SpitAnimation", defaultAnimationsName);
            animationsNames[AnimationType.eJump.ordinal()] = _tiledMap.getTileProperty(i, "JumpAnimation", defaultAnimationsName);
            boolean regrows = Boolean.valueOf(_tiledMap.getTileProperty(i,"Regrows","true")).booleanValue();
            boolean anchor = Boolean.valueOf(_tiledMap.getTileProperty(i, "Anchor", "true")).booleanValue();
            boolean isFlammable = Boolean.valueOf(_tiledMap.getTileProperty(i, "Flammable", "false")).booleanValue();
            sLevel.TileType type = typeMap.get(typeString);
            int assertion = type.ordinal();
            assertion = 0xB00B135;
            if (!shape.equals("None"))
            {
                if (shape.equals("Block"))
                {
                    RootTile tile = new BlockTile(i, type, animationsNames, regrows, anchor, isFlammable, maxHealth);
                    for (int rootId = i + 16; i < rootId; i++)
                    {
                        mRootTiles.add(tile);
                    }
                    while (maxHealth > 1)
                    {
                        maxHealth--;
                        RootTile tile2 = new BlockTile(i, type, animationsNames, regrows, anchor, false, maxHealth);
                        tile.setNext(tile2);
                        tile2.setNext(tile);
                        for (int rootId = i + 16; i < rootId; i++)
                        {
                            mRootTiles.add(tile2);
                        }
                        tile = tile2;
                    }
                }
                else if (shape.equals("Slope"))
                {
                    for (int rootId = i; i < rootId + 4; i++)
                        mRootTiles.add(new RightDownSlope(i,type, animationsNames, maxHealth)); 
                    for (int rootId = i; i < rootId + 4; i++)
                        mRootTiles.add(new LeftDownSlope(i,type, animationsNames, maxHealth)); 
                    for (int rootId = i; i < rootId + 4; i++)
                        mRootTiles.add(new LeftUpSlope(i,type, animationsNames, maxHealth)); 
                    for (int rootId = i; i < rootId + 4; i++)
                        mRootTiles.add(new RightUpSlope(i,type, animationsNames, maxHealth));
                }
                else if (shape.equals("NonEdible"))
                {
                    int size = Integer.valueOf(_tiledMap.getTileProperty(i, "Size", "1"));
                    for (int ii = 0; ii < size; ii++)
                    {
                        mRootTiles.add(new NonEdibleTile(i, type, anchor));
                        i++;
                    }
                }
                else if (shape.equals("NonEdibleEdge"))
                {
                    int size = Integer.valueOf(_tiledMap.getTileProperty(i, "Size", "1"));
                    float bodyScale = Float.valueOf(_tiledMap.getTileProperty(i, "BodyScale", "1.0"));
                    for (int ii = 0; ii < size; ii++)
                    {
                        mRootTiles.add(new NonEdibleEdgeTile(i, type, Direction.values()[ii], animationsNames, bodyScale));
                        i++;
                    }
                }
                else if (shape.equals("NonEdibleSlope"))
                {
                    SlopeTile tile;
                    
                    tile = new RightDownSlope(i,type, animationsNames, maxHealth);
                    tile.setImmutable();
                    mRootTiles.add(tile);
                    i++;
                    tile = new LeftDownSlope(i,type, animationsNames, maxHealth);
                    tile.setImmutable();
                    mRootTiles.add(tile);
                    i++;
                    tile = new LeftUpSlope(i,type, animationsNames, maxHealth);
                    tile.setImmutable();
                    mRootTiles.add(tile);
                    i++;
                    tile = new RightUpSlope(i,type, animationsNames, maxHealth);
                    tile.setImmutable();
                    mRootTiles.add(tile);
                    i++;
                }
                else if (shape.equals("Water"))
                {
                    for (int rootId = i; i < rootId + 4; i++)
                    {
                        mRootTiles.add(new WaterTile(rootId, type, animationsNames));
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
                else if (shape.equals("Edge"))
                {
                    Direction direction = fromDirection.get(_tiledMap.getTileProperty(i, "Direction", "Down"));
                    int size = Integer.valueOf(_tiledMap.getTileProperty(i, "Size", "1"));
                    for (int ii = 0; ii < size; ii++)
                    {
                        for (int rootId = i; i < rootId + 2; i++)
                        {
                            mRootTiles.add(new EdgeTile(i, type, direction, animationsNames, regrows, anchor, isFlammable, maxHealth));
                        }
                    }
                }
                else if (shape.equals("Line"))
                {
                    Direction direction = fromDirection.get(_tiledMap.getTileProperty(i, "Direction", "Down"));
                    int size = Integer.valueOf(_tiledMap.getTileProperty(i, "Size", "1"));
                    for (int ii = 0; ii < size; ii++)
                    {
                        for (int rootId = i; i < rootId + 4; i++)
                        {
                            mRootTiles.add(new LineTile(i, type, direction, animationsNames, regrows, anchor, isFlammable, maxHealth));
                        }
                    }
                }
                else i++;
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
