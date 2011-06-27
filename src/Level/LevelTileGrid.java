/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
public final class LevelTileGrid extends TileGrid
{
    private TiledMap tiledMap;
    private int layerIndex;
    public LevelTileGrid(TiledMap _tiledMap, RootTileList _rootTiles, int _layerIndex)
    {
        super(_rootTiles, _tiledMap.getWidth(), _tiledMap.getHeight());
        tiledMap = _tiledMap;
        layerIndex = _layerIndex;
        HashMap parameters = new HashMap();
        parameters.put("isDynamic", false);
        parameters.put("position", new Vec2(0, 0));
        parameters.put("angle", 0.0f);
        init(parameters);
    }
    int getTileId(int _x, int _y)
    {
        return tiledMap.getTileId(_x, _y, layerIndex);
    }
    void setTileId(int _x, int _y, int _id)
    {
        tiledMap.setTileId(_x, _y, layerIndex, _id);
    }
    void createPhysicsBody(int _x, int _y, Tile _tile)
    {
        mTiles[_x][_y].createPhysicsBody(mBody);
    }
    public void destroyTile(int _x, int _y)
    {
        destroyTileImplementation(_x, _y);
    }
    void caveInSearch(int _x, int _y)
    {
        CaveInSearcher search = new CaveInSearcher(this, tiledMap, layerIndex, new Vec2(0,0), 0);
        search.destroy(_x, _y);
    }
}
