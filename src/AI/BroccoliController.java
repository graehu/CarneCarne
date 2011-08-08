/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import AI.iPathFinding.Command;
import Entities.AIEntity;
import Entities.Broccoli;
import Entities.Entity;
import World.sWorld.BodyCategories;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class BroccoliController extends iAIController
{
     private Entity mTarget;
     private float mFollowRadius;
     private float mAttackRadius;
     private boolean mAttacking;
    public BroccoliController(AIEntity _entity)
    {
        super(_entity);
        mFollowRadius = 7.5f;
        mAttackRadius = 1.5f;
        mAttacking = false;
    }
    
    public void update()
    {
        mTarget = sPathFinding.getPlayer();
        
        if(mTarget != null)
        {
            Vec2 myPos = mEntity.getBody().getPosition();
            Vec2 targetPos = mTarget.getBody().getPosition();
            
            if(mAttacking == false)            
            {
                if ((targetPos.x < myPos.x+mAttackRadius) && targetPos.x > myPos.x-mAttackRadius)
                {
                    if((targetPos.y < myPos.y+mAttackRadius) && targetPos.y > myPos.y-mAttackRadius)
                    {
                        mAttacking = true;
                        //((Broccoli)mEntity).attack();
                    }
                }
                else if((targetPos.x < myPos.x+mFollowRadius) && targetPos.x > myPos.x-mFollowRadius)
                {
                    if((targetPos.y < myPos.y+mFollowRadius) && targetPos.y > myPos.y-mFollowRadius)
                    {
                        if(targetPos.x > myPos.x)
                        {
                            ((Broccoli)mEntity).move(1.5f);
                        }
                        else
                        {
                            ((Broccoli)mEntity).move(-1.5f);
                        }
                    }
                }
                else if(mEntity.getBody().getContactList().contact.getFixtureA().getFilterData().categoryBits == (1 << BodyCategories.ePlayer.ordinal()))
                {
                    mAttacking = true;            
                }
            }
            else
            {
                 ((Broccoli)mEntity).attack();
            }
        }
    }
}
