/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import java.lang.String;
/**
 *
 * @author alasdair
 */
public interface iEvent {
    
    public String getName();
    public String getType(); /// This is the name of the event type, not used for hashing just to know.
}
