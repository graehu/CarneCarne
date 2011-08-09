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
import Graphics.Skins.sSkinFactory;
import Level.sLevel;
import States.Game.iGameMode;
import World.sWorld;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
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
    iSkin mTaco = null;
    Queue<String> mNextLevelProgression;
    public AdventureMode(Queue<String> _nextLevelProgression)
    {
        HashMap params = new HashMap();
        params.put("ref", "tacoEnd");
        mTaco = sSkinFactory.create("animated", params);
        mTimer = 0;
        sEvents.subscribeToEvent("PlayerCreatedEvent", this);
        sEvents.subscribeToEvent("RaceWonEvent", this);
        mNextLevelProgression = new ArrayDeque<String>(_nextLevelProgression);
        /*nextMap = */sLevel.newLevel(mNextLevelProgression.remove());
    }
    public iGameMode update(Graphics _graphics, float _time)
    {
        if(mIsCompleted)
        {
            mTimer = 0;
            mIsCompleted = false;
            cleanup();
            /*if(nextMap == null)
            {
                //FIXME: EXIT TO TITLE
            }
            else
                nextMap = sLevel.newLevel(nextMap);*/
            if (mNextLevelProgression.isEmpty())
            {
                //FIXME: EXIT TO TITLE
                sLevel.newLevel("Level1"); /// This is le coon
            }
            else
            {
                sLevel.newLevel(mNextLevelProgression.remove());
            }
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
