/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import AI.iPathFinding.Command;
import Entities.AIEntity;
import Entities.Entity;

/**
 *
 * @author G203947
 */
public class CarrotController extends iAIController
{
     iPathFinding mPathFinding;
     Entity mTarget;
     int mTargetX, mTargetY;
    public CarrotController(AIEntity _entity)
    {
        super(_entity);
        mPathFinding = new AStar(mEntity, new ShortestDistance());
    }
    
    public void update()
    {
        mTarget = sPathFinding.getPlayer();
        int x = (int)(mEntity.mBody.getPosition().x);
        int y = (int)(mEntity.mBody.getPosition().y);
        if(mTargetY != (int)(mTarget.mBody.getPosition().y) || mTargetY != (int)(mTarget.mBody.getPosition().y))
        {
            mTargetX = (int)(mTarget.mBody.getPosition().x);
            mTargetY = (int)(mTarget.mBody.getPosition().y);
            mPathFinding.updatePath(x, y, mTargetX, mTargetY+4);
        }
        
        
        
        
        switch (mPathFinding.follow())
        {
            case eMoveLeft:
                mEntity.fly(Command.eMoveLeft);
                break;
            case eMoveRight:
               mEntity.fly(Command.eMoveRight);
                break;
            case eMoveUp:
                mEntity.fly(Command.eMoveUp);
                break;
            case eMoveDown:
                mEntity.fly(Command.eMoveDown);
                break;
            case eMoveTopLeft:
                mEntity.fly(Command.eMoveTopLeft);
                break;
            case eMoveBottomLeft:
                mEntity.fly(Command.eMoveBottomLeft);
                break;
            case eMoveBottomRight:
                mEntity.fly(Command.eMoveBottomRight);
                break;
            case eMoveTopRight:
                mEntity.fly(Command.eMoveTopRight);
                break;
            case eStandStill:
            {
                mEntity.Hover();
                break;
            }
        }        
    }
    
    
    
}
