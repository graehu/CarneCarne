/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Level.Tile;
import Level.sLevel;
import World.sWorld;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
public class GumLandEvent extends iEvent
{
    Body mBody;
    Tile mTile;
    int mRootId;
    public GumLandEvent(Body _body, int _rootId, Tile _tile)
    {
        mBody = _body;
        mRootId = _rootId;
        mTile = _tile;
    }
    public String getName()
    {
        return "GumLandEvent";
    }

    public String getType()
    {
        return getName();
    }
    
    @Override
    public boolean process()
    {
        mTile.getTileGrid().placeTile(mTile.getPosition(), mRootId);
        sWorld.destroyBody(mBody);
        return true;
    }
    
}
