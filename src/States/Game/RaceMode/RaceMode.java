/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.RaceMode;

import Entities.PlayerEntity;
import Events.PlayerCreatedEvent;
import Events.RaceResetEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Graphics.sGraphicsManager;
import Level.sLevel;
import States.Game.iGameMode;
import World.sWorld;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import org.jbox2d.common.Vec2;
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
    iSkin mRaceRender = null;
    public RaceMode(boolean _initLevel)
    {
        mTimer = 0;
        mRaceState = new RaceState();
        sEvents.subscribeToEvent("PlayerCreatedEvent", this);
        sEvents.subscribeToEvent("RaceResetEvent", this);
        sEvents.subscribeToEvent("RaceStartEvent", this);
        if (_initLevel)
        {
            sLevel.init();
        }
        sLevel.newLevel();
    }
    public iGameMode update(Graphics _graphics, float _time)
    {
        mTimer++;
        mRaceState.update();
        sLevel.update();
        sWorld.update(_graphics, _time);
        sEvents.processEvents();
        return this;
    }
    public void render(Graphics _graphics)
    {
        sWorld.getCamera().render(_graphics);
        mRaceState.renderState();
        if (mRaceRender != null)
        {
            Vec2 dimensions = new Vec2(272, 269);
            mRaceRender.setDimentions(dimensions.x, dimensions.y);
            Vec2 s = sGraphicsManager.getTrueScreenDimensions();
            mRaceRender.render(0.5f*(s.x-dimensions.x), 0.5f*(s.y-dimensions.y));
            if (mTimer == 120)
            {
                mRaceRender = null;
            }
        }
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
        else if (_event.getName().equals("RaceStartEvent"))
        {
            HashMap map = new HashMap();
            map.put("ref", "CountdownGo");
            mRaceRender = sSkinFactory.create("static", map);
            mTimer = 0;
        }
        return true;
    }
}