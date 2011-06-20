/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iAIController;
import Graphics.iSkin;
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
    boolean canJump;
    int turnThisFrame;
    int jumpTimer;
    static int jumpReload = 60; /// NOTE frame rate change
    public RevoluteJoint mJoint;
    public AIEntity(iSkin _skin)
    {
        super(_skin);
        canJump = false;
        jumpTimer = 0;
        turnThisFrame = jumpReload;
    }
    public void update()
    {
        //dmBody.applyLinearImpulse(new Vec2(0,1.0f*mBody.getMass()), new Vec2(0,0));
        if (mWaterTiles != 0)
        {
            float height = 1.0f-(mWaterHeight - mBody.getPosition().y);
            if (height > 1.0f)
            {
                height = 1.0f;
            }
            mBody.applyLinearImpulse(new Vec2(0,-1.0f*height), new Vec2(0,0));
        }
        if (jumpTimer != 0)
        {
            jumpTimer--;
        }
        
        ContactEdge edge = mBody.m_contactList;
        canJump = false;
        while (edge != null)
        {
            Body body = edge.other;
            if (body.m_fixtureList.m_filter.categoryBits == (1 << sWorld.BodyCategories.eEdibleTiles.ordinal())||
                    body.m_fixtureList.m_filter.categoryBits == (1 << sWorld.BodyCategories.eNonEdibleTiles.ordinal()))
            {
                if (body.getPosition().y > mBody.getPosition().y)
                {
                    canJump = true;
                    break;
                }
            }
            edge = edge.next;
        }
        if (turnThisFrame == 0)
            mJoint.m_motorSpeed = 0.0f;
        else turnThisFrame = 0;
        mController.update();
    }
    public void walkLeft()
    {
        mBody.applyLinearImpulse(new Vec2(-0.1f,0), new Vec2(0,0));
        mJoint.m_motorSpeed = 10.0f;
        turnThisFrame = 1000;
    }
    public void walkRight()
    {
        mBody.applyLinearImpulse(new Vec2(0.1f,0), new Vec2(0,0));
        mJoint.m_motorSpeed = -10.0f;
        turnThisFrame = 1000;
    }
    public void jump()
    {
        if (canJump && jumpTimer == 0)
        {
            mBody.applyLinearImpulse(new Vec2(0,-30.0f), new Vec2(0,0));
            canJump = false;
            jumpTimer = jumpReload;
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