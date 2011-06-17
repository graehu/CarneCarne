/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.RootTile.TileShape;
import java.util.Vector;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author A203946
 */
public class RootTileList {
    
    private static Vector<RootTile> mRootTiles;
    
    public RootTileList(TiledMap _tiledMap)
    {
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
            String type = _tiledMap.getTileProperty(i, "Type", "None");
            if (type.equals("Block"))
            {
                for (int rootId = i; i < rootId + 16; i++)
                {
                    mRootTiles.add(new BlockTile(rootId));
                }
            }
            else if (type.equals("Slope"))
            {
                for (int rootId = i; i < rootId + 4; i++)
                    mRootTiles.add(new RightDownSlope(i)); 
                for (int rootId = i; i < rootId + 4; i++)
                    mRootTiles.add(new LeftDownSlope(i)); 
                for (int rootId = i; i < rootId + 4; i++)
                    mRootTiles.add(new LeftUpSlope(i)); 
                for (int rootId = i; i < rootId + 4; i++)
                    mRootTiles.add(new RightUpSlope(i));
            }
            else if (type.equals("NonEdible"))
            {
                mRootTiles.add(new NonEdibleTile(i));
                i++;
            }
            else
            {
                mRootTiles.add(new EmptyTile());
                i++;
            }
        }
    }
    
    public RootTile get(int index)
    {
        return mRootTiles.get(index);
    }
}
