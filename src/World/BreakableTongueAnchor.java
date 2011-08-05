/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.AIEntity;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class BreakableTongueAnchor extends TongueAnchor
{
    AIEntity mAnchor;
    public BreakableTongueAnchor(AIEntity _anchor)
    {
        mAnchor = _anchor;
        mAnchor.addAnchor(this);
    }
    @Override
    public Vec2 getPosition()
    {
        return mAnchor.getBody().getPosition();
    }

    @Override
    public TongueAnchor stun()
    {
        return null;
    }
    @Override
    public boolean hasContact()
    {
        return mAnchor != null;
    }
    
    public void breakContact()
    {
        mAnchor = null;
    }
    
}
