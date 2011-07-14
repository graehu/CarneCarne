/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.AIEntity;
import Entities.PlayerEntity;
import Graphics.Particles.ParticleSysBase;
import Graphics.Particles.sParticleManager;
import Graphics.Sprites.iSprite;
import Graphics.Sprites.sSpriteFactory;
import Level.RootTile.AnimationType;
import Level.Tile;
import Level.sLevel.TileType;
import World.TongueAnchor;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJoint;

/**
 *
 * @author alasdair
 */
public class TongueStateMachine {
    
    enum State
    {
        eStart,
        eFiringTongue,
        eRetractingTongue,
        eStuckToBlock,
        eRetractingWithBlock,
        eFoodInMouth,
        eFiringHammer,
        eRetractingHammer,
        eSpittingBlock,
        ePlacingBlock,
        eSpitting,
        eIdleAnimation,
        eSwinging,
        eStatesMax
    }
    
    
    //static members
    static Vec2 mUp = new Vec2(0,-1);
    static int tongueFiringTimeout = 10;
    static int actionDelay = 10;
    static float tongueLength = 6.0f;
    static int idleAnimationTrigger = 1000;
    
    //members (protected to allow access by PlayerInputController
    protected State mState;
    private int mCurrentStateTimer;
    private PlayerInputController mAIController;
    private DistanceJoint mJoint = null;
    private iSprite mTongueEndSprite;
    protected Vec2 mTongueDir = new Vec2(1,0);
    private Vec2 mTonguePosition = new Vec2(0,0);
    private float mTargetDistance = 0; //the distance the tongue trys to achieve
    private float mLastLength = 0;
    private int mAmmoLeft;
    protected boolean mIsTongueActive = false;
    private String mBlockMaterial;
    private Tile mTile;

    void layBlock()
    {
        if (mState.equals(State.eFoodInMouth))
        {
            changeState(State.ePlacingBlock);
        }
    }
    
    
    
    public TongueStateMachine(PlayerInputController _aIController)
    {
        mAIController = _aIController;
        mState = State.eStart;
        mCurrentStateTimer = 0;
        mBlockMaterial = "SomeSoftMaterial";
        mTile = null;
        mAmmoLeft = 0;
        HashMap params = new HashMap();
        params.put("ref", "mouthBlock");
        mTongueEndSprite = sSpriteFactory.create("simple", params);
        mTongueEndSprite.setVisible(false);
    }
    private Tile extendTongue(boolean _grabBlock)
    {
        float currentLength = ((float)mCurrentStateTimer/(float)tongueFiringTimeout)*tongueLength;
        setTongue(mTongueDir, currentLength);
        
        Vec2 tongueOffset = mTongueDir.mul(currentLength);
        mTonguePosition = mAIController.mEntity.mBody.getPosition().add(tongueOffset);
        
        //update end of tongue sprite's position
        mTongueEndSprite.setPosition(mTonguePosition.add(new Vec2(0.25f,0.25f)).mul(64));
        
        if (_grabBlock)
            return mAIController.grabBlock(mTonguePosition);
        else
            return null;
    }
    private boolean hammerCollide()
    {                
        Vec2 tongueOffset = mTongueDir.mul(((float)mCurrentStateTimer/(float)tongueFiringTimeout)*tongueLength);
        return mAIController.hammer(mAIController.mEntity.mBody.getPosition().add(tongueOffset));
    }
    private boolean hasFood()
    {
        return mAmmoLeft != 0;
    }
    private int setAnimation(String _name)
    {
        mAIController.mEntity.mSkin.startAnim(_name, false, 0.0f);
        return 1;
    }
    
