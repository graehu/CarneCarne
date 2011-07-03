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
    protected boolean mAllowRoll = false;
    protected int mJumpTimer;
    protected int mJumpContacts;
    protected static int mJumpReload = 60; /// NOTE frame rate change
    protected float mMoveSpeed;
    protected Command mCommand;
    protected AIEntityState mAIEntityState;

    public RevoluteJoint mJoint;
    public AIEntity(iSkin _skin)
    {
        super(_skin);
        mAIEntityState = new AIEntityState(this);
        mJumpTimer = 0;
        mMoveSpeed = 1;
    }
    public void update()
    {
        //apply gravity
        Vec2 mv = mBody.getLinearVelocity();
        float mass = mBody.getMass();
        ContactEdge edge = mBody.m_contactList;
        int mTar = 0;
        int mIce = 0;
        mJumpContacts = 0;
        mAllowRoll = false;
        while (edge != null)
        {
            Fixture other = edge.contact.m_fixtureA;
            boolean AtoB = true;
            if (other.m_body == mBody)
            {
                AtoB = false;
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
                            if (((Tile)other.getUserData()).isOnFire())
                            {
                                kill();
                            }
                            else
                            {
                                mTar++;
                            }
                            break;
                        default:
                            break;
                    }
                }
                Vec2 collisionNorm = edge.contact.m_manifold.localNormal;
                float rot = other.getBody().getTransform().getAngle();
                collisionNorm.x = (float) (collisionNorm.x*Math.cos(rot) - collisionNorm.y*Math.sin(rot));
                collisionNorm.y = (float) (collisionNorm.x*Math.sin(rot) + collisionNorm.y*Math.cos(rot));
                if(AtoB == false)
                {
                    collisionNorm.negateLocal();
                }
                collisionNorm.normalize();
                if(collisionNorm.y > 1/root2) //up
                {
                }
                else if(collisionNorm.y < - 0.9) //down
                {
                    if(edge.contact.isTouching() && !other.isSensor())
                    {
                        mJumpContacts++;
                    }
                }
                else if(collisionNorm.y < - 0.3 || collisionNorm.y > 0.3)//slopes // horizontal
                {
                    if(edge.contact.isTouching() && !other.isSensor())
                    {
                        mJumpContacts++; //allow jump on slopes
                        mAllowRoll = true;
                    }
                    
                }
            }
            edge = edge.next;
        }
        mAIEntityState.update(mTar, mIce, mJumpContacts);
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
        switch (mAIEntityState.getState())
        {
            case eFalling:
            {
                mBody.applyLinearImpulse(new Vec2(0.1f*value,0), mBody.getWorldCenter());
                break;
            }
            case eStanding:
            {
                if(mAllowRoll)
                {
                    mBody.m_fixtureList.m_friction = 20;
                    mBody.applyAngularImpulse(0.7f*value);
                }
                else
                {
                    mBody.m_fixtureList.m_friction = 5;
                    mBody.applyLinearImpulse(new Vec2(1.1f*value,0),mBody.getWorldCenter());
                }
                break;
            }
            case eStandingOnTar:
            case eStillCoveredInTar:
            {
                mBody.applyLinearImpulse(new Vec2(0.3f*value,0),mBody.getWorldCenter());
                break;
            }
            case eIce:
            {
                mBody.applyAngularImpulse(0.5f*value);
                break;
            }
            case eDead:
            {
                break;
            }
        }
    }
    public void walkLeft()
    {
        switch (mAIEntityState.getState())
        {
            case eFalling:
            {
                mBody.applyLinearImpulse(new Vec2(-0.1f,0), mBody.getWorldCenter());
                break;
            }
            case eStanding:
            {
                if(mAllowRoll)
                {
                    mBody.m_fixtureList.m_friction = 20;
                    mBody.applyAngularImpulse(-0.7f);
                }
                else
                {
                    mBody.m_fixtureList.m_friction = 5;
                    mBody.applyLinearImpulse(new Vec2(-1.1f,0),mBody.getWorldCenter());
                }
                break;
            }
            case eStandingOnTar:
            case eStillCoveredInTar:
            {
                mBody.applyLinearImpulse(new Vec2(-0.3f,0),mBody.getWorldCenter());
                break;
            }
            case eIce:
            {
                mBody.applyAngularImpulse(-0.5f);
                break;
            }
            case eDead:
            {
                break;
            }
        }
    }
    public void walkRight()
    {
        switch (mAIEntityState.getState())
        {
            case eFalling:
            {
                mBody.applyLinearImpulse(new Vec2(0.1f,0), mBody.getWorldCenter());
                break;
            }
            case eStanding:
            {
                if(mAllowRoll)
                {
                    mBody.m_fixtureList.m_friction = 20;
                    mBody.applyAngularImpulse(0.7f);
                }
                else
                {
                    mBody.m_fixtureList.m_friction = 5;
                    mBody.applyLinearImpulse(new Vec2(1.1f,0),mBody.getWorldCenter());
                }
                break;
            }
            case eStandingOnTar:
            case eStillCoveredInTar:
            {
                mBody.applyLinearImpulse(new Vec2(0.3f,0),mBody.getWorldCenter());
                break;
            }
            case eIce:
            {
                mBody.applyAngularImpulse(0.5f);
                break;
            }
            case eDead:
            {
                break;
            }
        }
    }
    public void jump()
    {
        if (!mAIEntityState.getState().equals(AIEntityState.State.eFalling) && 
            !mAIEntityState.getState().equals(AIEntityState.State.eJumping) &&
            mJumpContacts != 0)
        {
            
            mBody.applyLinearImpulse(new Vec2(0,-23.0f), mBody.getWorldCenter());
            mJumpTimer = mJumpReload;
            mAIEntityState.jump();
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
                mBody.setLinearVelocity(new Vec2(-mMoveSpeed, mMoveSpeed));
                break;
            case eMoveBottomRight:
                mBody.setLinearVelocity(new Vec2(mMoveSpeed, mMoveSpeed));
                break;
            case eMoveTopRight:
                mBody.setLinearVelocity(new Vec2(mMoveSpeed, -mMoveSpeed));
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
        return mAIEntityState.getState().equals(AIEntityState.State.eFalling);
        //return mJumpContacts > 0;
    }
    
    public void crouch()
    {
    }
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }

    /*public void canJump()
    {
        mJumpContacts++;
    }
    public void cantJump()
    {
        mJumpContacts--;
    }*/
}