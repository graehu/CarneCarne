/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.sLevel.TileType;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
public class LevelEditor {
    
    private TiledMap mTiledMap;
    int layerIndex;
    RootTileList rootTiles;
    TileGrid tileGrid;
    
    public LevelEditor(TiledMap _tiledMap)
    {
        mTiledMap = _tiledMap;
        layerIndex = mTiledMap.getLayerIndex("Level");
        
        rootTiles = new RootTileList(mTiledMap);
        
        tileGrid = new TileGrid(mTiledMap, rootTiles, layerIndex);
        
        FlagProcessor flagProcessor = new FlagProcessor(_tiledMap);
        flagProcessor = null;
    }
    public void placeTile(int _x, int _y, TileType _tileType)
    {
        tileGrid.placeTile(_x, _y, 1);        
    }
    public void destroyTile(int _xTile, int _yTile)
    {
        tileGrid.destroyTile(_xTile, _yTile);
    }
    public void update()
    {
        tileGrid.update();
    }
    public sLevel.TileType getTileType(int _id)
    {
        return rootTiles.getTileType(_id);
    }

    boolean damageTile(int _x, int _y)
    {
        return tileGrid.damageTile(_x, _y);
    }
}
