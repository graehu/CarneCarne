/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Particles;

import Level.Tile;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class TileMovingParticleSys extends MovingParticleSys
{
    Tile mTile;
    TileMovingParticleSys(ParticleSysBase _particles, Tile _tile)
    {
        super(_particles);
        mTile = _tile;
    }
    public boolean update(int _delta)
    {
        Vec2 position = mTile.getWorldPosition().mul(64).add(new Vec2(32,32));
        mParticles.moveEmittersTo(position.x, position.y);
        return mParticles.update(_delta);
    }
    public Vec2 getPosition()
    {
        return mTile.getWorldPosition().mul(64).add(new Vec2(32,32));
    }
}
