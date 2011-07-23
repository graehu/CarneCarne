/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Score;

import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Graphics.sGraphicsManager;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;

/**
 *
 * @author alasdair
 */
abstract public class ScoreTracker
{
    protected int mScore;
    private int mBestTime;
    private float mRenderedScore;
    private iSkin mSkin;

    public enum ScoreEvent
    {
        eKilledOpponent,
        eDied,
        eWonRace,
        eLostRace,
        eScoreEventsMax
    }
    
    protected ScoreTracker()
    {
        HashMap params = new HashMap();
        params.put("ref", "taco");
        mSkin = sSkinFactory.create("static", params);
        mRenderedScore = mScore = 0;
        mBestTime = Integer.MAX_VALUE;
    }
    
    abstract public void score(ScoreEvent _event);
    public void winRace(int _time)
    {
        score(ScoreEvent.eWonRace);
        if (_time < mBestTime)
        {
            mBestTime = _time;
        }
    }
    public void render()
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        mSkin.render(s.x-143, s.y-94);
        
        Color stringColour = Color.white;
        int renderedScore = (int)mRenderedScore;
        if (mRenderedScore < mScore)
        {
            mRenderedScore += (float)(mScore - mRenderedScore)*0.1f;
            if (mRenderedScore - (float)renderedScore > 0.5f)
            {
                renderedScore++;
            }
            stringColour = Color.green;
        }
        else if (mRenderedScore > mScore)
        {
            mRenderedScore += (float)(mScore - mRenderedScore)*0.1f;
            if (mRenderedScore - (float)renderedScore < -0.5f)
            {
                renderedScore--;
            }
            stringColour = Color.red;
        }
        if (renderedScore == mScore)
            stringColour = Color.white;
        sGraphicsManager.drawString(String.valueOf(renderedScore), 0.9f, 0.9f, stringColour);
    }
}
