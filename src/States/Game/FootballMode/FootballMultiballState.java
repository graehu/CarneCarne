/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.FootballMode;

import Entities.Football;
import Entities.PlayerEntity;
import Entities.sEntityFactory;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Graphics.sGraphicsManager;
import java.util.ArrayList;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Font;

/**
 *
 * @author alasdair
 */
public class FootballMultiballState extends FootballState
{
    private ArrayList<Football> mBalls;
    private iSkin mSkin;
    private float mSkinPosition;
    private Vec2 mBallSpawnPosition;
    iSkin mGoalScoreRenders[];
    iSkin mGoalScoreRender;
    Font mFont;
    int mGoalScoreRenderTimer;
    public FootballMultiballState(FootballMode _mode, Vec2 _ballSpawnPosition, Font _font)
    {
        super(_mode, false);
        mBallSpawnPosition = _ballSpawnPosition;
        mBalls = new ArrayList<Football>();
        HashMap parameters = new HashMap();
        
        parameters.put("position",mBallSpawnPosition);
        for (int i = 0; i < 3; i++)
        {
            Football football = (Football)sEntityFactory.create("Football",parameters);
            football.setGameMode(mMode);
            mBalls.add(football);
        }
        for (PlayerEntity player: mMode.players)
        {
            player.setFootball(null);
        }
        HashMap params = new HashMap();
        params.put("ref", "MultiBall");
        mSkin = sSkinFactory.create("static", params);
        mSkinPosition = 0;
        
        mGoalScoreRenders = new iSkin[2];
        params.put("ref", "Goal0");
        mGoalScoreRenders[0] = sSkinFactory.create("static", params);
        params.put("ref", "Goal1");
        mGoalScoreRenders[1] = sSkinFactory.create("static", params);
        mGoalScoreRender = null;
        
        mFont = _font;
    }
    
    @Override
    void render(int _score1, int _score2)
    {
        Vec2 s = sGraphicsManager.getTrueScreenDimensions().mul(0.5f);
        s.y -= mFont.getHeight(_score1 + ":" + _score2)*0.65f ;
        s.x -= mFont.getWidth(_score1 + " ") + (mFont.getWidth(":")*0.5f);
        mFont.drawString(s.x, s.y, _score1 + " ");
        s.x += mFont.getWidth(_score1 + " ");
        mFont.drawString(s.x, s.y,":");
        s.x += mFont.getWidth(":") + mFont.getWidth(" ");
        mFont.drawString(s.x, s.y, String.valueOf(_score2));
        if (mSkin != null)
        {
            mSkin.render(mSkinPosition, 0);
            mSkinPosition += sGraphicsManager.getTrueScreenDimensions().x / 1000.0f;
            if (mSkinPosition > sGraphicsManager.getTrueScreenDimensions().x)
            {
                mSkin = null;
            }
        }
        if (mGoalScoreRender != null)
        {
            s = sGraphicsManager.getTrueScreenDimensions().mul(0.5f);
            Vec2 dims = new Vec2(1148, 471);
            s = s.sub(dims.mul(0.5f));
            mGoalScoreRender.render(s.x, s.y);
            mGoalScoreRenderTimer--;
            if (mGoalScoreRenderTimer == 0)
            {
                mGoalScoreRender = null;
            }
        }
    }

    @Override
    void spawnFootball(Football _football)
    {
        throw new UnsupportedOperationException("Shouldn't be spawning footballs");
    }

    @Override
    FootballState score(int _team, Football _football, ArrayList<PlayerEntity> _players)
    {
        if (mBalls.contains(_football))
        {
            mGoalScoreRender = mGoalScoreRenders[_team];
            mGoalScoreRenderTimer = 120;
            mBalls.remove(_football);
            mMode.scores.set(_team, mMode.scores.get(_team)+1);
            for (int i = 0; i < mMode.players.size(); i++)
            {
                if (i % mMode.goals.size() == _team)
                {
                    mMode.players.get(i).mScoreTracker.scoreGoal();
                }
            }
            _football.doom();
        }
        if (mBalls.isEmpty())
        {
            return new FootballNormalState(mMode, mBallSpawnPosition, _team);
        }
        return this;
    }

    @Override
    FootballState footballDied(Football _football)
    {
        mBalls.remove(_football);
        if (mBalls.isEmpty())
        {
            return new FootballNormalState(mMode, mBallSpawnPosition, -1);
        }
        return this;
    }
    
}
