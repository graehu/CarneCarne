/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iAIController;
import AI.iPathFinding.Command;
import Entities.AIEntityState.State;
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
    public iAIController mController;
    protected boolean mAllowRoll = false;
    protected int mJumpTimer;
    protected String mCurrentAnimation;
    protected float mAnimSpeed;
    protected static int mJumpReload = 60; /// NOTE frame rate change
    protected float mMoveSpeed;
    protected Command mCommand;
    protected AIEntityState mAIEntityState;
    protected TileType mTileType;

    public RevoluteJoint mJoint;
    
    /*public void changeState(State _newState)
    {
        mAIEntityState.changeState(_newState);
    }*/
    
    public AIEntity(iSkin _skin)
    {
        super(_skin);
        mAIEntityState = new AIEntityState(this);
        mJumpTimer = 0;
        mMoveSpeed = 1;
        mAnimSpeed = 1;
    }
    public void update()
    {
        //count contacts
        int numContacts = 0;
        ContactEdge edgeCounter = mBody.m_contactList;
        while(edgeCounter != null)
        {
            numContacts++;
            edgeCounter = edgeCounter.next;
        }
        
        ContactEdge edge = mBody.m_contactList;
        int mTar = 0;
        int mIce = 0;
        int mWater = 0;
        int mJumpContacts = 0;
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
                        case eWater:
                        {
                            mWater = ((Tile)other.getUserData()).getWaterHeight();
                            mJumpContacts++;
                            break;
                        }
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
                        //set tile type while only touching one type
                        if(numContacts == 1)
                            mTileType = ((Tile)other.getUserData()).getTileType();
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
        mAIEntityState.update(mTar, mIce, mWater, mJumpContacts);
        //mBody.applyLinearImpulse(new Vec2(0,0.4f*mBody.getMass()), mBody.getWorldCenter());
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
        if (mAIEntityState.getState().equals(State.eSwimming))
        {
            buoyancy();
        }
    }
    protected void buoyancy()
    {
        float waterHeight = mAIEntityState.getWaterHeight();
        waterHeight = mBody.getPosition().y + 1 - waterHeight;
        mBody.applyLinearImpulse(new Vec2(0, -waterHeight*0.8f), mBody.getWorldCenter());
        if (waterHeight > 1.0f)
        {
            waterHeight = 1.0f;
        }
        mBody.applyLinearImpulse(mBody.getLinearVelocity().mul(-0.3f*waterHeight), mBody.getWorldCenter());
    }
    protected void airControl(float _value)
    {
        mBody.applyLinearImpulse(new Vec2(0.1f*_value,0), mBody.getWorldCenter());
    }
    public void walk(float value)
    {
        mBody.m_fixtureList.m_friction = 5;
        switch (mAIEntityState.getState())
        {
            case eFalling:
            case eFallingDoubleJumped:
            case eJumping:
            {
                airControl(value);
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
                    mBody.applyLinearImpulse(new Vec2(1.1f*value,0),mBody.getWorldCenter());
                }
                break;
            }
            case eStandingOnTar:
            case eStillCoveredInTar:
            {
                mBody.applyLinearImpulse(new Vec2(0.5f*value,0),mBody.getWorldCenter());
                break;
            }
            case eIce:
            {
                mBody.applyAngularImpulse(0.5f*value);
                break;
            }
            case eSwimming:
            {
                mBody.applyLinearImpulse(new Vec2(0.5f*value,0),mBody.getWorldCenter());
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
        mBody.m_fixtureList.m_friction = 5;
        switch (mAIEntityState.getState())
        {
            case eFalling:
            case eFallingDoubleJumped:
            case eJumping:
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
                    mBody.applyLinearImpulse(new Vec2(-1.5f,0),mBody.getWorldCenter());
                }
                break;
            }
            case eStandingOnTar:
            case eStillCoveredInTar:
            {
                mBody.applyLinearImpulse(new Vec2(-0.5f,0),mBody.getWorldCenter());
                break;
            }
            case eIce:
            {
                mBody.applyAngularImpulse(-0.5f);
                break;
            }
            case eSwimming:
            {
                mBody.applyLinearImpulse(new Vec2(-0.5f,0),mBody.getWorldCenter());
                mBody.applyAngularImpulse(-0.3f);
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
        mBody.m_fixtureList.m_friction = 5;
        switch (mAIEntityState.getState())
        {
            case eFalling:
            case eFallingDoubleJumped:
            case eJumping:
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
                    mBody.applyLinearImpulse(new Vec2(1.5f,0),mBody.getWorldCenter());
                }
                break;
            }
            case eStandingOnTar:
            case eStillCoveredInTar:
            {
                mBody.applyLinearImpulse(new Vec2(0.5f,0),mBody.getWorldCenter());
                break;
            }
            case eIce:
            {
                mBody.applyAngularImpulse(0.5f);
                break;
            }
            case eSwimming:
            {
                mBody.applyLinearImpulse(new Vec2(0.5f,0),mBody.getWorldCenter());
                mBody.applyAngularImpulse(0.3f);
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
        float canJump = mAIEntityState.canJump(mBody.getLinearVelocity().y);
        if (canJump != 0.0f)
        {
            mBody.setLinearVelocity(new Vec2(mBody.getLinearVelocity().x, canJump));
            mJumpTimer = mJumpReload;
            mAIEntityState.jump();
        }
    }
    public void stopJumping()
    {
        mAIEntityState.stopJumping();
    }
    
    public void setMoveSpeed(float _moveSpeed)
    {
        mMoveSpeed = _moveSpeed;
    }
    
    public boolean isAirBorn()
    {
        return mAIEntityState.getState().equals(AIEntityState.State.eFalling);
    }
    
    public boolean isDead()
    {
        return mAIEntityState.getState() == AIEntityState.State.eDead;
    }
    
    public float setAnimation(String _animation)
    {
        if(!_animation.equals(mCurrentAnimation))
        {
            mSkin.stopAnim(mCurrentAnimation);
            mCurrentAnimation = _animation;
            return mSkin.startAnim(_animation, true, mAnimSpeed);
        }
        return 0;
    }
    
    public void crouch()
    {
    }
    @Override
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(mBody.getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }
}