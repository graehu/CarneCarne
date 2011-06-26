/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.AIEntity;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Graphics.Sprites.iSprite;
import Graphics.Sprites.sSpriteFactory;
import Level.Tile;
import Level.sLevel.TileType;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJoint;

/**
 *
 * @author alasdair
 */
public class TongueStateMachine {
    
    static Vec2 mUp = new Vec2(0,-1);
    static int tongueFiringTimeout = 10;
    static float tongueLength = 6.0f;
    static int idleAnimationTrigger = 1000;
    int ammoLeft;
    boolean mIsTongueActive = false;
    Vec2 mTongueDir = new Vec2(1,0);
    
    Vec2 mPosition = new Vec2(0,0);
    String mBlockMaterial;
    private Tile mTile;

    void layBlock()
    {
        if (mState.equals(State.eFoodInMouth))
        {
            changeState(State.ePlacingBlock);
        }
    }
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
    State mState;
    int mCurrentStateTimer;
    PlayerInputController mAIController;
    iSprite mTongueEndSprite;
    public TongueStateMachine(PlayerInputController _aIController)
    {
        mAIController = _aIController;
        mState = State.eStart;
        mCurrentStateTimer = 0;
        mBlockMaterial = "SomeSoftMaterial";
        mTile = null;
        ammoLeft = 0;
        HashMap params = new HashMap();
        params.put("ref", "CrossHair");
        mTongueEndSprite = sSpriteFactory.create("simple", params);
    }
    private Tile extendTongue(boolean _grabBlock)
    {
        
        mTongueDir = mAIController.mPlayerDir;
        setTongue(mTongueDir, ((float)mCurrentStateTimer/(float)tongueFiringTimeout)*tongueLength);
        Vec2 tongueOffset = mTongueDir.mul(((float)mCurrentStateTimer/(float)tongueFiringTimeout)*tongueLength);
        if (_grabBlock)
            return mAIController.grabBlock(mAIController.mEntity.mBody.getPosition().add(tongueOffset));
        else
            return null;
    }
    private boolean hammerCollide()
    {
        Vec2 direction = mPosition.sub(mAIController.mEntity.mBody.getPosition());
        direction.normalize();
        setTongue(direction, ((float)mCurrentStateTimer/(float)tongueFiringTimeout)*tongueLength);
        direction = direction.mul(((float)mCurrentStateTimer/(float)tongueFiringTimeout)*tongueLength);
        
        /// Need new image for this
        /*HashMap parameters = new HashMap();
        parameters.put("ref", "ChewedBlock");
        iSkin skin = sSkinFactory.create("static", parameters);
        Vec2 hammerPosition = mAIController.mEntity.mBody.getPosition();
        hammerPosition = sWorld.translateToWorld(hammerPosition);
        skin.render(hammerPosition.x, hammerPosition.y);*/
                
        return mAIController.hammer(mAIController.mEntity.mBody.getPosition().add(direction));
    }
    private boolean hasFood()
    {
        return false;
    }
    private int setAnimation(String _name)
    {
        mAIController.mEntity.mSkin.startAnim(_name, false, 0.0f);
        return 1;
    }
    
    public void setTongue(Vec2 _direction, float _distance)
    {
        //calc. direction of tongue
        mAIController.mEntity.mSkin.setDimentions("tng", 0, _distance*64);
        Vec2 direction = _direction.clone();
        float angle = (float)Math.acos(Vec2.dot(direction, mUp));
        if(direction.x < 0)
                angle = (float) ((2*Math.PI) - angle);
        mAIController.mEntity.mSkin.setRotation("tng", 180 + (angle*(180.0f/(float)Math.PI)));
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
                    if (mTile.getTileType() == TileType.eMelonFlesh)
                    {
                        ammoLeft = 10;
                    }
                    else
                    {
                        ammoLeft = 1;
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
                    if (ammoLeft == 0)
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
                mTongueDir = (mJoint.m_bodyB.getPosition().add(mJoint.m_localAnchor2)).sub((mJoint.m_bodyA.getPosition().add(mJoint.m_localAnchor1)));
                float actualLength = mTongueDir.normalize();
                setTongue(mTongueDir, actualLength);
                mJoint.m_length = actualLength * 0.99f;
                
                //mJoint.m_length -= 0.01f;
                // mJoint.m_length *= 0.99f; Try either of these
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
                mPosition = _position;
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
                mPosition = _position;
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
                mPosition = _position;
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
                mPosition = _position;
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
                sWorld.destroyJoint(mJoint);
                mJoint = null;
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
            case eIdleAnimation:
            {
                break;
            }
        }
    }
    
    private void changeState(State _state)
    {
        switch (_state)
        {
            case eStart:
            {
                //no tongue
                mAIController.mEntity.mSkin.stopAnim("tng");
                mIsTongueActive = false;
                mCurrentStateTimer = 0;
                break;
            }
            case eFiringTongue:
            {
                //calculate relative dir
                mTongueDir = mAIController.mPlayerDir;
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
                //no tongue
                mAIController.mEntity.mSkin.stopAnim("tng");
                mIsTongueActive = false;
                mCurrentStateTimer = 0;
                break;
            }
            case eFiringHammer:
            {
                //render tongue
                mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                mIsTongueActive = true;
                mCurrentStateTimer = 0;
                break;
            }
            case eRetractingHammer:
            {
                //render tongue
                mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                mIsTongueActive = true;
                break;
            }
            case eSpittingBlock:
            {
                //no tongue
                mAIController.mEntity.mSkin.stopAnim("tng");
                mIsTongueActive = false;
                mCurrentStateTimer = setAnimation("SpittingBlock");
                spitBlock();
                break;
            }
            case ePlacingBlock:
            {
                mAIController.layBlock(mTile); //FIXME: placeTile function should take rootID not TileType 
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
                Vec2 direction = mPosition.sub(mAIController.mEntity.mBody.getPosition());
                direction.normalize();
                setTongue(direction, ((float)mCurrentStateTimer/(float)tongueFiringTimeout)*tongueLength);
                direction = direction.mul(((float)mCurrentStateTimer/(float)tongueFiringTimeout)*tongueLength);
                Vec2 position = mAIController.mEntity.mBody.getPosition().add(direction);
                mJoint = sWorld.createTongueJoint(mAIController.mEntity.mBody);
            }
        }
        mState = _state;
    }
    private DistanceJoint mJoint;
    private void spitBlock()
    {
        ammoLeft--;
        mAIController.spitBlock(mPosition, mTile);
    }
}
