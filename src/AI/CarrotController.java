/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import AI.iPathFinding.Command;
import Entities.AIEntity;
import Entities.Carrot;
import Entities.Entity;
import Level.sLevel;
import World.sWorld;
import java.util.HashMap;
import javax.management.MBeanAttributeInfo;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.ContactEdge;

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
     private int mPunchCountdown;
     
     enum CarrotState
     {
         eIdle,
         ePathing,
         eWaiting,
         eDrillAttack,
         eStuck,
         eDying,
         eTongued,
         eBladeAttack
     }
    public CarrotController(AIEntity _entity)
    {
        super(_entity);
        mPathFinding = new AStar(mEntity, new ShortestDistance());
        mState = CarrotState.ePathing;
        mTimer = 0;
        mPunchCountdown = 0;
    }
    protected void updateTarget()
    {
        mTarget = sPathFinding.getPlayer();
        mTargetX = (int)(mTarget.getBody().getPosition().x);
        mTargetY = (int)(mTarget.getBody().getPosition().y);
    }
    public void stun()
    {
        mState = CarrotState.eDying;
        mTimer = 0;
        mEntity.getBody().setFixedRotation(false);
        mEntity.mSkin.stop("car_fly");
    }
    public void update()
    {
        int tx = mTargetX;
        int ty = mTargetY;
        updateTarget();
        int x = (int)(mEntity.getBody().getPosition().x);
        int y = (int)(mEntity.getBody().getPosition().y);
        
        if((mTargetY != ty || mTargetX != tx) && mState == CarrotState.ePathing)
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

            if(mTimer > 0.25f)
            {
                mTimer = 0;
                mState = CarrotState.eDrillAttack;
                mEntity.mSkin.deactivateSubSkin("car_fly");
            }
            
            ((Carrot)mEntity).fly(target, 0.25f);
            if (mEntity.hasTongueContacts())
            {
                mState = CarrotState.eTongued;
                mTimer = 0;
                mEntity.mSkin.setSpeed(2);
            }
        }
        else if(mState == CarrotState.eTongued)
        {
            if (!mEntity.hasTongueContacts())
            {
                mTimer = 0;
                mEntity.mSkin.setSpeed(1);
                mState = CarrotState.ePathing;
            }
            else
            {
                //((Carrot)mEntity).fly(mEntity.getBody().getPosition().add(new Vec2(0, -10)), 0.25f);
                mEntity.getBody().applyLinearImpulse(new Vec2(0, -1), mEntity.getBody().getWorldCenter());
                mTimer += (1.0f/60.0f);
                if (mTimer > 3.0f)
                {
                    mEntity.breakTongueContacts();
                    mState = CarrotState.ePathing;
                }
            }
        }
        else if(mState == CarrotState.eDrillAttack)
        {
            ContactEdge edge = mEntity.getBody().getContactList();
            while (edge != null)
            {
                if (edge.other.getFixtureList().getFilterData().categoryBits == (1 << sWorld.BodyCategories.ePlayer.ordinal()))
                {
                    ((Entity)edge.other.getUserData()).kill(Entity.CauseOfDeath.eEnemy, mEntity);
                }
                edge = edge.next;
            }
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
            mEntity.mSkin.deactivateSubSkin("car_fly");
            mEntity.getBody().m_fixtureList.setSensor(false);
            if (mEntity.getBody().getType().equals(BodyType.DYNAMIC))
            {
                HashMap params = new HashMap();
                params.put("position", mEntity.getBody().getPosition());
                params.put("aIEntity", mEntity);
                params.put("category", sWorld.BodyCategories.eEnemy);
                params.put("isDead", true);

                mEntity.getBody().getWorld().destroyBody(mEntity.getBody());
                mEntity.setBody(sWorld.useFactory("BoxCharFactory", params));
            }
            if (mEntity.hasTongueContacts())
            {
                Vec2 position = mEntity.getBody().getPosition();
                sWorld.destroyBody(mEntity.getBody());
                HashMap params = new HashMap();
                params.put("position", position);
                params.put("aIEntity", mEntity);
                params.put("category", sWorld.BodyCategories.eEnemy);
                mEntity.setBody(sWorld.useFactory("BoxCharFactory",params));
                mState = CarrotState.ePathing;
                //mEntity.setAnimation("car_fly");
                //mEntity.mSkin.setSpeed(1);
                mEntity.mSkin.activateSubSkin("car_fly", true, 1);
                mEntity.mSkin.deactivateSubSkin("car_stu");
                mEntity.breakTongueContacts();
                //mEntity.replaceAnchors();
            }
        }
        else if(mState == CarrotState.eDying)
        {
            mTimer++;
            if (mTimer == 180)
            {
                sWorld.destroyBody(mEntity.getBody());
            }
        }
    }
}
