/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import World.sWorld.BodyCategories;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author alasdair
 */
public class WorldContactListener implements ContactListener{

    iListener reactions[][];
    public WorldContactListener()
    {
        reactions = new iListener[BodyCategories.eBodyCategoriesMax.ordinal()][BodyCategories.eBodyCategoriesMax.ordinal()];
        iListener defaultListener = new NullListener();
        for (int i = 0; i < BodyCategories.eBodyCategoriesMax.ordinal(); i++)
        {
            for (int ii = 0; ii < BodyCategories.eBodyCategoriesMax.ordinal(); ii++)
            {
                reactions[i][ii] = defaultListener;
            }
        }
        for (int i = 0; i < BodyCategories.eBodyCategoriesMax.ordinal(); i++)
        {
            set(BodyCategories.eWater.ordinal(),i,new WaterListener());
            set(BodyCategories.eTar.ordinal(),i,new TarListener());
        }
        set(BodyCategories.eTar.ordinal(),BodyCategories.eFire.ordinal(),new TarFireListener());
        set(BodyCategories.eGum.ordinal(),BodyCategories.eEdibleTiles.ordinal(),new GumListener());
        set(BodyCategories.eGum.ordinal(),BodyCategories.eNonEdibleTiles.ordinal(),new GumListener());
        set(BodyCategories.ePlayer.ordinal(),BodyCategories.eSpikes.ordinal(),new DeathListener());
        set(BodyCategories.eCheckPoint.ordinal(), BodyCategories.ePlayer.ordinal(), new CheckPointListener());
    }
    private void set(int _x, int _y, iListener _reaction)
    {
        reactions[_x][_y] = _reaction;
        reactions[_y][_x] = new FlipListener(_reaction);
        reactions[_y][_x] = _reaction;
    }
    private int getIndex(int bitMask)
    {
        int count = 0;
        while ((bitMask >> count) != 1)
        {
            count++;
        }
        return count;
    }
    public void beginContact(Contact _contact)
    {
        int categories[] = new int[2];
        categories[0] = getIndex(_contact.m_fixtureA.m_filter.categoryBits);
        categories[1] = getIndex(_contact.m_fixtureB.m_filter.categoryBits);
        reactions[categories[0]][categories[1]].beginContact(_contact);
    }

    public void endContact(Contact _contact)
    {
        int categories[] = new int[2];
        categories[0] = getIndex(_contact.m_fixtureA.m_filter.categoryBits);
        categories[1] = getIndex(_contact.m_fixtureB.m_filter.categoryBits);
        reactions[categories[0]][categories[1]].endContact(_contact);
    }

    public void preSolve(Contact _contact, Manifold _manifold)
    {
    }

    public void postSolve(Contact _contact, ContactImpulse _impulse)
    {
    }
    
}
