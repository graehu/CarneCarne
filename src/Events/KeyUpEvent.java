/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author A203946
 */
public class KeyUpEvent implements iEvent {
    
    private char mKey;
    public KeyUpEvent(char _key)
    {
        mKey = _key;
    }
    
    public String getName()
    {
        return "KeyUpEvent" + mKey;
    }
    public String getType()
    {
        return "KeyUpEvent";
    }
    public char getKey()
    {
        return mKey;
    }
}
