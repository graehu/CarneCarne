/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;
import Entities.AIEntity;

/**
 *
 * @author Graham
 */
public class PatrolAi implements iAIController{
    
    public PatrolAi(AIEntity _entity)
    {
        mEntity = _entity;
    }
    
    public void update()
    {
        // do stuff like pathing
        // check if pathing,
        // if not pathing
        // find node      
            
    }
    
    private AIEntity mEntity;
    
}
