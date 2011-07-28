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
    Football(iSkin _skin)
    {
        super(_skin);
        mGameMode = null;
    }
    
    @Override
    public void update()
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

    public void setGameMode(FootballMode _gameMode)
    {
        mGameMode = _gameMode;
    }
    
    @Override
    public void kill(CauseOfDeath _cause, Object _killer)
    {
        mGameMode.footballDied(this);
        super.kill(_cause, _killer);
    }
}
