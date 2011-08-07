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
    int level = 1;
    public AdventureMode()
    {
        mTimer = 0;
        sEvents.subscribeToEvent("PlayerCreatedEvent", this);
        sEvents.subscribeToEvent("RaceWonEvent", this);
        //sLevel.newLevel("Level"+level);
        sLevel.newLevel("single");
    }
    public iGameMode update(Graphics _graphics, float _time)
    {
        if(mIsCompleted)
        {
            mIsCompleted = false;
            level++;
            sLevel.newLevel("Level"+level);
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
}
