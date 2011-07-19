/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.iAIController;
import AI.iPathFinding.Command;
import Entities.AIEntityState.State;
import Graphics.Particles.ParticleSysBase;
import Graphics.Particles.sParticleManager;
import Graphics.Skins.iSkin;
import Level.RootTile.AnimationType;
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
    protected Tile mTouchingTile;
    protected String mCurrentAnimation;
    protected float mAnimSpeed;
    protected Vec2 mFloorNormal;
    protected static int mJumpReload = 60; /// NOTE frame rate change
    protected float mMoveSpeed;
    protected Command mCommand;
    protected AIEntityState mAIEntityState;
    protected TileType mTileType;
    protected int mContactParticleTimer = 0;

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
        mTouchingTile = null;
    }
    @Override
    public void kill()
    {
        if (getBody() != null)
        {
            sWorld.destroyBody(getBody());
            setBody(null);
            mController.destroy();
            mController = null;
            mAIEntityState.destroy();
            mAIEntityState = null;
        }
    }
    public void update()
    {
        mContactParticleTimer++;
        
        //dampen
        getBody().setAngularDamping(8);
        
        //count contacts
        int numContacts = 0;
        ContactEdge edgeCounter = getBody().m_contactList;
        while(edgeCounter != null)
        {
            numContacts++;
            edgeCounter = edgeCounter.next;
        }
        
        ContactEdge edge = getBody().m_contactList;
        int mTar = 0;
        int mIce = 0;
        int mJumpContacts = 0;
        mAllowRoll = false;
        mTouchingTile = null;
        
        while (edge != null)
        {
            Fixture other = edge.contact.m_fixtureA;
            boolean AtoB = true;
            if (other.m_body == getBody())
            {
                AtoB = false;
                other = edge.contact.m_fixtureB;
            }
            //if (other.m_filter.categoryBits == (1 << sWorld.BodyCategories.eEdibleTiles.ordinal())||
            //        other.m_filter.categoryBits == (1 << sWorld.BodyCategories.eNonEdibleTiles.ordinal()))
            //if (other.getUserData() != null)
            {
                Vec2 collisionNorm = edge.contact.m_manifold.localNormal;
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
                        if(((Tile)other.getUserData()) != null)
                            mTouchingTile = ((Tile)other.getUserData());
                        mJumpContacts++;
                        //set tile type while only touching one type
                        if(numContacts == 1 && other.getUserData() != null)
                            mTileType = ((Tile)other.getUserData()).getTileType();
                    }
                    mFloorNormal = collisionNorm.clone();
                }
                else if(collisionNorm.y < - 0.3 || collisionNorm.y > 0.3)//slopes
                {
                    if(edge.contact.isTouching() && !other.isSensor())
                    {
                        createContactParticle(collisionNorm);
                        mJumpContacts++; //allow jump on slopes
                        mAllowRoll = true;
                    }
                    mFloorNormal = collisionNorm.clone();
                }
                else
                {
                    if(edge.contact.isTouching() && !other.isSensor())
                    {
                        createContactParticle(collisionNorm);
                    }
                }
            }
            edge = edge.next;
        }
        mAIEntityState.update(mTar, mIce, mWaterHeight, mJumpContacts);
        getBody().applyLinearImpulse(new Vec2(0,0.2f*getBody().getMass()), getBody().getWorldCenter());
        if (mJumpTimer != 0)
        {
            mJumpTimer--;
        }

        mController.update();
        subUpdate();
    }
    protected void createContactParticle(Vec2 _dir)
    {
        if(getBody().getLinearVelocity().lengthSquared() > 20)
        {
            if(mContactParticleTimer > 10) //assumes 60fps
            {
                mContactParticleTimer = 0;
                ParticleSysBase sys = null;
                if (mTouchingTile == null)
                    sys = sParticleManager.createSystem("cloud", sWorld.translateToWorld(getBody().getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32)).add(_dir.mul(-32)), 1f);
                else
                    sys = sParticleManager.createSystem(mTouchingTile.getAnimationsName(AnimationType.eJump) + "Jump", sWorld.translateToWorld(getBody().getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,32)).add(_dir.mul(-32)), 1f);
                float angle = (float) Math.acos(Vec2.dot(_dir, new Vec2(0,1)));
                if(_dir.x >= 0)
                    sys.setAngularOffset(angle);
                else
                    sys.setAngularOffset(-angle);
            }
        }
    }
    protected void subUpdate()
    {
        if (mAIEntityState.getState().equals(State.eSwimming))
        {
            buoyancy();
        }
    }
    protected void airControl(float _value)
    {
        getBody().applyLinearImpulse(new Vec2(0.8f*_value,0), getBody().getWorldCenter());
    }
    public void walk(float value)
    {
        mBody.m_fixtureList.m_friction = 5;
        switch (mAIEntityState.getState())
        {
            case eFalling:
            case eFallingDoubleJumped:
            {
                mBody.m_fixtureList.m_friction = 1;
            }
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
                    getBody().applyAngularImpulse(0.7f*value);
                }
                else
                {
                    getBody().applyLinearImpulse(new Vec2(1.1f*value,0),getBody().getWorldCenter());
                }
                break;
            }
            case eStandingOnTar:
            case eStillCoveredInTar:
            {
                getBody().applyLinearImpulse(new Vec2(0.5f*value,0),getBody().getWorldCenter());
                break;
            }
            case eIce:
            {
                getBody().applyAngularImpulse(0.5f*value);
                break;
            }
            case eSwimming:
            {
                getBody().applyLinearImpulse(new Vec2(0.5f*value,0),getBody().getWorldCenter());
                getBody().applyAngularImpulse(0.5f*value);
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
        walk(-1f);
    }
    public void walkRight()
    {
        walk(1f);
    }
    public void jump()
    {
        float canJump = mAIEntityState.canJump(getBody().getLinearVelocity().y);
        if (canJump != 0.0f)
        {
            getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x, canJump));
            mJumpTimer = mJumpReload;
            mAIEntityState.jump();
            if (!mAIEntityState.getState().equals(State.eSwimming))
                try
                {
                    if (mTouchingTile == null)
                        sParticleManager.createSystem("cloud", sWorld.translateToWorld(getBody().getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,64)), 1f);
                    else
                        sParticleManager.createSystem(mTouchingTile.getAnimationsName(AnimationType.eJump) + "Jump", sWorld.translateToWorld(getBody().getPosition()).sub(sWorld.getPixelTranslation()).add(new Vec2(32,64)), 1f);
                }
                catch (NullPointerException e)
                {
                    System.err.println("Null pointer rendering jump particle");
                }
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
        switch(mAIEntityState.getState())
        {
            case eFalling:
            case eFallingDoubleJumped:
            case eJumpTransition:
            case eJumping:
                return true;
        }
        return false;
        //return mAIEntityState.getState().equals(AIEntityState.State.eFalling);
    }
    
    public boolean isDead()
    {
        return mAIEntityState.getState() == AIEntityState.State.eDead || mAIEntityState.getState() == AIEntityState.State.eRestartingRace;
    }
    
    public float setAnimation(String _animation)
    {
        if(!_animation.equals(mCurrentAnimation))
        {
            mSkin.deactivateSubSkin(mCurrentAnimation);
            mCurrentAnimation = _animation;
            return mSkin.activateSubSkin(_animation, false, mAnimSpeed);
        }
        else if(!mSkin.isAnimating(_animation))
        {
            mSkin.deactivateSubSkin(mCurrentAnimation);
            mCurrentAnimation = _animation;
            return mSkin.activateSubSkin(_animation, false, mAnimSpeed);
        }
        return 0;
    }

    public void stun(Vec2 _direction)
    {
        mBody.applyLinearImpulse(_direction.mul(15.0f), getBody().getWorldCenter());
    }
    void stun()
    {
        mAIEntityState.stun();
    }

    public void crouch()
    {
    }
    @Override
    public void render()
    {
        Vec2 pixelPosition = sWorld.translateToWorld(getBody().getPosition());
        mSkin.render(pixelPosition.x,pixelPosition.y);
    }

}