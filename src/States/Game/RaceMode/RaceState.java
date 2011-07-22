/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.RaceMode;

import Events.RaceResetEvent;
import Events.RaceStartEvent;
import Events.RaceWonEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Graphics.sGraphicsManager;
import java.util.HashMap;
import java.util.LinkedList;
import org.jbox2d.common.Vec2;

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
        eRaceCountDown,
        eRaceInProgress,
        eRaceWon,
        eStatesMax,
    }
    iSkin mCountDownRender;
    State mState;
    int mTimer;
    public RaceState()
    {
        mState = State.eRaceNotStarted;
        mTimer = 0;
        raceTimes = new LinkedList<Integer>();
        sEvents.subscribeToEvent("RaceWonEvent", this);
        sEvents.subscribeToEvent("RaceCountdownStartEvent", this);
        sEvents.subscribeToEvent("RaceCountdownInterruptEvent", this);
    }

    public boolean trigger(iEvent _event)
    {
        if (_event.getType().equals("RaceWonEvent"))
        {
            RaceWonEvent event = (RaceWonEvent)_event;
            raceTimes.add(event.getTime());
            changeState(State.eRaceWon);
        }
        else if (_event.getType().equals("RaceCountdownStartEvent"))
        {
            changeState(State.eRaceCountDown);
            HashMap params = new HashMap();
            params.put("ref", "Countdown3");
            mCountDownRender = sSkinFactory.create("static", params);
            mTimer = 1;
        }
        else if (_event.getType().equals("RaceCountdownInterruptEvent"))
        {
            mCountDownRender = null;
            mState = State.eRaceNotStarted;
        }
        return true;
    }
    
    /// Properties for the countdown animation
    static final int phase1CompleteTime = 20;
    static final float phase1EndSize = 0.8f; /// Size of the sprite at the end of phase 1
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
            case eRaceCountDown:
            {
                if (mTimer == 180)
                {
                    changeState(State.eRaceInProgress);
                    sEvents.triggerEvent(new RaceStartEvent());
                }
                else
                {
                    int mod60 = mTimer % 60;
                    if (mod60 == 0)
                    {
                        int time = 3-(mTimer /60);
                        HashMap params = new HashMap();
                        params.put("ref", "Countdown" + time);
                        mCountDownRender = sSkinFactory.create("static", params);
                    }
                }
            }
        }
    }
    void renderState()
    {
        if (mState.equals(State.eRaceCountDown))
        {
            int mod60 = mTimer % 60;
            float scale = 1.0f;
            if (mod60 < phase1CompleteTime)
            {
                float time = (float)(mod60)/(float)(phase1CompleteTime);
                scale = phase1EndSize * time;
            }
            else
            {
                float time = (float)(mod60-phase1CompleteTime)/(float)(60-phase1CompleteTime);
                scale = ((1.0f-phase1EndSize) * time) + phase1EndSize;
            }
            if (mod60 != 0)
            {
                sGraphicsManager.removeClip();
                Vec2 dimensions = new Vec2(scale*(float)272, scale*(float)269);
                mCountDownRender.setDimentions(dimensions.x, dimensions.y);
                Vec2 s = sGraphicsManager.getTrueScreenDimensions();
                mCountDownRender.render(0.5f*(s.x-dimensions.x), 0.5f*(s.y-dimensions.y));
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
                mTimer = 0;
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
