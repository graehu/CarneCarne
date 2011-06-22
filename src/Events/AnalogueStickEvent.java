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
    private int mPlayer;
    public AnalogueStickEvent(float _value, int _player)
    {
        mValue = _value;
        mPlayer = _player;
    }
    
    public float getValue()
    {
        return mValue;
    }

    @Override
    public String getName()
    {
        return getType() + mPlayer;
    }

    @Override
    public String getType()
    {
        return "AnalogueStickEvent";
    }
    
}
