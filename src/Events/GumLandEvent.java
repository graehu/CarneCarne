/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Level.Tile;
import Level.sLevel.TileType;
import World.sWorld;
import org.jbox2d.common.Vec2;
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
        if (mTile != null)
        {
            Vec2 position = mTile.getTileGrid().getBody().getWorldPoint(mTile.getLocalPosition());//mBody.getPosition();
            Vec2 velocity = mBody.getLinearVelocity();
            int xPos = (int)position.x;
            int yPos = (int)position.y;
            
            if (velocity.x* velocity.x > velocity.y*velocity.y)
            {
                if (!tryX(xPos, yPos, velocity))
                {
                    if (!tryY(xPos, yPos, velocity))
                    {
                        tryDiagonal(xPos, yPos, velocity);
                    }
                }
            }
            else if (!tryY(xPos, yPos, velocity))
            {
                if (!tryX(xPos, yPos, velocity))
                {
                    tryDiagonal(xPos, yPos, velocity);
                }
            }
            //place(xPos, yPos);
            mTile = null;
        }
        sWorld.destroyBody(mBody);
        return false;
    }
    
    private boolean tryX(int _x, int _y, Vec2 _velocity)
    {
        if (_velocity.x > 0.0f)
            _x--;
        else
            _x++;
        return place(_x, _y);
    }
    private boolean tryY(int _x, int _y, Vec2 _velocity)
    {
        if (_velocity.y > 0.0f)
            _y--;
        else
            _y++;
        return place(_x, _y);
    }
    private boolean tryDiagonal(int _x, int _y, Vec2 _velocity)
    {
        if (_velocity.x > 0.0f)
            _x--;
        else
            _x++;
        if (_velocity.y > 0.0f)
            _y--;
        else
            _y++;
        return place(_x, _y);
    }
    private boolean place(int _x, int _y)
    {
        if (mTile.getTileGrid().get(_x, _y).getRootTile().getTileType().equals(TileType.eEmpty))
        {
            mTile.getTileGrid().placeTile(_x, _y, mRootId);
            return true;
        }
        else return false;
    }
    
}
