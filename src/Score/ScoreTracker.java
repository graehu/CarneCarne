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
import org.newdawn.slick.Font;
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
    private int mGUIManager;
    private GraphicalComponent mTacoImage;
    private GraphicalComponent mRaceWinnerImage;
    private GraphicalComponent mDisplayComponent;
    private Text mScoreText;
    Font mScoreDisplay;
    PlayerEntity mEntity;

    public void render()
    {
        
    }


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
    
    protected ScoreTracker(int _GUIManager, PlayerEntity _entity) 
    {
        mGUIManager = _GUIManager;
        mTacoImage = (GraphicalComponent)GUIManager.use(_GUIManager)
                .createRootComponent(GUIManager.ComponentType.eGraphical, new Vector2f(143+30,30), new Vector2f()); //FIXME: assumes 1680x1050
        mTacoImage.setImage("ui/HUD/taco.png");
        mTacoImage.setDimentionsToImage();
        mTacoImage.setAlignment(true, false);
        mTacoImage.setMaintainRatio(true);
        
        mScoreText = new Text(sGraphicsManager.getGUIContext(), sFontLoader.createFont("score", 30), "SCORE", new Vector2f(30,40), false);
        mTacoImage.addChild(mScoreText);
        
        mRenderedScore = mScore = 0;
        mBestTime = mBestEverTime = Integer.MAX_VALUE;
        //loadWinnerImage(1);
        mEntity = _entity;
    }

    private void loadWinnerImage(int _position)
    {
        if (mRaceWinnerImage == null)
        {
            mDisplayComponent = mRaceWinnerImage = (GraphicalComponent)GUIManager.use(mGUIManager)
                    .createRootComponent(GUIManager.ComponentType.eGraphical, new Vector2f(200, 200), new Vector2f(200, 200));
            mRaceWinnerImage.setImage("ui/HUD/finishedRaceAtPosition" + _position + ".png");
            mRaceWinnerImage.setDimentionsToImage();
            mRaceWinnerImage.setIsVisible(false);
            mScoreDisplay = sFontLoader.createFont("score");
            mBestTime = mEntity.getRaceTimer();
        }
    }
    public void score(ScoreEvent _event)
    {
        if (_event.equals(ScoreEvent.eWonRace) || _event.equals(ScoreEvent.eScoredGoal))
        {
            throw new UnsupportedOperationException("Call winRace or scoregoal instead, not this directly");
        }
        scoreimpl(_event);
    }
    abstract public void scoreimpl(ScoreEvent _event);
    public void finish(int _time, PlayerEntity _player, int _position)
    {
        loadWinnerImage(_position);
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
        mRaceWinnerImage.setIsVisible(true);
        mWinnerTimer = 180;
        sParticleManager.createFirework("Green", _player.getBody().getPosition().mul(64).add(new Vec2(32, 80)), new Vec2(0.5f,-3));
        sParticleManager.createFirework("Green", _player.getBody().getPosition().mul(64).add(new Vec2(32, 80)), new Vec2(1.5f,-2));
        sParticleManager.createFirework("Green", _player.getBody().getPosition().mul(64).add(new Vec2(32, 80)), new Vec2(-0.5f,-3));
        sParticleManager.createFirework("Green", _player.getBody().getPosition().mul(64).add(new Vec2(32, 80)), new Vec2(-1.5f,-2));
    }
    public void scoreGoal()
    {
        scoreimpl(ScoreEvent.eScoredGoal);
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
        
        try
        {
            if (mDisplayComponent.isVisible())
            {
                mScoreDisplay.drawString(0, 0.5f, "Time: " + mBestTime);
                mWinnerTimer--;
                if (mWinnerTimer == 0)
                    mDisplayComponent.setIsVisible(false);
            }
            
        }
        catch (NullPointerException e)
        {
        }
    }
}
