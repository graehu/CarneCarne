/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.sLevel.TileType;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class SpikeTile extends NonEdibleTile
{
    private static String[] getAnimationsNames()
    {
        String[] strings = new String[AnimationType.eAnimationsMax.ordinal()];
        strings[AnimationType.eFireHit.ordinal()] = "Default";
        strings[AnimationType.eDamage.ordinal()] = "Default";
        strings[AnimationType.eSpawn.ordinal()] = "Default";
        strings[AnimationType.eSpit.ordinal()] = "Default";
        strings[AnimationType.eJump.ordinal()] = "Hard";
        return strings;
    }
    public SpikeTile(int _id)
    {
        super(_id, TileType.eSpikes, true);
    }
    
    @Override
    protected Shape createShape(int _xTile, int _yTile)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.25f, 0.25f, new Vec2(_xTile,_yTile).add(new Vec2(0.25f,0.25f)), 0);
        return shape;
    }
    
}
