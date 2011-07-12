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
public class MovingParticleSys implements ParticleSysBase
{
    ParticleSys mParticles;
    Tile mTile;
    Vec2 mOffset;
    public MovingParticleSys(ParticleSys _particles, Tile _tile)
    {
        mParticles = _particles;
        mTile = _tile;
    }

    public boolean isDead()
    {
        return mParticles.isDead();
    }

    public void kill() {
        mParticles.kill();
    }

    public void recycle() {
        mParticles.recycle();
    }

    public void moveEmittersTo(float _x, float _y) {
        mParticles.moveEmittersTo(_x, _y);
    }

    public void moveEmittersBy(float _dx, float _dy) {
        mParticles.moveEmittersBy(_dx, _dy);
    }

    public void setWind(float _windFactor) {
        mParticles.setWind(_windFactor);
    }

    public void setGravity(float _gravityFactor) {
        mParticles.setGravity(_gravityFactor);
    }

    public void setAngularOffset(float _degrees) {
        mParticles.setAngularOffset(_degrees);
    }

    public boolean update(int _delta)
    {
        Vec2 position = mTile.getWorldPosition().mul(64).add(new Vec2(32,32));
        mParticles.moveEmittersTo(position.x, position.y);
        return mParticles.update(_delta);
    }

    public void render(float _x, float _y)
    {
        throw new UnsupportedOperationException("You stupid nigger");
    }
}
