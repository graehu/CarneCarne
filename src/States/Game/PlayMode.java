/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.sEvents;
import World.sWorld;
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
        sWorld.update(_time);
        sEvents.processEvents();
    }
    public void render()
    {
        sLevel.renderBackground();
        sWorld.render();
        sLevel.renderForeground();
    }
}
