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
     private Entity mTarget;
     private float mFollowRadius;
     private float mAttackRadius;
    public BroccoliController(AIEntity _entity)
    {
        super(_entity);
        mFollowRadius = 7.5f;
        mAttackRadius = 2.5f;
    }
    
    public void update()
    {
        mTarget = sPathFinding.getPlayer();
        
        if(mTarget != null)
        {
            Vec2 myPos = mEntity.getBody().getPosition();
            Vec2 targetPos = mTarget.getBody().getPosition();
            
            if ((targetPos.x < myPos.x+mAttackRadius) && targetPos.x > myPos.x-mAttackRadius)
            {
                if((targetPos.y < myPos.y+mAttackRadius) && targetPos.y > myPos.y-mAttackRadius)
                {
                    ((Broccoli)mEntity).attack();
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
        }
    }
}
