/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.FootballMode;

import Entities.Football;
import Entities.PlayerEntity;
import Entities.sEntityFactory;
import Events.FootballSpawnEvent;
import Events.sEvents;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Graphics.sGraphicsManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class FootballNormalState extends FootballState
{
    int mLastRandomEventSpawn = 0;
    Football mFootball;
    Random mRand;
    private Vec2 ballSpawnPosition;
    private static final int mColonWidth = 25;
    private static final int mLetterHeight = 50;
    private static final int mLetterWidth = 50;
    iSkin mScoreColonRender;
    iSkin mDigitSkins[];
    public FootballNormalState(FootballMode _mode, Vec2 _ballSpawnPosition)
    {
        super(_mode, true);
        ballSpawnPosition = _ballSpawnPosition;
        HashMap params = new HashMap();
        params.put("ref", "Numbers/Colon");
        mScoreColonRender = sSkinFactory.create("static", params);
        mScoreColonRender.setDimentions(mColonWidth*2, mLetterHeight*2);
        
        mDigitSkins = new iSkin[2];
        for (int i = 0; i < mDigitSkins.length; i++)
        {
            params.put("ref", "Numbers/Number" + i);
            mDigitSkins[i] = sSkinFactory.create("static", params);
            mDigitSkins[i].setDimentions(mLetterWidth, mLetterHeight*2);
        }
        mRand = new Random(System.currentTimeMillis());
    }
    
    @Override
    void render(int _score1, int _score2)
    {
        try
        {
        Vec2 s = sGraphicsManager.getTrueScreenDimensions().mul(0.5f);
        int sX = (int)s.x;
        s.x -= mColonWidth;
        s.y -= mLetterHeight;
        mScoreColonRender.render(s.x, s.y);
        if (_score1 == 0)
        {
            s.x -= mLetterWidth;
            mDigitSkins[0].render(s.x, s.y);
        }
        else
        while (_score1 > 0)
        {
            int digit = _score1 % 10;
            _score1 /= 10;
            s.x -= mLetterWidth;
            mDigitSkins[digit].render(s.x, s.y);
        }
        s.x = sX;
        s.x += mColonWidth;
        int digits = 0;
        int score = _score2;
        while (_score2 > 0)
        {
            _score2 /= 10;
            digits++;
        }
        s.x += mLetterWidth*digits;
        while (score > 0)
        {
            int digit = score % 10;
            score /= 10;
            s.x -= mLetterWidth;
            mDigitSkins[digit].render(s.x, s.y);
        }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            
        }
    }

    @Override
    void spawnFootball(Football _football)
    {
        ballSpawnPosition = _football.getBody().getPosition().clone();
        _football.setGameMode(mMode);
        for (PlayerEntity player: mMode.players)
        {
            player.setFootball(_football);
        }
        mFootball = _football;
    }

    @Override
    FootballState score(int _team, Football _football, ArrayList<PlayerEntity> _players)
    {
        if (_football == mFootball)
        {
            mMode.scores.set(_team, mMode.scores.get(_team)+1);
            for (int i = 0; i < mMode.players.size(); i++)
            {
                if (i % mMode.goals.size() == _team)
                {
                    mMode.players.get(i).mScoreTracker.scoreGoal();
                }
            }
            mFootball.doom();
            mFootball = null;
            for (PlayerEntity player: _players)
            {
                player.resetRace();
            }
            mLastRandomEventSpawn++;
            if (mLastRandomEventSpawn == 3)
            {
                mLastRandomEventSpawn = 0;
                return randomEvent();
            }
        }
        return this;
    }
    private FootballState randomEvent()
    {
        int event = mRand.nextInt()%2;
        switch (event)
        {
            case 0:
            {
                return new FootballMultiballState(mMode, ballSpawnPosition);
            }
            case 1:
            {
                HashMap params = new HashMap();
                params.put("position", ballSpawnPosition);
                sEntityFactory.create("Broccoli", params);
                break;
            }
        }
        return this;
    }
    @Override
    FootballState footballDied(Football _football)
    {
        HashMap parameters = new HashMap();
        parameters.put("position",ballSpawnPosition);
        mFootball = (Football)sEntityFactory.create("Football",parameters);
        sEvents.triggerEvent(new FootballSpawnEvent(mFootball));
        return this;
    }
    
}
