/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Entities.PlayerEntity;
import Events.PlayerCreatedEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Level.sLevel;
import States.Game.iGameMode;
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
    Collection<PlayerEntity> players = new LinkedList<PlayerEntity>();
    int mTimer;
    public RaceMode()
    {
        mTimer = 0;
    }
    public void init()
    {
        sEvents.subscribeToEvent("PlayerCreatedEvent", this);
    }
    public void update(float _time)
    {
        mTimer++;
        sLevel.update();
        sWorld.update(_time);
        sEvents.processEvents();
    }
    public void render(Graphics _graphics)
    {
        sWorld.getCamera().render();
    }

    public void trigger(iEvent _event)
    {
        PlayerCreatedEvent event = (PlayerCreatedEvent)_event;
        players.add(event.getPlayer());
    }
}