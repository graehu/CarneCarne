/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import AI.iPathFinding.Command;
import Entities.AIEntity;
import Entities.Broccoli;
import Entities.Entity;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class BroccoliController extends iAIController
{
     iPathFinding mPathFinding;
     Entity mTarget;
     int mTargetX, mTargetY;
     Command mCommand;
    public BroccoliController(AIEntity _entity)
    {
        super(_entity);
        mPathFinding = new AStar(mEntity, new ShortestDistance());
    }
    
    public void update()
    {
        mTarget = sPathFinding.getPlayer();
        int x = (int)(mEntity.mBody.getPosition().x);
        int y = (int)(mEntity.mBody.getPosition().y);
        Vec2 pos = mTarget.mBody.getPosition();
        
        /*if(mTargetY != (int)(mTarget.mBody.getPosition().y) || mTargetX != (int)(mTarget.mBody.getPosition().x))
        {
            mTargetX = (int)(mTarget.mBody.getPosition().x);
            mTargetY = (int)(mTarget.mBody.getPosition().y);
            mPathFinding.updatePath(x, y, mTargetX, mTargetY-4);
        }*/
        
        if(pos.x < x)
        {
            mCommand = Command.eMoveLeft;
        }
        else
        {
            mCommand = Command.eMoveRight;
        }
        
        
       /* switch (mCommand)
        {
            case eMoveLeft:
                //mEntity.fly(Command.eMoveLeft);
                ((Broccoli)mEntity).moveLeft();
                break;
            case eMoveRight:
                //mEntity.fly(Command.eMoveRight);
                ((Broccoli)mEntity).moveRight();
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
        }        */
    }
    
}
