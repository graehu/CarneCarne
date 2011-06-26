/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.AIEntity;
import Level.sLevel;

/**
 *
 * @author alasdair
 */
public class ZombieController extends iAIController
{
    iPathFinding pathFinding;
    boolean mToggle;
    public ZombieController(AIEntity _entity)
    {
        super(_entity);
        pathFinding = new AStar(_entity, new ShortestDistance());
        mToggle = true;
        mEntity.setMoveSpeed(0.9f);
    }
    public void update()
    {
        int x = (int)(mEntity.mBody.getPosition().x);
        int y = (int)(mEntity.mBody.getPosition().y);
        
        if(mToggle)
        {
            pathFinding.updatePath(x, y, x+1, y);
        }
        else if(!mToggle)
        {
            pathFinding.updatePath(x, y, x-1, y);
        }
        
        switch (pathFinding.follow())
        {
            case eWalkLeft:
            {
                mEntity.walkLeft();
                break;
            }
            case eWalkRight:
            {
                mEntity.walkRight();
                break;
            }
            case eStandStill:
            {
                if(mToggle)
                    mToggle = false;
                else if(!mToggle)
                    mToggle = true;
                break;
            }
        }
    }
}
