/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Entities.PlayerEntity;
import Events.PlayerCreatedEvent;
import Events.RaceResetEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Level.sLevel;
import World.sWorld;
import java.util.Collection;
import java.util.LinkedList;
import org.newdawn.slick.Graphics;

/**
 *
 * @author alasdair
 */
public class RaceMode implements iGameMode, iEventListener
{
    RaceState mRaceState;
    Collection<PlayerEntity> players = new LinkedList<PlayerEntity>();
    int mTimer;
    public RaceMode()
    {
        mTimer = 0;
        mRaceState = new RaceState();
        try
        {
            sEvents.subscribeToEvent("PlayerCreatedEvent", this);
            sEvents.subscribeToEvent("RaceResetEvent", this);
        }
        finally
        {
            sEvents.unsubscribeToEvent("PlayerCreatedEvent", this);
        }
    }
    public iGameMode update(float _time)
    {
        mTimer++;
        mRaceState.update();
        sLevel.update();
        sWorld.update(_time);
        sEvents.processEvents();
        return this;
    }
    public void render(Graphics _graphics)
    {
        sWorld.getCamera().render();
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
            RaceResetEvent event = (RaceResetEvent)_event;
            for (PlayerEntity entity: players)
            {
                entity.resetRace();
            }
        }
        return true;
    }
}