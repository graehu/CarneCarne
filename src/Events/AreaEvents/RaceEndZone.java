/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events.AreaEvents;

import Graphics.sGraphicsManager;

/**
 *
 * @author alasdair
 */
public class RaceEndZone extends CheckPointZone
{
    public RaceEndZone(int _x, int _y, int _x2, int _y2, int _numCheckPoints)
    {
        super(_x, _y, _x2, _y2, _numCheckPoints, null);
    }
    public void renderRaceState()
    {
        sGraphicsManager.drawString("You are teh winnar!", 0f, 0);
    }
}
