/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.Adventure;

import Entities.Entity.CauseOfDeath;
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
    boolean mIsCompleted = false;
    int mTimer;
    String nextMap = null;
    public AdventureMode(String _level)
    {
        mTimer = 0;
        sEvents.subscribeToEvent("PlayerCreatedEvent", this);
        sEvents.subscribeToEvent("RaceWonEvent", this);
        nextMap = sLevel.newLevel(_level);
    }
    public iGameMode update(Graphics _graphics, float _time)
    {
        if(mIsCompleted)
        {
            mTimer = 0;
            mIsCompleted = false;
            cleanup();
            if(nextMap == null)
            {
                //FIXME: EXIT TO TITLE
            }
            else
                nextMap = sLevel.newLevel(nextMap);
        }
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
        else if (_event.getName().equals("RaceWonEvent"))
        {
            //on finish
            mIsCompleted = true;
            
        }
        return true;
    }

    public void cleanup()
    {
        for(PlayerEntity player : players)
        {
            player.destroy();
            sWorld.destroyBody(player.getBody());
        }
        players.clear();
        sEvents.unsubscribeToEvent("PlayerCreatedEvent", this);
        sEvents.unsubscribeToEvent("RaceWonEvent", this);
    }
}
