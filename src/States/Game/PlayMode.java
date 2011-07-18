/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import org.newdawn.slick.Graphics;
import Events.sEvents;
import World.sWorld;
import Level.sLevel;
/**
 *
 * @author alasdair
 */
public class PlayMode implements iGameMode
{
    public PlayMode()
    {
        //sLevel.init();
        sLevel.newLevel();
    }
    public iGameMode update(float _time)
    {
        sLevel.update();
        sWorld.update(_time);
        sEvents.processEvents();
        return this;
    }
    public void render(Graphics _graphics)
    {
        sWorld.getCamera().render();
        
    }
}
