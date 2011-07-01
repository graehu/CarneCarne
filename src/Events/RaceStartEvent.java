/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author alasdair
 */
public class RaceStartEvent extends iEvent
{
    public RaceStartEvent()
    {
        
    }
    @Override
    public String getName()
    {
        return getType();
    }

    @Override
    public String getType()
    {
        return "RaceStartEvent";
    }
    
}
