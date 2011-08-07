/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.FootballMode;

import Entities.Football;
import Entities.PlayerEntity;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Graphics.sGraphicsManager;
import java.util.ArrayList;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class FootballWonState extends FootballState
{
    private iSkin mDisplay;
    public FootballWonState(FootballMode _mode, int _score1, int _score2)
    {
        super(_mode, false);
        HashMap params = new HashMap();
        if (_score1 > _score2)
        {
            params.put("ref", "RedWon");
        }
        else if (_score1 == _score2)
        {
            params.put("ref", "Draw");
        }
        else
        {
            params.put("ref", "BlueWon");
        }
        mDisplay = sSkinFactory.create("static", params);
    }
    
    @Override
    void render(int _score1, int _score2)
    {
        Vec2 s = sGraphicsManager.getTrueScreenDimensions().mul(0.5f);
        mDisplay.render(s.x, s.x);
    }

    @Override
    void spawnFootball(Football _football)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    FootballState score(int _team, Football _football, ArrayList<PlayerEntity> _players)
    {
        return this;
    }

    @Override
    FootballState footballDied(Football _football)
    {
        for (PlayerEntity player: mMode.players)
        {
            player.setFootball(null);
        }
        return this;
    }
    
}
