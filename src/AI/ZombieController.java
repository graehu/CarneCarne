/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.AIEntity;

/**
 *
 * @author alasdair
 */
public class ZombieController extends iAIController
{
    iPathFinding pathFinding;
    public ZombieController(AIEntity _entity)
    {
        super(_entity);
        //pathFinding = new AStarPathFinder();
    }
    public void update()
    {
        /*int x = (int)mEntity.mBody.getPosition().x;
        int y = (int)mEntity.mBody.getPosition().y;
        pathFinding.updatePosition(x, y, x+2, y);
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
                break;
            }
        }*/
    }
}
