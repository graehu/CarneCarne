/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.AIEntity;
import Entities.PlayerEntity;
import Events.RaceWonEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Sound.SoundScape;
import Sound.sSound;

/**
 *
 * @author alasdair
 */
public class RaceEndZone extends CheckPointZone implements iEventListener
{
    private PlayerEntity mWinner;
    private int mFinishedPlayers = 0;
    public RaceEndZone(int _x, int _y, int _x2, int _y2, int _numCheckPoints)
    {
        super(_x, _y, _x2, _y2, _numCheckPoints, null);
        sEvents.subscribeToEvent("newLevel", this);
        sEvents.subscribeToEvent("BarrierOpenEvent" + "StartGate", this);
        mWinner = null;
    }
    @Override
    public void renderRaceState(int _raceTimer)
    {
       // sGraphicsManager.drawString("You won the race in " + getTimeString(_raceTimer) + " seconds.", 0f, 0);
    }
    @Override
    public void enter(AIEntity _entity)
    {
        mFinishedPlayers++;
        if (mWinner == null)
        {
            _entity.placeCheckPoint(this);
            mWinner = (PlayerEntity)_entity;
            sSound.play(SoundScape.Sound.eRaceWin, 0);
            sEvents.triggerDelayedEvent(new RaceWonEvent(mWinner));
        }
        else
        {
            _entity.placeCheckPoint(new RaceLostFakeZone(mCheckPointNumber, mFinishedPlayers));
        }
        mWinner.mScoreTracker.finish(mWinner.getRaceTimer(), mWinner, mFinishedPlayers);
    }
    @Override
    public boolean incrementRaceTimer()
    {
        return false;
    }

    public boolean trigger(iEvent _event)
    {
        if(_event.getName().equals("BarrierOpenEvent" + "StartGate"))
        {
            mWinner = null;
            mFinishedPlayers = 0;
        }
        else if(_event.getType().equals("newLevel"))
        {
            sEvents.unsubscribeToEvent("newLevel", this);
            sEvents.unsubscribeToEvent("BarrierOpenEvent" + "StartGate", this);
        }
        return true;
    }
}
