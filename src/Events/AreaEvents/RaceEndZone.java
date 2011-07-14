/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.PlayerEntity;
import Events.RaceResetEvent;
import Events.RaceWonEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.sGraphicsManager;

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
        mWinner = null;
        sEvents.subscribeToEvent("RaceResetEvent", this);
    }
    @Override
    public void renderRaceState(int _raceTimer)
    {
        sGraphicsManager.drawString("You won the race in " + getTimeString(_raceTimer) + " seconds.", 0f, 0);
    }
    @Override
    public void enter(PlayerEntity _entity)
    {
        mFinishedPlayers++;
        if (mWinner == null)
        {
            _entity.placeCheckPoint(this);
            mWinner = _entity;
            sEvents.triggerDelayedEvent(new RaceWonEvent(mWinner));
        }
        else
        {
            _entity.placeCheckPoint(new RaceLostFakeZone(mCheckPointNumber, mFinishedPlayers));
        }
    }
    @Override
    public boolean incrementRaceTimer()
    {
        return false;
    }

    public boolean trigger(iEvent _event)
    {
        RaceResetEvent event = (RaceResetEvent)_event;
        mWinner = null;
        return true;
    }
}
