/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Entities.PlayerEntity;
import Graphics.sGraphicsManager;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author alasdair
 */
public class RaceStartZone extends CheckPointZone
{
    Set<PlayerEntity> players;
    public RaceStartZone(int _x, int _y, int _x2, int _y2, CheckPointZone _nextCheckPoint)
    {
        super(_x, _y, _x2, _y2, 0, _nextCheckPoint);
        players = new HashSet<PlayerEntity>();
    }
    @Override
    public void enter(PlayerEntity _entity) 
    {
        players.add(_entity);
        _entity.placeCheckPoint(this);
    }
    @Override
    public void leave(PlayerEntity _entity) 
    {
        players.remove(_entity);
    }
    
    @Override
    public void renderRaceState()
    {
        sGraphicsManager.drawString("Run Forrest run! Time: " + mFrames/60.0f + " " + mCheckPointNumber + " checkpoints reached", 0f, 0);
    }
}
