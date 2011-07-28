/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.FootballMode;

import Entities.AIEntity;
import Entities.Entity.CauseOfDeath;
import Entities.Football;
import Entities.PlayerEntity;
import Entities.sEntityFactory;
import Events.AreaEvents.AreaEvent;
import Events.AreaEvents.GoalZone;
import Events.FootballSpawnEvent;
import Events.GoalSpawnEvent;
import Events.PlayerCreatedEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Level.sLevel;
import Score.ScoreTracker.ScoreEvent;
import States.Game.iGameMode;
import World.sWorld;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
    public FootballMode()
    {
        mTimer = 0;
        ballSpawnPosition = null;
        sEvents.subscribeToEvent("PlayerCreatedEvent", this);
        sEvents.subscribeToEvent("FootballSpawnEvent", this);
        sEvents.subscribeToEvent("GoalSpawnEvent", this);
        sLevel.newLevel("The_Match");
    }
    
    public iGameMode update(Graphics _graphics, float _time)
    {
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
            scores.add(0);
        }
        else if (_event.getName().equals("FootballSpawnEvent"))
        {
            FootballSpawnEvent event = (FootballSpawnEvent)_event;
            mFootball = event.getFootball();
            ballSpawnPosition = mFootball.getBody().getPosition().clone();
            mFootball.setGameMode(this);
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
        if (_football == mFootball)
        {
            mFootball.doom();
            mFootball = null;
            scores.set(_team, scores.get(_team)+1);
            PlayerEntity scoredPlayer = players.get(_team);
            scoredPlayer.mScoreTracker.score(ScoreEvent.eScoredGoal);
            for (PlayerEntity player: players)
            {
                if (player != scoredPlayer)
                {
                    player.mScoreTracker.score(ScoreEvent.eConceededGoal);
                }
            }
        }
    }
    public void footballDied(Football _football)
    {
        HashMap parameters = new HashMap();
        parameters.put("position",ballSpawnPosition);
        sEvents.triggerEvent(new FootballSpawnEvent((Football)sEntityFactory.create("Football",parameters)));
    }
}
