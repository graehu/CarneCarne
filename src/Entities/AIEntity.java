/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iAIController;
import AI.iPathFinding.Command;
import Graphics.Skins.iSkin;
import Level.Tile;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.RevoluteJoint;
/**
 *
 * @author alasdair
 */
public class AIEntity extends Entity {

    protected iAIController mController;
    protected boolean mCanJump;
    protected int mTurnThisFrame;
    protected int mJumpTimer;
    protected static int mJumpReload = 60; /// NOTE frame rate change
    protected float mMoveSpeed;
    protected Command mCommand;

    public RevoluteJoint mJoint;
    public AIEntity(iSkin _skin)
    {
        super(_skin);
        mCanJump = false;
        mJumpTimer = 0;
        mTurnThisFrame = mJumpReload;
        mMoveSpeed = 1;
    }
    public void update()
    {
        //apply gravity
        //mBody.applyLinearImpulse(new Vec2(0,2.0f*mBody.getMass()), new Vec2(0,0));
        if (mWaterTiles != 0)
        {
            float height = 1.0f-(mWaterHeight - mBody.getPosition().y);
            if (height > 1.0f)
            {
                height = 1.0f;
            }
            //while traveling downwards
            Vec2 velocity = mBody.getLinearVelocity();
            //velocity.normalize();
            mBody.applyLinearImpulse(new Vec2(0,-3.0f*height), new Vec2(0,0));
        }
        if (mJumpTimer != 0)
        {
            mJumpTimer--;
        }
        
        ContactEdge edge = mBody.m_contactList;
        mCanJump = false;
        while (edge != null)
        {
            Fixture other = edge.contact.m_fixtureA;
            if (other.m_body != edge.other)
            {
                other = edge.contact.m_fixtureB;
            }
            //if (other.m_filter.categoryBits == (1 << sWorld.BodyCategories.eEdibleTiles.ordinal())||
            //        other.m_filter.categoryBits == (1 << sWorld.BodyCategories.eNonEdibleTiles.ordinal()))
            if (other.getUserData() != null)
            {
                if (((Tile)other.getUserData()).getPosition().y > mBody.getPosition().y)
                {
                    mCanJump = true;
                    break;
                }
            }
            edge = edge.next;
        }
        if (mTurnThisFrame == 0)
            mJoint.m_motorSpeed = 0.0f;
        else mTurnThisFrame = 0;
        mController.update();
        subUpdate();
    }
    protected void subUpdate()
    {
        
    }
    public void walk(float value)
    {
        mBody.applyLinearImpulse(new Vec2(value*0.1f,0), new Vec2(0,0));
        if (mTar == 0)
        {
            mJoint.m_motorSpeed = value*-10.0f;
        }
        else
        {
            mJoint.m_motorSpeed = value*-1.0f;
        }
        mTurnThisFrame = 1000;
        
    }
    public void walkLeft()
    {
        mBody.applyLinearImpulse(new Vec2(-0.1f,0), new Vec2(0,0));
        if (mTar == 0)
        {
            mJoint.m_motorSpeed = mMoveSpeed*10.0f;
        }
        else
        {
            mJoint.m_motorSpeed = mMoveSpeed*1.0f;
        }
        mTurnThisFrame = 1000;
    }
    public void walkRight()
    {
        mBody.applyLinearImpulse(new Vec2(0.1f,0), new Vec2(0,0));
        if (mTar == 0)
        {
            mJoint.m_motorSpeed = mMoveSpeed*-10.0f;
        }
        else
        {
            mJoint.m_motorSpeed = mMoveSpeed*-1.0f;
        }
        mTurnThisFrame = 1000;
    }
    public void jump()
    {
        if (mCanJump && mJumpTimer == 0)
        {
            mBody.applyLinearImpulse(new Vec2(0,-30.0f), new Vec2(0,0));
            mCanJump = false;
            mJumpTimer = mJumpReload;
        }
    }
    
    public void fly(Command _command)
    {
        //do sommit
        //Hover();
        switch(_command)
        {
            case eMoveLeft:
                mBody.setLinearVelocity(new Vec2(-mMoveSpeed,0));
                break;
            case eMoveRight:
                mBody.setLinearVelocity(new Vec2(mMoveSpeed,0));
                break;
            case eMoveUp:
                mBody.setLinearVelocity(new Vec2(0,-mMoveSpeed));
                break;
            case eMoveDown:
                mBody.setLinearVelocity(new Vec2(0,mMoveSpeed));
                break;
            case eMoveTopLeft:
                mBody.setLinearVelocity(new Vec2(-mMoveSpeed,-mMoveSpeed));
                break;
            case eMoveBottomLeft:
                mBody.setLinearVelocity(new Vec2(mMoveSpeed,-mMoveSpeed));
                break;
            case eMoveBottomRight:
                mBody.setLinearVelocity(new Vec2(mMoveSpeed, mMoveSpeed));
                break;
            case eMoveTopRight:
                mBody.setLinearVelocity(new Vec2(-mMoveSpeed, mMoveSpeed));
                break;
        }
    }
    public void Hover()
    {
        mBody.setLinearVelocity(new Vec2(0,0));
    }
    
    public void setMoveSpeed(float _moveSpeed)
    {
        mMoveSpeed = _moveSpeed;
    }
    
    public void crouch()
    {
    }
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }
}