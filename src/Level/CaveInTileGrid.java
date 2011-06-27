/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Entities.Entity;
import Entities.sEntityFactory;
import Graphics.Skins.TiledSkin;
import Level.CaveInSearcher.TempTile;
import World.sWorld;
import java.util.ArrayList;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TileSet;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
public class CaveInTileGrid extends TileGrid
{
    private int mXTrans, mYTrans;
    private int ids[][];
    private TiledMap mTiledMap;
    private int mLayerIndex;
    public CaveInTileGrid(RootTileList _rootTiles, TiledMap _tiledMap, int _xTrans, int _yTrans, int _width, int _height, int _layerIndex)
    {
        super(_rootTiles, _width, _height);
        mXTrans = _xTrans;
        mYTrans = _yTrans;
        ids = new int[_width][_height];
        mTiledMap = _tiledMap;
        mLayerIndex = _layerIndex;
    }
    
    public void setTempId(int _x, int _y, int _id)
    {
        ids[_x-mXTrans][_y-mYTrans] = _id;
    }
    public void finish(ArrayList<TempTile> tiles)
    {
        for (TempTile tile: tiles)
        {
            setTempId(tile.x, tile.y, tile.getId());
            tile.x -= mXTrans;
            tile.y -= mYTrans;
        }
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("isDynamic", true);
        parameters.put("position", new Vec2(mXTrans, mYTrans));
        init(parameters);
        parameters = new HashMap<String, Object>();
        parameters.put("tiles", tiles);
        parameters.put("body",mBody);
        mBody.setUserData(sEntityFactory.create("CaveIn", parameters));
    }
    @Override
    int getTileId(int _x, int _y)
    {
        return ids[_x][_y];
    }

    @Override
    void setTileId(int _x, int _y, int _id) 
    {
        ids[_x][_y] = _id;
        if (mBody != null)
        {
            Entity entity = (Entity)mBody.getUserData();
            if (entity != null)
            {
                TiledSkin skin = (TiledSkin)entity.mSkin;
                skin.setId(_x,_y,_id);
            }
        }
    }
    void createPhysicsBody(int _x, int _y, Tile _tile)
    {
        int id = ids[_x][_y];
        if (id != 0)
        {
            TileSet tileSet = mTiledMap.findTileSet(id);
            int idDiff = id - tileSet.firstGID;
            Image image = tileSet.tiles.getSubImage(idDiff%tileSet.tilesAcross, idDiff/tileSet.tilesAcross);
            HashMap parameters = new HashMap();
            parameters.put("isDynamic", true);
            parameters.put("img", image);

            /// Individual tiles
            mTiles[_x][_y].createPhysicsBody(_x, _y, parameters, mBody);
            Fixture fixture = mTiles[_x][_y].mFixture;
            //mLastBody = body;
            //parameters.put("Body", fixture);
            //sEntityFactory.create("CaveInTileFactory", parameters);
        }
    }
    public void destroyTile(int _x, int _y)
    {
        destroyTileImplementation(_x, _y);
        if (mBody.getFixtureList() == null)
        {
            sWorld.destroyBody(mBody);
            mBody = null;
        }
    }
    void caveInSearch(int _x, int _y)
    {
        /*sWorld.destroyBody(mBody);
        ((Entity)mBody.getUserData()).mBody = null;
        mBody.setUserData(null);*/
    }
}
