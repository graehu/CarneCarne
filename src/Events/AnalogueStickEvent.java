/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author alasdair
 */
public class AnalogueStickEvent extends iEvent {
    
    private float mValue;
    public AnalogueStickEvent(float _value)
    {
        mValue = _value;
    }
    
    public float getValue()
    {
        return mValue;
    }

    @Override
    public String getName() {
        return "AnalogueStickEvent";
    }

    @Override
    public String getType() {
        return getName();
    }
    
}
