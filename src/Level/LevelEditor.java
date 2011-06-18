/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

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
}
