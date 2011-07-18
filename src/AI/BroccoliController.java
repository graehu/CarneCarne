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
     private iPathFinding mPathFinding;
     private Entity mTarget;
     private int mTargetX, mTargetY;
     private Command mCommand;
     private float mRange;
    public BroccoliController(AIEntity _entity)
    {
        super(_entity);
        mPathFinding = new AStar(mEntity, new ShortestDistance());
        mRange = 15;
    }
    
    public void update()
    {
        mTarget = sPathFinding.getPlayer();
        
        Vec2 myPos = mEntity.mBody.getPosition();
        Vec2 targetPos = mTarget.mBody.getPosition();
        
        
        if((targetPos.x > myPos.x+mRange) && targetPos.x < myPos.x-mRange)
        {
            if((targetPos.y > myPos.y+mRange) && targetPos.y < myPos.y-mRange)
            {
                mEntity.jump();
            }
        }
                
        
        /*if(mTargetY != (int)(mTarget.mBody.getPosition().y) || mTargetX != (int)(mTarget.mBody.getPosition().x))
        {
            mTargetX = (int)(mTarget.mBody.getPosition().x);
            mTargetY = (int)(mTarget.mBody.getPosition().y);
            mPathFinding.updatePath(x, y, mTargetX, mTargetY-4);
        }*/
        
        /*if(pos.x < x)
        {
            mCommand = Command.eMoveLeft;
        }
        else
        {
            mCommand = Command.eMoveRight;
        }*/
        
        
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
