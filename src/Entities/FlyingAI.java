/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iPathFinding.Command;
import Graphics.Skins.iSkin;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Graham
 */
public class FlyingAI extends AIEntity
{
    boolean mToggle;
    public FlyingAI(iSkin _skin)
    {
        super(_skin);
        mToggle = true;
    }
    
    public void fly(Vec2 _point, float _maxSpeed)
    {
        mBody.setLinearDamping(0.8f);        
        Vec2 pos = mBody.getPosition();
        Vec2 speed = new Vec2((_point.x-pos.x)/2, (_point.y-pos.y)/2);
        
        if(speed.x > _maxSpeed)
            speed.x = _maxSpeed;
        else if(speed.x < -_maxSpeed)
            speed.x = -_maxSpeed;
        
        if(speed.y > _maxSpeed)
            speed.y = _maxSpeed;
        else if(speed.y < -_maxSpeed)
            speed.y = -_maxSpeed;
        
        mBody.applyLinearImpulse(speed, mBody.getWorldCenter());
        
        
       /* if(!(mBody.getLinearVelocity().y < 0))
        {
            mBody.applyLinearImpulse(new Vec2(0,-0.98f/(mBody.getMass())), mBody.getWorldCenter());
        }*/
        /*switch(_command)
        {
            case eMoveLeft:
                //mBody.setLinearVelocity(new Vec2(-mMoveSpeed,0));
                //if(!(mBody.getLinearVelocity().x > -mMoveSpeed))
                if(mToggle)
                mBody.applyLinearImpulse(new Vec2(-mMoveSpeed,0), mBody.getWorldCenter());
                break;
            case eMoveRight:
                //mBody.setLinearVelocity(new Vec2(mMoveSpeed,0));
                //if(!(mBody.getLinearVelocity().x < mMoveSpeed))
                if(mToggle)
                mBody.applyLinearImpulse(new Vec2(mMoveSpeed,0), mBody.getWorldCenter());
                break;
            case eMoveUp:
                //mBody.setLinearVelocity(new Vec2(0,-mMoveSpeed));
                //if(!(mBody.getLinearVelocity().y > -mMoveSpeed))
                if(mToggle)
                mBody.applyLinearImpulse(new Vec2(0,-mMoveSpeed), mBody.getWorldCenter());
                break;
            case eMoveDown:
                //mBody.setLinearVelocity(new Vec2(0,mMoveSpeed));
                //if(!(mBody.getLinearVelocity().y < mMoveSpeed))
                if(mToggle)
                mBody.applyLinearImpulse(new Vec2(0,mMoveSpeed),mBody.getWorldCenter());
                break;
            case eMoveTopLeft:
                //mBody.setLinearVelocity(new Vec2(-mMoveSpeed,-mMoveSpeed));
                //if(!(mBody.getLinearVelocity().x > -mMoveSpeed) && !(mBody.getLinearVelocity().y > -mMoveSpeed))
                if(mToggle)
                mBody.applyLinearImpulse(new Vec2(-mMoveSpeed,-mMoveSpeed),mBody.getWorldCenter());
                break;
            case eMoveBottomLeft:
                //mBody.setLinearVelocity(new Vec2(-mMoveSpeed, mMoveSpeed));
                //if(!(mBody.getLinearVelocity().x > -mMoveSpeed) && !(mBody.getLinearVelocity().y < mMoveSpeed))
                if(mToggle)
                mBody.applyLinearImpulse(new Vec2(-mMoveSpeed, mMoveSpeed),mBody.getWorldCenter());
                break;
            case eMoveBottomRight:
                //mBody.setLinearVelocity(new Vec2(mMoveSpeed, mMoveSpeed));
                //if(!(mBody.getLinearVelocity().x < mMoveSpeed) && !(mBody.getLinearVelocity().y < mMoveSpeed))
                if(mToggle)
                mBody.applyLinearImpulse(new Vec2(mMoveSpeed, mMoveSpeed),mBody.getWorldCenter());
                break;
            case eMoveTopRight:
                //mBody.setLinearVelocity(new Vec2(mMoveSpeed, -mMoveSpeed));
                //if(!(mBody.getLinearVelocity().x < mMoveSpeed) && !(mBody.getLinearVelocity().y > -mMoveSpeed))
                if(mToggle)
                mBody.applyLinearImpulse(new Vec2(mMoveSpeed,-mMoveSpeed),mBody.getWorldCenter());
                break;
        }*/
    }
    
    public void Hover()
    {
        /*if(!(mBody.getLinearVelocity().y < (9.8f/60)))
        {
            mBody.applyLinearImpulse(new Vec2(0,-0.98f/(mBody.getMass())), mBody.getWorldCenter());
        }*/
       //mBody.applyLinearImpulse(new Vec2(0, -9.8f/mBody.getMass()),mBody.getWorldCenter());
        /*if(mBody.getLinearVelocity().y > 0.5)
        {
            mBody.applyLinearImpulse(new Vec2(0,-1.0f), mBody.getWorldCenter());
        }*/
        //mBody.setLinearVelocity(new Vec2(0,0));
    }
    
}
