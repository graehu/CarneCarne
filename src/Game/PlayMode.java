/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Physics.sPhysics;
import Level.sLevel;
/**
 *
 * @author alasdair
 */
public class PlayMode implements iGameMode {
    
    public PlayMode()
    {
        
    }
    public void update(float _time)
    {
        sLevel.update();
        sPhysics.update(_time);
    }
    public void render()
    {
        sLevel.render();
        sPhysics.render();
    }
}
