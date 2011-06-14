/*
 * Factory class that encapsulates the recycling of entities
 * deals in interfaces, internally handles entities.
 */

package slicktestbed.Graphics;

import java.util.List;
import java.util.Set;

/**
 *
 * @author aaron
 */

abstract class GraphicsFactory {

    //Member variables
    List<GEntityInstance> m_gEntityList;
    Set<Integer> m_freeGEntityList; //references positions in m_gEntityList

    /* Creates entityInstance and adds to list
     * Returns an interface to new Entity
     */
    public abstract GEntityInterface createEntity();

    /* Disables given GEntityInterface and sets given entity to be recycled
     * returns false if unsuccessful
     */
    public boolean destroyEntity(GEntityInterface gEInterface)
    {
        if(gEInterface.m_entityInstance >= 0)
        {
            //add entity to list
            if(false == m_freeGEntityList.add(gEInterface.m_entityInstance))
                return false;
            //kill entity
            gEInterface.m_entityInstance = -1;
            return true;
        }
        return false;
    }




}
