/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author alasdair
 */
public class KeyDownEvent implements iEvent{
    
    private char mKey;
    public KeyDownEvent(char _key)
    {
        mKey = _key;
    }
    public String getName()
    {
        return "KeyDownEvent" + mKey;
    }
    public String getType()
    {
        return "KeyDownEvent";
    }
    public char getKey()
    {
        return mKey;
    }
}