    public void setTongue(final Vec2 _direction, final float _distance)
    {
        mAIController.mEntity.mSkin.setOffset("tng", new Vec2(32,32).add(_direction.mul(0.4f*64)));
        mAIController.mEntity.mSkin.setDimentions("tng", 0, _distance*64);
        double angle = Math.acos(Vec2.dot(_direction, mUp));
        if(_direction.x < 0)
                angle = ((2*Math.PI) - angle);
        mAIController.mEntity.mSkin.setRotation("tng", 180.0f + (float)(angle*(180.0/Math.PI)));
    }
    public void tick(AIEntity _entity)
    {
        switch (mState)
        {
            case eStart:
            {
                break;
            }
            case eFiringTongue:
            {
                mCurrentStateTimer++;
                mTile = extendTongue(true);
                if (mTile == null)
                {
                    if (mCurrentStateTimer > tongueFiringTimeout)
                    {
                        changeState(State.eRetractingTongue);
                    }
                }
                else switch (mTile.getTileType())
                {
                    case eGum:
                    case eEdible:
                    case eMelonFlesh:
                    case eChilli:
                    {
                        changeState(State.eStuckToBlock);
                        break;
                    }
                    case eSwingable:
                    {
                        changeState(State.eSwinging);
                        break;
                    }
                    case eIce:
                    case eIndestructible:
                    {
                        Vec2 pos = sWorld.translateToWorld(mTonguePosition).sub(sWorld.getPixelTranslation());
                        pos = pos.add(new Vec2(32,32));
                        ParticleSysBase sys = sParticleManager.createSystem(mTile.getAnimationsName(AnimationType.eSpit) + "Spit", pos, 1f);
                        if(mTongueDir.x >= 0)
                            sys.setAngularOffset(-(float)(Math.acos(Vec2.dot(mTongueDir, mUp.negate())) * 180/Math.PI));
                        else
                            sys.setAngularOffset((float)(Math.acos(Vec2.dot(mTongueDir, mUp.negate())) * 180/Math.PI));
                        changeState(State.eRetractingTongue);
                        break;
                    }
                    default:
                    case eTileTypesMax:
                    {
                        if (mCurrentStateTimer > tongueFiringTimeout)
                        {
                            changeState(State.eRetractingTongue);
                        }
                        break;
                    }
                }
                break;
            }
            case eRetractingTongue:
            {
                mCurrentStateTimer--;
                extendTongue(false);
                if (mCurrentStateTimer == 0)
                {
                    changeState(State.eStart);
                }
                break;
            }
            case eStuckToBlock:
            {
                if (mBlockMaterial.equals("SomeSoftMaterial"))
                {
                    changeState(State.eRetractingWithBlock);
                }
                break;
            }
            case eRetractingWithBlock:
            {
                mCurrentStateTimer--;
                extendTongue(false);
                if (mCurrentStateTimer == 0)
                {
                    if (mTile.getTileType().equals(TileType.eMelonFlesh))
                    {
                        mAmmoLeft = 10;
                    }
                    else if (mTile.getTileType().equals(TileType.eChilli))
                    {
                        mAmmoLeft = 4;
                    }
                    else
                    {
                        mAmmoLeft = 1;
                    }
                    changeState(State.eFoodInMouth);
                }
                break;
            }
            case eFoodInMouth:
            {
                break;
            }
            case eFiringHammer:
            {
                mCurrentStateTimer++;
                extendTongue(false);
                if (hammerCollide())
                {
                    changeState(State.eRetractingHammer);
                }
                else if (mCurrentStateTimer == tongueFiringTimeout)
                {
                    changeState(State.eRetractingHammer);
                }
                break;
            }
            case eRetractingHammer:
            {
                mCurrentStateTimer--;
                extendTongue(false);
                if (mCurrentStateTimer == 0)
                {
                    changeState(State.eFoodInMouth);
                }
                break;
            }
            case eSpittingBlock:
            {
                mCurrentStateTimer--;
                if (mCurrentStateTimer == 0)
                {
                    if (hasFood())
                    {
                        changeState(State.eFoodInMouth);
                    }
                    else
                    {
                        changeState(State.eStart);
                    }
                }
                break;
            }
            case ePlacingBlock:
            {
                mCurrentStateTimer--;
                if (mCurrentStateTimer == 0)
                {
                    mTile = null;
                    changeState(State.eStart);
                }
                break;
            }
            case eSpitting:
            {
                mCurrentStateTimer--;
                if (mCurrentStateTimer == 0)
                {
                    if (mAmmoLeft == 0)
                        changeState(State.eStart);
                    else
                        changeState(State.eFoodInMouth);
                }
                break;
            }
            case eIdleAnimation:
            {
                mCurrentStateTimer--;
                if (mCurrentStateTimer == 0)
                {
                    changeState(State.eStart);
                }
                break;
            }
            case eSwinging:
            {
                if (tongueAttachment.hasContact())
                {
                    //mTongueDir = (mJoint.m_bodyB.getPosition().add(mJoint.m_localAnchor2)).sub((mJoint.m_bodyA.getPosition().add(mJoint.m_localAnchor1)));
                    mTongueDir = mJoint.m_bodyB.getWorldPoint(mJoint.m_localAnchor2).sub(mAIController.mEntity.mBody.getPosition());
                    float actualLength = mTongueDir.normalize();
                    setTongue(mTongueDir, actualLength); //lock tongue to block
                    if(actualLength < mLastLength)
                        mJoint.m_frequencyHz = 0.001f;
                    else
                        mJoint.m_frequencyHz = 3.0f;
                     mLastLength = actualLength;
                    if(actualLength > mTargetDistance)
                    {
                        actualLength = mTargetDistance;
                    }
                    else
                    {
                        mJoint.m_frequencyHz = 0.001f;
                    }
                    
                    //mJoint.m_dampingRatio = 1.0f;
                    
                    mJoint.m_length = actualLength;
                    //amAIController.mEntity.

                    //mJoint.m_length -= 0.01f;
                    //mJoint.m_length *= 0.99f; //Try either of these
                }
                else
                {
                    tongueAttachment = null;
                    changeState(State.eRetractingTongue);
                    tick(_entity);
                }
                break;
            }
        }
    }
    public void leftClick(Vec2 _position)
    {
        switch (mState)
        {
            case eStart:
            {
                mTonguePosition = _position;
                changeState(State.eFiringTongue);
                break;
            }
            case eFiringTongue:
            {
                /// assert(false);
                break;
            }
            case eRetractingTongue:
            {
                break;
            }
            case eStuckToBlock:
            {
                /// assert(false);
                break;
            }
            case eRetractingWithBlock:
            {
                break;
            }
            case eFoodInMouth:
            {
                mTonguePosition = _position;
                changeState(State.eFiringHammer);
                break;
            }
            case eFiringHammer:
            {
                break;
            }
            case eRetractingHammer:
            {
                break;
            }
            case eSpittingBlock:
            {
                break;
            }
            case eSpitting:
            {
                break;
            }
            case eIdleAnimation:
            {
                break;
            }
        }
        
    }
    public void rightClick(Vec2 _position)
    {
        switch (mState)
        {
            case eStart:
            {
                mTonguePosition = _position;
                changeState(State.eSpitting);
                break;
            }
            case eFiringTongue:
            {
                break;
            }
            case eRetractingTongue:
            {
                break;
            }
            case eStuckToBlock:
            {
                break;
            }
            case eRetractingWithBlock:
            {
                break;
            }
            case eFoodInMouth:
            {
                mTonguePosition = _position;
                changeState(State.eSpittingBlock);
                break;
            }
            case eFiringHammer:
            {
                break;
            }
            case eRetractingHammer:
            {
                break;
            }
            case eSpittingBlock:
            {
                break;
            }
            case eSpitting:
            {
                break;
            }
            case eIdleAnimation:
            {
                break;
            }
            case eSwinging:
            {
                break;
            }
        }
    }
    public void leftRelease(Vec2 _position)
    {
        switch (mState)
        {
            case eStart:
            {
                /// assert(false);
                break;
            }
            case eFiringTongue:
            {
                changeState(State.eRetractingTongue);
                break;
            }
            case eRetractingTongue:
            {
                break;
            }
            case eStuckToBlock:
            {
                changeState(State.eRetractingTongue);
                break;
            }
            case eRetractingWithBlock:
            {
                break;
            }
            case eFoodInMouth:
            {
                break;
            }
            case eFiringHammer:
            {
                break;
            }
            case eRetractingHammer:
            {
                break;
            }
            case eSpittingBlock:
            {
                break;
            }
            case eSpitting:
            {
                break;
            }
            case eIdleAnimation:
            {
                break;
            }
            case eSwinging:
            {
                tongueAttachment = null;
                changeState(State.eRetractingTongue);
            }
        }
    }
    public void rightRelease(Vec2 _position)
    {
        switch (mState)
        {
            case eStart:
            {
                /// assert(false);
                break;
            }
            case eFiringTongue:
            {
                break;
            }
            case eRetractingTongue:
            {
                break;
            }
            case eStuckToBlock:
            {
                break;
            }
            case eRetractingWithBlock:
            {
                break;
            }
            case eFoodInMouth:
            {
                break;
            }
            case eFiringHammer:
            {
                break;
            }
            case eRetractingHammer:
            {
                break;
            }
            case eSpittingBlock:
            {
                break;
            }
            case eSpitting:
            {
                break;
            }
            case eSwinging:
            {
                break;
            }
            case eIdleAnimation:
            {
                break;
            }
        }
    }
    
