/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.PlayerEntity;
import Events.RaceResetEvent;
import Events.RaceStartEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.sGraphicsManager;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author alasdair
 */
public class RaceStartZone extends CheckPointZone implements iEventListener
{
    Map<PlayerEntity, CheckPointZone> mPlayers;
    int mNumPlayers;
    boolean mRaceHasStarted;
    public RaceStartZone(int _x, int _y, int _x2, int _y2, CheckPointZone _nextCheckPoint, int _numPlayers)
    {
        super(_x, _y, _x2, _y2, 0, _nextCheckPoint);
        mPlayers = new HashMap<PlayerEntity, CheckPointZone>();
        mNumPlayers = _numPlayers;
        mRaceHasStarted = false;
        sEvents.subscribeToEvent("RaceResetEvent", this);
    }
    @Override
    public void enter(PlayerEntity _entity) 
    {
        assert(_entity.getCheckPoint() != this);
        mPlayers.put(_entity, _entity.getCheckPoint());
        _entity.placeCheckPoint(this);
    }
    @Override
    public void leave(PlayerEntity _entity) 
    {
        //_entity.placeCheckPoint(mPlayers.get(_entity));
        if (!mRaceHasStarted)
            _entity.getToStartingZone();
        mPlayers.remove(_entity);
    }
    @Override
    public boolean incrementRaceTimer()
    {
        if (mRaceHasStarted)
        {
            return true;
        }
        mRaceHasStarted = mPlayers.size() == mNumPlayers;
        if (mRaceHasStarted)
        {
            sEvents.triggerEvent(new RaceStartEvent());
        }
        return mRaceHasStarted;
    }
    @Override
    public void renderRaceState(int _raceTimer)
    {
        if (mRaceHasStarted)
        {
            super.renderRaceState(_raceTimer);
        }
        else
        {
            sGraphicsManager.drawString("Waiting for all players to be ready.", 0f, 0);
        }
    }

    public boolean trigger(iEvent _event)
    {
        RaceResetEvent event = (RaceResetEvent)_event;
        mRaceHasStarted = false;
        return true;
    }
}
