/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.AIEntity;
import Level.sLevel;
import Level.sLevel.PathInfo;

/**
 *
 * @author alasdair
 */
public class PeaController extends iAIController
{
    iPathFinding mPathFinding;
    boolean mToggle;
    public PeaController(AIEntity _entity)
    {
        super(_entity);
        mPathFinding = new AStar(_entity, new ShortestDistance());
        mToggle = true;
        mEntity.setMoveSpeed(0.9f);
    }
    public void update()
    {
        int x = (int)(mEntity.mBody.getPosition().x);
        int y = (int)(mEntity.mBody.getPosition().y);
        
        if(mToggle)
        {
            //mPathFinding.updatePath(x, y, x+2, y);
            mEntity.walkLeft();
            if(sLevel.getPathInfo((int)(mEntity.mBody.getPosition().x), (int)(mEntity.mBody.getPosition().y)) == PathInfo.eNotPassable)
            {
                mToggle = false;
            }
        }
        else if(!mToggle)
        {
            mEntity.walkRight();
            if(sLevel.getPathInfo((int)(mEntity.mBody.getPosition().x+1), (int)(mEntity.mBody.getPosition().y)) == PathInfo.eNotPassable)
            {
                mToggle = false;
            }
        }
        

        
        /*switch (mPathFinding.follow())
        {
            case eMoveLeft:
            {
                mEntity.walkLeft();
                break;
            }
            case eMoveRight:
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
        }*/
    }
}
