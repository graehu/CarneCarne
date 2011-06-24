/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import java.util.HashMap;
import java.util.Stack;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author alasdair
 */
public class Tile {

    RootTile mRootId;
    int mId;
    Body mBody;
    int mHealth; /// Aliases as water height
    public Tile(int _id, RootTile _rootId)
    {
        mId = _id;
        mRootId = _rootId;
        mHealth = mRootId.mMaxHealth;
    }
    enum Direction
    {
        eFromUp,
        eFromLeft,
        eFromRight,
        eFromDown,
        eDirectionsMax
    }
    public int getWaterHeight()
    {
        return mHealth;
    }
    public void setWaterHeight(int _height)
    {
        mHealth = _height;
    }
    public void createPhysicsBody(int _xTile, int _yTile)
    {
        HashMap parameters = new HashMap();
        parameters.put("HighestSurface", mHealth); /// Will only be read by water tile factory
        mBody = mRootId.createPhysicsBody(_xTile, _yTile, parameters);
    }
    public Fixture createFixture(int _xTile, int _yTile)
    {
        return mRootId.createFixture(_xTile, _yTile);
    }
    public void checkEdges(int _xTile, int _yTile, Stack<Integer> _stack, TileGrid _tileGrid)
    {
        mRootId.checkEdges(_xTile, _yTile, _stack, _tileGrid);
    }
}
