/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.Adventure;

import Entities.PlayerEntity;
import Events.PlayerCreatedEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Skins.iSkin;
import Level.sLevel;
import States.Game.iGameMode;
import World.sWorld;
import java.util.Collection;
import java.util.LinkedList;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Aaron
 */
public class AdventureMode implements iGameMode, iEventListener
{
    Collection<PlayerEntity> players = new LinkedList<PlayerEntity>();
    int mTimer;
    public AdventureMode()
    {
        mTimer = 0;
        sEvents.subscribeToEvent("PlayerCreatedEvent", this);
        sEvents.subscribeToEvent("RaceResetEvent", this);
        sLevel.newLevel("Level1");
    }
    public iGameMode update(Graphics _graphics, float _time)
    {
        mTimer++;
        sLevel.update();
        sWorld.update(_graphics, _time);
        sEvents.processEvents();
        return this;
    }
    public void render(Graphics _graphics)
    {
        sWorld.getCamera().render(_graphics);
    }

    public boolean trigger(iEvent _event)
    {
        if (_event.getName().equals("PlayerCreatedEvent"))
        {
            PlayerCreatedEvent event = (PlayerCreatedEvent)_event;
            players.add(event.getPlayer());
        }
        else if (_event.getName().equals("RaceResetEvent"))
        {
            //on finish
        }
        return true;
    }
}
