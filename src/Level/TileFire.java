/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Graphics.Particles.sParticleManager;
import java.util.LinkedList;
import java.util.Queue;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

/**
 *
 * @author alasdair
 */
class TileFire
{
    TileGrid mTileGrid;
    private int mFrames;
    private final int burnTime = 60;
    private final int decayLife = 60;
    private class BurningTile
    {
        Tile mTile;
        int mTimer;
        public BurningTile(Tile _tile, int _timer)
        {
            mTile = _tile;
            mTimer = _timer;
        }
    }
    Queue<BurningTile> burningTiles = new LinkedList<BurningTile>();
    Queue<BurningTile> decayingTiles = new LinkedList<BurningTile>();
    public TileFire(TileGrid _tileGrid)
    {
        mTileGrid = _tileGrid;
    }
    
    void addTile(Tile _tile)
    {
        burningTiles.add(new BurningTile(_tile, mFrames + burnTime));
        if (_tile.mTileGrid.mBody.m_type.equals(BodyType.STATIC))
            sParticleManager.createSystem("TarBurn", new Vec2(_tile.getWorldPosition()).mul(64).add(new Vec2(32,32)), 180/60.0f);
        else
            sParticleManager.createMovingSystem("TarBurn", 180/60.0f,_tile);
    }
    void update()
    {
        mFrames++;
        while (!burningTiles.isEmpty() && mFrames > burningTiles.peek().mTimer)
        {
            BurningTile tile = burningTiles.poll();
            decayingTiles.add(tile);
            ignite(tile.mTile.mXTile-1,tile.mTile.mYTile);
            ignite(tile.mTile.mXTile+1,tile.mTile.mYTile);
            ignite(tile.mTile.mXTile,tile.mTile.mYTile-1);
            ignite(tile.mTile.mXTile,tile.mTile.mYTile+1);
        }
        while (!decayingTiles.isEmpty() && mFrames > decayingTiles.peek().mTimer)
        {
            BurningTile tile = decayingTiles.poll();
            tile.mTile.destroyFixture();
        }
    }
    private void ignite(int _x, int _y)
    {
        try
        {
            mTileGrid.mTiles[_x][_y].setOnFire();
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            
        }
    }
}
