/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Events.AreaEvents.CheckPointZone;
import Events.AreaEvents.GoalZone;
import Graphics.Skins.iSkin;
import States.Game.FootballMode.FootballMode;
import World.sWorld;
import org.jbox2d.dynamics.contacts.ContactEdge;

/**
 *
 * @author alasdair
 */
public class Football extends Pea
{
    FootballMode mGameMode;
    int mDoomTimer;
    Football(iSkin _skin)
    {
        super(_skin);
        mGameMode = null;
        mDoomTimer = 0;
    }
    
    @Override
    public void update()
    {
        if (mDoomTimer != 0)
        {
            mDoomTimer--;
            if (mDoomTimer == 0)
            {
                kill(CauseOfDeath.eMundane, this);
                mBody = null;
            }
        }
        else
        {
            ContactEdge contact = mBody.getContactList();
            while (contact != null)
            {
                if (contact.other.getFixtureList().getFilterData().categoryBits == (1 << sWorld.BodyCategories.eCheckPoint.ordinal()))
                {
                    try
                    {
                        GoalZone zone = (GoalZone)contact.other.getUserData();
                        zone.enter(this);
                    }
                    catch (ClassCastException e)
                    {

                    }
                }
                contact = contact.next;
            }
        }
    }

    public void setGameMode(FootballMode _gameMode)
    {
        mGameMode = _gameMode;
    }
    
    @Override
    public void kill(CauseOfDeath _cause, Object _killer)
    {
        if (mBody != null)
        {
            mGameMode.footballDied(this);
            super.kill(_cause, _killer);
        }
    }

    public void doom()
    {
        mDoomTimer = 120;
    }
}
