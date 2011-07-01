/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Entities.Entity;
import Level.sLevel.TileType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
public class FakeTile extends Tile
{
    Body mBody;
    public FakeTile(Body _body)
    {
        super(-1, null, null, -1, -1);
        mBody = _body;
    }
    
    @Override
    public TileType getTileType()
    {
        return TileType.eSwingable;
    }
    @Override
    public Vec2 getPosition()
    {
        return mBody.getPosition();
    }
    @Override
    public void destroyFixture()
    {
        //((Entity)mBody.getUserData()).kill();
    }
    public boolean damageTile()
    {
        return true;
    }
}
