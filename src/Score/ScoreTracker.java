/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Score;

import Entities.PlayerEntity;
import Events.NewHighScoreEvent;
import Events.sEvents;
import Graphics.Particles.sParticleManager;
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
    private int mBestEverTime;
    private float mRenderedScore;
    private iSkin mSkin;
    private iSkin mWinnerSkin;
    private int mWinnerTimer;


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
        mBestTime = mBestEverTime = Integer.MAX_VALUE;
    }
    
    public void score(ScoreEvent _event)
    {
        if (_event.equals(ScoreEvent.eWonRace))
        {
            throw new UnsupportedOperationException("Call winRace instead, not this directly");
        }
        scoreimpl(_event);
    }
    abstract public void scoreimpl(ScoreEvent _event);
    public void winRace(int _time, PlayerEntity _player)
    {
        scoreimpl(ScoreEvent.eWonRace);
        if (_time < mBestTime)
        {
            mBestTime = _time;
            if (_time < mBestEverTime)
            {
                mBestEverTime = _time;
                sEvents.triggerEvent(new NewHighScoreEvent(_time, _player));
            }
        }
        HashMap params = new HashMap();
        params.put("ref", "winner");
        mWinnerSkin = sSkinFactory.create("static", params);
        mWinnerTimer = 180;
        sParticleManager.createFirework("Green", new Vec2(500,500), new Vec2(-5,0));
        sParticleManager.createFirework("Green", new Vec2(500,500), new Vec2(5,0));
        sParticleManager.createFirework("Green", new Vec2(500,500), new Vec2(0,-5));
        sParticleManager.createFirework("Green", new Vec2(500,500), new Vec2(0,5));
    }
    public void raceEnded()
    {
        if (mWinnerSkin == null)
        {
            HashMap params = new HashMap();
            params.put("ref", "loser");
            mWinnerSkin = sSkinFactory.create("static", params);
            mWinnerTimer = 180;
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
        
        if (mWinnerSkin != null)
        {
            mWinnerSkin.render(0, 0);
            mWinnerTimer--;
            if (mWinnerTimer == 0)
            {
                mWinnerSkin = null;
            }
        }
    }
}