    private void changeState(State _state)
    {
        switch(mState)
        {
            case eSwinging:
            {
                sWorld.destroyJoint(mJoint);
                mJoint = null;
            }
            default:
            {

            }
        }
        switch (_state) //new 
        {
            case eStart:
            {
                //set body type
                ((PlayerEntity)mAIController.mEntity).changeBodyType(TileType.eTileTypesMax);
                //no tongue
                mAIController.mEntity.mSkin.stopAnim("tng");
                mIsTongueActive = false;
                mCurrentStateTimer = 0;
                break;
            }
            case eFiringTongue:
            {
                mTongueDir = mAIController.mPlayerDir; //assume nomalised
                //render tongue
                mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                mIsTongueActive = true;
                break;
            }
            case eRetractingTongue:
            {
                //render tongue
                mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                mIsTongueActive = true;
                break;
            }
            case eStuckToBlock:
            {
                //render tongue
                mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                mIsTongueActive = true;
                break;
            }
            case eRetractingWithBlock:
            {
                //render tongue
                mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                mIsTongueActive = true;
                break;
            }
            case eFoodInMouth:
            {
                //STOP DISPLAYING TONGUE END
                mTongueEndSprite.setVisible(false);
                //set body type
                ((PlayerEntity)mAIController.mEntity).changeBodyType(mTile.getTileType());
                //no tongue
                mAIController.mEntity.mSkin.stopAnim("tng");
                mIsTongueActive = false;
                mCurrentStateTimer = 0;
                break;
            }
            case eFiringHammer:
            {
                mTongueDir = mAIController.mPlayerDir; //assume nomalised
                //DISPLAY TONGUE END
                mTongueEndSprite.setVisible(true);
                //render tongue
                mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                mIsTongueActive = true;
                break;
            }
            case eRetractingHammer:
            {
                //render tongue
                mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                mIsTongueActive = true;
                //mCurrentStateTimer = Math.max(actionDelay, mCurrentStateTimer);
                break;
            }
            case eSpittingBlock:
            {
                //no tongue
                mAIController.mEntity.mSkin.stopAnim("tng");
                mIsTongueActive = false;
                mCurrentStateTimer = setAnimation("SpittingBlock");
                mCurrentStateTimer = Math.max(actionDelay, mCurrentStateTimer);
                spitBlock();
                break;
            }
            case ePlacingBlock:
            {
                mAmmoLeft = 0;
                mAIController.layBlock(mTile);
                mCurrentStateTimer = setAnimation("PlacingBlock");
                break;
            }
            case eSpitting:
            {
                //no tongue
                mAIController.mEntity.mSkin.stopAnim("tng");
                mIsTongueActive = false;
                mCurrentStateTimer = setAnimation("Spitting");
                break;
            }
            case eIdleAnimation:
            {
                //set body type
                ((PlayerEntity)mAIController.mEntity).changeBodyType(TileType.eTileTypesMax);
                //no tongue
                mAIController.mEntity.mSkin.stopAnim("tng");
                mIsTongueActive = false;
                mCurrentStateTimer = setAnimation("Idle");
                break;
            }
            case eSwinging:
            {
                //render tongue
                mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                mIsTongueActive = true;
                tongueAttachment = sWorld.getLastTongueHit(mAIController.mPlayer);
                mTargetDistance = 2;//mTonguePosition.sub(mAIController.mEntity.mBody.getPosition()).length();
                mJoint = sWorld.createTongueJoint(mAIController.mEntity.mBody);
            }
        }
        mState = _state;
    }
    //private DistanceJoint mJoint;
    private TongueAnchor tongueAttachment;
    private void spitBlock()
    {
        mAmmoLeft--;
        if (mTile.getTileType().equals(TileType.eChilli))
        {
            mAIController.breathFire();
        }
        else
        {
            //mAIController.spitBlock(mPosition, mTile);
            mAIController.spitBlock(mTile);
        }
    }
    public boolean isSwinging()
    {
        return (mState.equals(State.eSwinging));
    }
    public Vec2 getTongueDir()
    {
        return mTongueDir.clone();
    }
    //reel in tongue (in metres)
    public void reelInTongue(float _scalar)
    {
        mTargetDistance -= _scalar;
    }
}
