/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Entities.PlayerEntity;
import Score.ScoreTracker.ScoreEvent;

/**
 *
 * @author alasdair
 */
public class RaceWonEvent extends iEvent
{
    PlayerEntity mEntity;
    int mTime;
    public RaceWonEvent(PlayerEntity _entity)
    {
        mEntity = _entity;
        mTime = mEntity.getRaceTimer();
        mEntity.mScoreTracker.score(ScoreEvent.eWonRace);
    }
    @Override
    public String getName()
    {
        return getType();
    }

    @Override
    public String getType()
    {
        return "RaceWonEvent";
    }
    
    public int getTime()
    {
        return mTime;
    }
}
