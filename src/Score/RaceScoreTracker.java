/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Score;

/**
 *
 * @author alasdair
 */
public class RaceScoreTracker extends ScoreTracker
{
    private static Integer[] getScoreValues()
    {
        Integer[] scoreValues = new Integer[ScoreEvent.eScoreEventsMax.ordinal()];
        scoreValues[ScoreEvent.eKilledOpponent.ordinal()] = 100;
        scoreValues[ScoreEvent.eDied.ordinal()] = -100;
        scoreValues[ScoreEvent.eWonRace.ordinal()] = 500;
        scoreValues[ScoreEvent.eLostRace.ordinal()] = -200;
        return scoreValues;
    }
    private static Integer[] mScoreValues = getScoreValues(); /// These are Integers instead of ints just so they can be null and therefore an error

    @Override
    public void scoreimpl(ScoreEvent _event)
    {
        mScore += mScoreValues[_event.ordinal()];
    }
    
}
