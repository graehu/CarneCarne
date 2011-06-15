/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iAIController;
import Graphics.iSkin;
import Physics.sPhysics;
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
    static int jumpReload = 1000; /// NOTE frame rate change
    public RevoluteJoint mJoint;
    public AIEntity(iSkin _skin)
    {
        super(_skin);
        canJump = false;
        jumpTimer = 0;
        turnThisFrame = 1000;
    }
    public void update()
    {
        if (jumpTimer != 0)
        {
            jumpTimer--;
        }
        ContactEdge edge = mBody.m_contactList;
        canJump = false;
        while (edge != null)
        {
            Body body = edge.other;
            if (body.m_shapeList.m_filter.categoryBits == sPhysics.BodyCategories.eTiles.ordinal())
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
        mBody.applyImpulse(new Vec2(-0.01f,0), new Vec2(0,0));
        mJoint.m_motorSpeed = 10.0f;
        turnThisFrame = 1000;
    }
    public void walkRight()
    {
        mBody.applyImpulse(new Vec2(0.01f,0), new Vec2(0,0));
        mJoint.m_motorSpeed = -10.0f;
        turnThisFrame = 1000;
    }
    public void jump()
    {
        if (canJump && jumpTimer == 0)
        {
            mBody.applyImpulse(new Vec2(0,-30.0f), new Vec2(0,0));
            canJump = false;
            jumpTimer = jumpReload;
        }
    }
    public void crouch()
    {
    }
    public void render()
    {
        //int xPixel = (int)(mBody.getPosition().x*64.0f);
        //int yPixel = (int)(mBody.getPosition().y*64.0f);
        Vec2 pixelPosition = sPhysics.translate(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }
}