/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Entities.Entity;
import Entities.sEntityFactory;
import Graphics.Skins.TiledSkin;
import Level.CaveInSearcher.TempTile;
import Level.sLevel.TileType;
import World.sWorld;
import java.util.ArrayList;
import java.util.HashMap;
import org.jbox2d.common.Mat33;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.Vec3;
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
    int ids[][];
    private TiledMap mTiledMap;
    private int mLayerIndex;
    Vec2 mPosition;
    float mAngle;
    public CaveInTileGrid(RootTileList _rootTiles, TiledMap _tiledMap, int _xTrans, int _yTrans, int _width, int _height, int _layerIndex, Vec2 _position, float _angle)
    {
        super(_rootTiles, _width, _height);
        mPosition = _position;
        mAngle = _angle;
        mXTrans = _xTrans;
        mYTrans = _yTrans;
        translate();
        ids = new int[_width][_height];
        mTiledMap = _tiledMap;
        mLayerIndex = _layerIndex;
    }

    private void translate()
    {
        Mat33 translation = new Mat33(new Vec3(1,0,mXTrans), new Vec3(0,1,mYTrans), new Vec3(0,0,1));
        float sin = (float)Math.sin(mAngle);
        float cos = (float)Math.cos(mAngle);
        Mat33 rotation = new Mat33(new Vec3(cos,-sin,0), new Vec3(sin,cos,0), new Vec3(0,0,1));
        //rotation = new Mat33(new Vec3(1,0,0), new Vec3(0,1,0), new Vec3(0,0,1));
        //Mat33 secondTranslation = new Mat33(new Vec3(1,0,0), new Vec3(0,1,0), new Vec3(mPosition.x,mPosition.y,1));
        
        translation = mul(translation, rotation);
        //translation = mul(translation, secondTranslation);
        Vec3 output = Mat33.mul(translation, new Vec3(mPosition.x,mPosition.y,1));
        mPosition.x = output.x;
        mPosition.y = output.y;
    }
    Mat33 mul(Mat33 M1, Mat33 M2)
    {
        Mat33 ret = new Mat33();

        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                float Value = 0;

                for(int k = 0; k < 3; k++)
                {
                    Value += getElem(M1,i,k) * getElem(M1,k,j);
                }
                setElem(ret,i,j,Value);
            }
        }
        return ret;
    }
    float getElem(Mat33 mat, int i, int ii)
    {
        Vec3 col = null;
        switch (i)
        {
            case 0:
                col = mat.col1;
                break;
            case 1:
                col = mat.col2;
                break;
            case 2:
                col = mat.col3;
                break;
        }
        switch (ii)
        {
            case 0:
                return col.x;
            case 1:
                return col.y;
            case 2:
                return col.z;
            default:
                return 10000000.0f;
        }
    }
    void setElem(Mat33 mat, int i, int ii, float value)
    {
        Vec3 col = null;
        switch (i)
        {
            case 0:
                col = mat.col1;
                break;
            case 1:
                col = mat.col2;
                break;
            case 2:
                col = mat.col3;
                break;
        }
        switch (ii)
        {
            case 0:
                col.x = value;
            case 1:
                col.y = value;
            case 2:
                col.z = value;
        }
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
        parameters.put("position", new Vec2(mXTrans, mYTrans).add(mPosition));
        parameters.put("position", mPosition);
        parameters.put("angle", mAngle);
        init(parameters);
        parameters = new HashMap<String, Object>();
        parameters.put("tiles", tiles);
        parameters.put("body",mBody);
        parameters.put("width",ids.length);
        parameters.put("height",ids[0].length);
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
            mTiles[_x][_y].createPhysicsBody(parameters, mBody);
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
    boolean searching = false;
    void caveInSearch(int _x, int _y, TileType _tileType)
    {
        if (!searching)
        {
            searching = true;
            CaveInSearcher search = new CaveInSearcher(this, mTiledMap, mLayerIndex, mBody.getPosition(), mBody.getAngle());
            search.destroy(_x, _y, _tileType);
            sWorld.destroyBody(mBody);
            ((Entity)mBody.getUserData()).mBody = null;
            mBody.setUserData(null);
            searching = false;
        }
    }
}
