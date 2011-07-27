/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;

/**
 *
 * @author alasdair
 */
public class Carcass extends Entity
{
    int mTimer;
    Object mKilledMe;
    public Carcass(iSkin _skin, Object _killedMe)
    {
        super(_skin);
        mTimer = 0;
        mKilledMe = _killedMe;
    }
    @Override
    public void update()
    {
    }
    
    @Override
    public void kill(CauseOfDeath _causeOfDeath, Object _killer)
    {
        if (mKilledMe != _killer)
        {
            super.kill(_causeOfDeath, _killer);
        }
    }
}
