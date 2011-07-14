/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author alasdair
 */
public class GenericEvent extends iEvent
{
    String mName;
    public GenericEvent(String _name)
    {
        mName = _name;
    }

    @Override
    public String getName()
    {
        return mName;
    }

    @Override
    public String getType()
    {
        return mName;
    }
}
