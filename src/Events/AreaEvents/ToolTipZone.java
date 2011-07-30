/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.AIEntity;
import Entities.PlayerEntity;

/**
 *
 * @author alasdair
 */
public class ToolTipZone extends AreaEvent
{
    private String mToolTipText;
    public ToolTipZone(int _x, int _y, int _x2, int _y2, String _toolTipText)
    {
        super(_x, _y, _x2, _y2);
        mToolTipText = _toolTipText;
    }
    @Override
    public void enter(AIEntity _entity)
    {
        //try
        {
            ((PlayerEntity)_entity).displayTooltip(mToolTipText);
        }
        //catch (ClassCastException e)
        {
            
        }
    }

    @Override
    public void leave(AIEntity _entity)
    {
    }
    
}
