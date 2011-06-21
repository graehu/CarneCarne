/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Level.sLevel;
import World.sWorld;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
public class GumLandEvent extends iEvent {

    int x, y;
    Body mBody;
    public GumLandEvent(int _x, int _y, Body _body)
    {
        x = _x;
        y = _y;
        mBody = _body;
    }
    public String getName()
    {
        return "GumLandEvent";
    }

    public String getType()
    {
        return getName();
    }
    
    public void process()
    {
        sLevel.placeTile(x, y, sLevel.TileType.eGum);
        sWorld.destroyBody(mBody);
    }
    
}
