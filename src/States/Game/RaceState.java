/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.RaceResetEvent;
import Events.RaceWonEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import java.util.LinkedList;

/**
 *
 * @author alasdair
 */
class RaceState implements iEventListener
{
    LinkedList<Integer> raceTimes;
    private enum State
    {
        eRaceNotStarted,
        eRaceInProgress,
        eRaceWon,
        eStatesMax,
    }
    State mState;
    int mTimer;
    public RaceState()
    {
        mState = State.eRaceNotStarted;
        mTimer = 0;
        raceTimes = new LinkedList<Integer>();
        sEvents.subscribeToEvent("RaceWonEvent", this);
    }

    public boolean trigger(iEvent _event) 
    {
        if (_event.getType().equals("RaceWonEvent"))
        {
            RaceWonEvent event = (RaceWonEvent)_event;
            raceTimes.add(event.getTime());
            changeState(State.eRaceWon);
        }
        return true;
    }
    
    public void update()
    {
        mTimer++;
        switch (mState)
        {
            case eRaceWon:
            {
                if (mTimer == 30)
                {
                    changeState(State.eRaceNotStarted);
                }
                break;
            }
        }
    }
    private void changeState(State _state)
    {
        switch (_state)
        {
            case eRaceNotStarted:
            {
                sEvents.triggerEvent(new RaceResetEvent());
                break;
            }
            case eRaceWon:
            {
                mTimer = 0;
                break;
            }
        }
        mState = _state;
    }
    
}
