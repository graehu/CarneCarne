/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import Level.MelonSkinTile.SkinDirection;
import Level.sLevel.TileType;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class ZoomzoomTile extends NonEdibleTile
{
    private static Vec2 mBoostDirections[];
    SkinDirection mDirection;
    public ZoomzoomTile(int _id, SkinDirection _direction)
    {
        super(_id, TileType.eZoomzoom, true);
        mDirection = _direction;
        if (mBoostDirections == null)
        {
            mBoostDirections = new Vec2[SkinDirection.eSkinDirectionsSize.ordinal()];
            mBoostDirections[SkinDirection.eRight.ordinal()] = new Vec2(1,0);
            mBoostDirections[SkinDirection.eLeft.ordinal()] = new Vec2(-1,0);
            mBoostDirections[SkinDirection.eUp.ordinal()] = new Vec2(0,-1);
            mBoostDirections[SkinDirection.eDown.ordinal()] = new Vec2(0,1);
            
            mBoostDirections[SkinDirection.eDownLeft.ordinal()] = new Vec2(-1,1);
            mBoostDirections[SkinDirection.eDownRight.ordinal()] = new Vec2(1,1);
            mBoostDirections[SkinDirection.eUpLeft.ordinal()] = new Vec2(-1,-1);
            mBoostDirections[SkinDirection.eUpRight.ordinal()] = new Vec2(1,-1);
            
            for (int i = 0; i < mBoostDirections.length; i++)
            {
                mBoostDirections[i] = mBoostDirections[i].mul(5.0f); /// This is the scalar
            }
        }
    }
    
    public Vec2 getBoostDirection()
    {
        return mBoostDirections[mDirection.ordinal()];
    }
}
