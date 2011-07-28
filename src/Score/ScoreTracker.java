/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Score;

import Entities.PlayerEntity;
import Events.NewHighScoreEvent;
import Events.sEvents;
import GUI.Components.GraphicalComponent;
import GUI.Components.Text;
import GUI.GUIManager;
import Graphics.Particles.sParticleManager;
import Graphics.sGraphicsManager;
import Utils.sFontLoader;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

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
    private int mWinnerTimer;
    private GraphicalComponent mTacoImage;
    private GraphicalComponent mWinnerImage;
    private Text mScoreText;


    public enum ScoreEvent
    {
        eKilledOpponent,
        eDied,
        eWonRace,
        eLostRace,
        eScoredGoal,
        eConceededGoal,
        eScoreEventsMax,
    }
    
    protected ScoreTracker(int _GUIManager) 
    {
        mTacoImage = (GraphicalComponent)GUIManager.use(_GUIManager)
                .createRootComponent(GUIManager.ComponentType.eGraphical, new Vector2f(1500,50), new Vector2f()); //FIXME: assumes 1680x1050
        mTacoImage.setImage("ui/HUD/taco.png");
        
        mWinnerImage = (GraphicalComponent)GUIManager.use(_GUIManager)
                .createRootComponent(GUIManager.ComponentType.eGraphical, new Vector2f(), new Vector2f());
        mWinnerImage.setImage("ui/HUD/winner.png");
        mWinnerImage.setIsVisible(false);
        
        mScoreText = new Text(sGraphicsManager.getGUIContext(), sFontLoader.createFont("GringoNights", 30), "SCORE", new Vector2f(30,40));
        mTacoImage.addChild(mScoreText);
        
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
        mWinnerImage.setIsVisible(true);
        mWinnerTimer = 180;
        sParticleManager.createFirework("Green", new Vec2(500,500), new Vec2(-5,0));
        sParticleManager.createFirework("Green", new Vec2(500,500), new Vec2(5,0));
        sParticleManager.createFirework("Green", new Vec2(500,500), new Vec2(0,-5));
        sParticleManager.createFirework("Green", new Vec2(500,500), new Vec2(0,5));
    }
    public void raceEnded()
    {
        //looser image here is req
    }

    public void update() 
    {
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
        mScoreText.setColor(stringColour);
        mScoreText.setTextString(String.valueOf(renderedScore));
        
        if (mWinnerImage.isVisible())
        {
            mWinnerTimer--;
            if (mWinnerTimer == 0)
                mWinnerImage.setIsVisible(false);
        }
    }
}
