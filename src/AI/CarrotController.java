/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import AI.iPathFinding.Command;
import Entities.AIEntity;
import Entities.Carrot;
import Entities.Entity;
import Entities.FlyingAI;
import Level.sLevel;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class CarrotController extends iAIController
{
     private iPathFinding mPathFinding;
     protected Entity mTarget;
     protected int mTargetX, mTargetY;
     private float mTimer;
     private Command mCommand;
     private CarrotState mState;
     
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
        mState = CarrotState.ePathing;
        mTimer = 0;
    }
    protected void updateTarget()
    {
        mTarget = sPathFinding.getPlayer();
        mTargetX = (int)(mTarget.getBody().getPosition().x);
        mTargetY = (int)(mTarget.getBody().getPosition().y);
    }
    public void update()
    {
        int tx = mTargetX;
        int ty = mTargetY;
        updateTarget();
        int x = (int)(mEntity.getBody().getPosition().x);
        int y = (int)(mEntity.getBody().getPosition().y);
        
        if(mTargetY != ty || mTargetX != tx)
        {
            /*mTargetX = (int)(mTarget.mBody.getPosition().x);
            mTargetY = (int)(mTarget.mBody.getPosition().y);*/
            mPathFinding.updatePath(x, y, mTargetX, mTargetY-4);
        }
        
        //((Carrot)mEntity).Hover();
        
        Vec2 target = mPathFinding.follow();
        Vec2 pos = mEntity.getBody().getPosition();
        
        /*if((target.x > pos.x) && (target.y+0.2 > pos.y && target.y-0.2 < pos.y)){mCommand = Command.eMoveRight;}
        else if((target.x < pos.x) && (target.y+0.2 > pos.y && target.y-0.2 < pos.y)){mCommand = Command.eMoveLeft;}
        else if((target.y > pos.y) && (target.x+0.2 > pos.x && target.x-0.2 < pos.x)){mCommand = Command.eMoveDown;}
        else if((target.y < pos.y) && (target.x+0.2 > pos.x && target.x-0.2 < pos.x)){mCommand = Command.eMoveUp;}
        else if((target.x > pos.x) && (target.y > pos.y)){mCommand = Command.eMoveBottomRight;}
        else if((target.x < pos.x) && (target.y < pos.y)){mCommand = Command.eMoveTopLeft;}
        else if((target.x > pos.x) && (target.y < pos.y)){mCommand = Command.eMoveTopRight;}
        else if((target.x < pos.x) && (target.y > pos.y)){mCommand = Command.eMoveBottomLeft;}
        else{mCommand = Command.eStandStill;}*/
        
        if(mState == CarrotState.ePathing)
        {
            if(pos.x < tx+1 && pos.x > tx-1 && ty > pos.y)
            {
                mTimer += (1.0f/60.0f);
            }
            else 
            {
                mTimer = 0;
            }

            if(mTimer > 1.75f)
            {
                mTimer = 0;
                mState = CarrotState.eDrillAttack;
            }
            
            ((Carrot)mEntity).fly(target, 0.25f);
        }
        else if(mState == CarrotState.eDrillAttack)
        {
            mEntity.setAnimation("car_att");
            //mEntity.mSkin.stopAnim();
            mEntity.getBody().applyLinearImpulse(new Vec2(0,1), mEntity.getBody().getWorldCenter());
            mEntity.getBody().m_fixtureList.setSensor(true);
            //((Carrot)mEntity).fly(target, 0.5f);
            if(sLevel.getPathInfo((int)pos.x, (int)(pos.y+1.1f)) == sLevel.PathInfo.eNotPassable)
            {
                //mTimer = 0;
                mState = CarrotState.eStuck;
            }
        }
        else if(mState == CarrotState.eStuck)
        {
            mEntity.setAnimation("car_stu");
            mEntity.getBody().m_fixtureList.setSensor(false);
        }
        
        

        
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
