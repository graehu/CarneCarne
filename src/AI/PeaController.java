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
 * @author Graham
 */
public class PeaController extends iAIController
{
    boolean mToggle;
    public PeaController(AIEntity _entity)
    {
        super(_entity);
        mToggle = true;
        mEntity.setMoveSpeed(0.9f);
    }
    public void update()
    {
        int x = (int)(mEntity.getBody().getPosition().x);
        int y = (int)(mEntity.getBody().getPosition().y);
        
        if(mToggle)
        {
            //mPathFinding.updatePath(x, y, x+2, y);
            mEntity.walkLeft();
            if(sLevel.getPathInfo((int)(mEntity.getBody().getPosition().x), (int)(mEntity.getBody().getPosition().y)) == PathInfo.eNotPassable)
            {
                mToggle = false;
            }
        }
        else if(!mToggle)
        {
            mEntity.walkRight();
            if(sLevel.getPathInfo((int)(mEntity.getBody().getPosition().x+1), (int)(mEntity.getBody().getPosition().y)) == PathInfo.eNotPassable)
            {
                mToggle = true;
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
