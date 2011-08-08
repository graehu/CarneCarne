/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.FootballMode;

import Entities.Football;
import Entities.PlayerEntity;
import Events.AreaEvents.AreaEvent;
import Events.AreaEvents.GoalZone;
import Events.FootballSpawnEvent;
import Events.GoalSpawnEvent;
import Events.PlayerCreatedEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.sGraphicsManager;
import Level.sLevel;
import States.Game.iGameMode;
import World.sWorld;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Graphics;

/**
 *
 * @author alasdair
 */
public class FootballMode implements iGameMode, iEventListener
{
    int mTimer;
    Vec2 ballSpawnPosition;
    ArrayList<PlayerEntity> players = new ArrayList<PlayerEntity>();
    ArrayList<AreaEvent> goals = new ArrayList<AreaEvent>();
    ArrayList<Integer> scores = new ArrayList<Integer>();
    Football mFootball;
    FootballState mState;
    public FootballMode(String _level)
    {
        mTimer = 5 * 60 * 60;
        ballSpawnPosition = null;
        mState = new FootballNormalState(this, null);
        scores.add(0);
        scores.add(0);
        sEvents.subscribeToEvent("PlayerCreatedEvent", this);
        sEvents.subscribeToEvent("FootballSpawnEvent", this);
        sEvents.subscribeToEvent("GoalSpawnEvent", this);
        sLevel.newLevel(_level);
        for(int i = 0; i < players.size(); i++)
        {
            players.get(i).mSkin.activateSubSkin("Player" + (i+1), false, 0);
        }
    }
    
    public iGameMode update(Graphics _graphics, float _time)
    {
        mTimer--;
        if (mTimer == 0)
        {
            mState = new FootballWonState(this, scores.get(0), scores.get(1));
        }
        sLevel.update();
        sWorld.update(_graphics, _time);
        sEvents.processEvents();
        sWorld.queryForKicks();
        return this;
    }

    public void render(Graphics _graphics)
    {
        sWorld.getCamera().render(_graphics);
        mState.render(scores.get(0), scores.get(1));
        sGraphicsManager.drawString("Timeleft: " + getTimeString(mTimer), 0f, 0);
    }
    private String getTimeString(int _timer)
    {
        int seconds = _timer / 60;
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        return minutes + ":" + seconds;
        /*String timer = String.valueOf(_timer/60.0f);
        if (timer.length() > 5)
        {
            timer = timer.substring(0, 5);
        }
        else while (timer.length() < 5)
        {
            timer = timer + "0";
        }
        return timer;*/
    }

    public boolean trigger(iEvent _event)
    {
        if (_event.getName().equals("PlayerCreatedEvent"))
        {
            PlayerCreatedEvent event = (PlayerCreatedEvent)_event;
            event.getPlayer().setTeam(players.size() % 2);
            players.add(event.getPlayer());
            if (((FootballNormalState)mState).mFootball != null)
            {
                event.getPlayer().setFootball(((FootballNormalState)mState).mFootball);
            }
        }
        else if (_event.getName().equals("FootballSpawnEvent"))
        {
            FootballSpawnEvent event = (FootballSpawnEvent)_event;
            mState.spawnFootball(event.getFootball());
        }
        else if (_event.getName().equals("GoalSpawnEvent"))
        {
            GoalSpawnEvent event = (GoalSpawnEvent)_event;
            GoalZone zone = event.getGoalZone();
            goals.add(zone);
            zone.setGameMode(this);
        }
        return true;
    }

    public void score(int _team, Football _football)
    {
        mState = mState.score(_team, _football, players);
        sWorld.queryForKicks();
    }
    private void multiBall()
    {
        /*mFootball = null;
        ArrayList<Football> multiBall = new ArrayList<Football>();
        for (int i = 0; i < 3; i++)
        {
            footballDied(null);
            mMultiBall.add(mFootball);
        }
        mMultiBall = multiBall;
        mFootball = null;
        for (PlayerEntity player: players)
        {
            player.setFootball(null);
        }*/
    }
    public void footballDied(Football _football)
    {
        mState = mState.footballDied(_football);
    }
    
    public void cleanup() 
    {
        for(PlayerEntity player : players)
        {
            player.destroy();
            sWorld.destroyBody(player.getBody());
        }
        sEvents.unsubscribeToEvent("PlayerCreatedEvent", this);
        sEvents.unsubscribeToEvent("FootballSpawnEvent", this);
        sEvents.unsubscribeToEvent("GoalSpawnEvent", this);
    }
}
