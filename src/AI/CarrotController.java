/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import AI.iPathFinding.Command;
import Entities.AIEntity;
import Entities.Carrot;
import Entities.Entity;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class CarrotController extends iAIController
{
     iPathFinding mPathFinding;
     Entity mTarget;
     int mTargetX, mTargetY;
     Command mCommand;
     CarrotState mState;
     
     enum CarrotState
     {
         eIdle,
         ePathing,
         eWaiting,
         eDrillAttack,
         eStuck,
         eBladeAttack
     }
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
        
        if(mTargetY != (int)(mTarget.mBody.getPosition().y) || mTargetX != (int)(mTarget.mBody.getPosition().x))
        {
            mTargetX = (int)(mTarget.mBody.getPosition().x);
            mTargetY = (int)(mTarget.mBody.getPosition().y);
            mPathFinding.updatePath(x, y, mTargetX, mTargetY-4);
        }
        
        //((Carrot)mEntity).Hover();
        
        Vec2 target = mPathFinding.follow();
        Vec2 pos = mEntity.mBody.getPosition();
        
        /*if((target.x > pos.x) && (target.y+0.2 > pos.y && target.y-0.2 < pos.y)){mCommand = Command.eMoveRight;}
        else if((target.x < pos.x) && (target.y+0.2 > pos.y && target.y-0.2 < pos.y)){mCommand = Command.eMoveLeft;}
        else if((target.y > pos.y) && (target.x+0.2 > pos.x && target.x-0.2 < pos.x)){mCommand = Command.eMoveDown;}
        else if((target.y < pos.y) && (target.x+0.2 > pos.x && target.x-0.2 < pos.x)){mCommand = Command.eMoveUp;}
        else if((target.x > pos.x) && (target.y > pos.y)){mCommand = Command.eMoveBottomRight;}
        else if((target.x < pos.x) && (target.y < pos.y)){mCommand = Command.eMoveTopLeft;}
        else if((target.x > pos.x) && (target.y < pos.y)){mCommand = Command.eMoveTopRight;}
        else if((target.x < pos.x) && (target.y > pos.y)){mCommand = Command.eMoveBottomLeft;}
        else{mCommand = Command.eStandStill;}*/
        
        
        
        ((Carrot)mEntity).fly(target, 0.5f);
        
        /*switch (mPathFinding.follow())
        {
            case eMoveLeft:
                ((Carrot)mEntity).fly(Command.eMoveLeft);
                mState = CarrotState.ePathing;
                break;
            case eMoveRight:
                ((Carrot)mEntity).fly(Command.eMoveRight);
                mState = CarrotState.ePathing;
                break;
            case eMoveUp:
                ((Carrot)mEntity).fly(Command.eMoveUp);
                mState = CarrotState.ePathing;
                break;
            case eMoveDown:
                ((Carrot)mEntity).fly(Command.eMoveDown);
                mState = CarrotState.ePathing;
                break;
            case eMoveTopLeft:
                ((Carrot)mEntity).fly(Command.eMoveTopLeft);
                mState = CarrotState.ePathing;
                break;
            case eMoveBottomLeft:
                ((Carrot)mEntity).fly(Command.eMoveBottomLeft);
                mState = CarrotState.ePathing;
                break;
            case eMoveBottomRight:
                ((Carrot)mEntity).fly(Command.eMoveBottomRight);
                mState = CarrotState.ePathing;
                break;
            case eMoveTopRight:
                ((Carrot)mEntity).fly(Command.eMoveTopRight);
                mState = CarrotState.ePathing;
                break;
            case eStandStill:
            {
                ((Carrot)mEntity).Hover();
                if(mState == CarrotState.ePathing);
                break;
            }
        }  //*/
    }
    
    
    
}
