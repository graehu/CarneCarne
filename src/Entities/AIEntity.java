/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iAIController;
import Graphics.Skins.iSkin;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.RevoluteJoint;
/**
 *
 * @author alasdair
 */
public class AIEntity extends Entity {
    iAIController mController;
    boolean mCanJump;
    int mTurnThisFrame;
    int mJumpTimer;
    static int mJumpReload = 60; /// NOTE frame rate change
    public RevoluteJoint mJoint;
    public AIEntity(iSkin _skin)
    {
        super(_skin);
        mCanJump = false;
        mJumpTimer = 0;
        mTurnThisFrame = mJumpReload;
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
            Body body = edge.other;
            if (body.m_fixtureList.m_filter.categoryBits == (1 << sWorld.BodyCategories.eEdibleTiles.ordinal())||
                    body.m_fixtureList.m_filter.categoryBits == (1 << sWorld.BodyCategories.eNonEdibleTiles.ordinal()))
            {
                if (body.getPosition().y > mBody.getPosition().y)
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
            mJoint.m_motorSpeed = 10.0f;
        }
        else
        {
            mJoint.m_motorSpeed = 1.0f;
        }
        mTurnThisFrame = 1000;
    }
    public void walkRight()
    {
        mBody.applyLinearImpulse(new Vec2(0.1f,0), new Vec2(0,0));
        if (mTar == 0)
        {
            mJoint.m_motorSpeed = -10.0f;
        }
        else
        {
            mJoint.m_motorSpeed = -1.0f;
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
    public void crouch()
    {
    }
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
        mSkin.setRotation("body", mBody.getAngle()*(180/(float)Math.PI));
    }
}