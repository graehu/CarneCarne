/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iAIController;
import AI.iPathFinding.Command;
import Graphics.Skins.iSkin;
import Level.Tile;
import Level.sLevel.TileType;
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

    final static float root2 = (float) Math.sqrt(2);
    protected iAIController mController;
    protected boolean mCanJump;
    //protected boolean mAirBorn;
    protected int mTurnThisFrame;
    protected int mJumpContacts;
    protected int mJumpTimer;
    protected static int mJumpReload = 60; /// NOTE frame rate change
    protected float mMoveSpeed;
    protected Command mCommand;
    protected int mTar = 0;
    protected int mIce = 0;

    public RevoluteJoint mJoint;
    public AIEntity(iSkin _skin)
    {
        super(_skin);
        mCanJump = false;
        mJumpTimer = 0;
        mTurnThisFrame = mJumpReload;
        mMoveSpeed = 1;
        mJumpContacts = 0;
    }
    public void update()
    {
        //apply gravity
        ContactEdge edge = mBody.m_contactList;
        mCanJump = false;
        mTar = mIce = mJumpContacts = 0;
        while (edge != null)
        {
            Fixture other = edge.contact.m_fixtureA;
            if (other.m_body == mBody)
            {
                other = edge.contact.m_fixtureB;
            }
            //if (other.m_filter.categoryBits == (1 << sWorld.BodyCategories.eEdibleTiles.ordinal())||
            //        other.m_filter.categoryBits == (1 << sWorld.BodyCategories.eNonEdibleTiles.ordinal()))
            //if (other.getUserData() != null)
            {
                if (other.getUserData() != null)
                {
                    TileType tileType = ((Tile)other.getUserData()).getTileType();
                    switch (tileType)
                    {
                        case eIce:
                            mIce++;
                            break;
                        case eTar:
                            mTar++;
                            break;
                        default:
                            break;
                    }
                }
                Vec2 collisionNorm = edge.contact.m_manifold.localNormal;
                collisionNorm.normalize();
                if(collisionNorm.y > 0.9) //up
                {
                }
                else if(collisionNorm.y < - 1/root2) //down
                {
                    mCanJump = true;
                    mJumpContacts++;
                }
                else //horizontal
                {
                }
            }
            edge = edge.next;
        }
        mBody.applyLinearImpulse(new Vec2(0,0.1f*mBody.getMass()), mBody.getWorldCenter());
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
        mBody.setAngularDamping(8);

        mController.update();
        subUpdate();
    }
    protected void subUpdate()
    {
        
    }
    public void walk(float value)
    {
        if(mIce > 0)
        {
            mBody.applyAngularImpulse(0.5f*value);
        }
        else if (mTar > 0)
        {
            mBody.applyLinearImpulse(new Vec2(0.3f*value,0),mBody.getWorldCenter());
        }
        else
        {
            if(mJumpContacts == 0)
                mBody.applyLinearImpulse(new Vec2(0.1f*value,0), mBody.getWorldCenter());
            else
                mBody.applyLinearImpulse(new Vec2(0.9f*value,0),mBody.getWorldCenter());
        }
        mTurnThisFrame = 1000;
    }
    public void walkLeft()
    {
        if(mIce > 0)
        {
            mBody.applyAngularImpulse(-0.5f);
        }
        else
        {
            if(mJumpContacts == 0)
                mBody.applyLinearImpulse(new Vec2(-0.1f,0), mBody.getWorldCenter());
            else
                mBody.applyLinearImpulse(new Vec2(-0.9f,0),mBody.getWorldCenter());
        }
        mTurnThisFrame = 1000;
    }
    public void walkRight()
    {
        if(mIce > 0)
        {
            mBody.applyAngularImpulse(0.5f);
        }
        else
        {
        if(mJumpContacts == 0)
        {
            mBody.applyLinearImpulse(new Vec2(0.15f,0), mBody.getWorldCenter());
        }
        else
            mBody.applyLinearImpulse(new Vec2(0.9f,0),mBody.getWorldCenter());
        }
        mTurnThisFrame = 1000;
    }
    public void jump()
    {
        if (mJumpContacts != 0 && mJumpTimer == 0)
        {
            mBody.applyLinearImpulse(new Vec2(0,-20.0f), mBody.getWorldCenter());
            //limit horizontal velocity
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
    
    public boolean isAirBorn()
    {
        return mJumpContacts > 0;
    }
    
    public void crouch()
    {
    }
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }

    public void canJump()
    {
        mJumpContacts++;
    }
    public void cantJump()
    {
        mJumpContacts--;
    }
}