/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.AIEntity;
import Graphics.iSkin;
import Graphics.sSkinFactory;
import Level.sLevel;
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
    
    static int tongueFiringTimeout = 10;
    static float tongueLength = 6.0f;
    static int idleAnimationTrigger = 1000;
    int ammoLeft;
    
    Vec2 position = new Vec2(0,0);
    String mBlockMaterial;
    sLevel.TileType mTileType;
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
        eSpitting,
        eIdleAnimation,
        eSwinging,
        eStatesMax
    }
    State mState;
    int currentStateTimer;
    PlayerInputController mAIController;
    public TongueStateMachine(PlayerInputController _aIController)
    {
        mAIController = _aIController;
        mState = State.eStart;
        currentStateTimer = 0;
        mBlockMaterial = "SomeSoftMaterial";
        mTileType = sLevel.TileType.eTileTypesMax;
        ammoLeft = 0;
    }
    private sLevel.TileType extendTongue(boolean _grabBlock)
    {
        mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
         //constant offset required due to offset applied during player creation
        Vec2 direction = position.sub(new Vec2(0.5f,0.5f)).sub(mAIController.mEntity.mBody.getPosition());
        direction.normalize();
        setTongue(direction, ((float)currentStateTimer/(float)tongueFiringTimeout)*tongueLength);
        direction = direction.mul(((float)currentStateTimer/(float)tongueFiringTimeout)*tongueLength);
        if (_grabBlock)
            return mAIController.grabBlock(mAIController.mEntity.mBody.getPosition().add(direction));
        else
            return sLevel.TileType.eTileTypesMax;
    }
    private boolean hammerCollide()
    {
        mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
        Vec2 direction = position.sub(mAIController.mEntity.mBody.getPosition());
        direction.normalize();
        setTongue(direction, ((float)currentStateTimer/(float)tongueFiringTimeout)*tongueLength);
        direction = direction.mul(((float)currentStateTimer/(float)tongueFiringTimeout)*tongueLength);
        
        HashMap parameters = new HashMap();
        parameters.put("ref", "ChewedBlock");
        iSkin skin = sSkinFactory.create("static", parameters);
        Vec2 hammerPosition = mAIController.mEntity.mBody.getPosition();
        hammerPosition = sWorld.translateToWorld(hammerPosition);
        skin.render(hammerPosition.x, hammerPosition.y);
                
        return mAIController.hammer(mAIController.mEntity.mBody.getPosition().add(direction));
    }
    private boolean hasFood()
    {
        return false;
    }
    private int setAnimation(String _name)
    {
        return 1;
    }
    static Vec2 mUp = new Vec2(0,-1);
    public void setTongue(Vec2 _direction, float _distance)
    {
        //calc. direction of tongue
        mAIController.mEntity.mSkin.setDimentions("tng", 0, _distance*64);
        Vec2 direction = _direction.clone();
        direction.normalize();
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
                currentStateTimer++;
                mTileType = extendTongue(true);
                switch (mTileType)
                {
                    case eGum:
                    case eEdible:
                    case eWaterMelon:
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
                        if (currentStateTimer > tongueFiringTimeout)
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
                currentStateTimer--;
                extendTongue(false);
                if (currentStateTimer == 0)
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
                currentStateTimer--;
                extendTongue(false);
                if (currentStateTimer == 0)
                {
                    if (mTileType == TileType.eWaterMelon)
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
                currentStateTimer++;
                if (hammerCollide())
                {
                    changeState(State.eRetractingHammer);
                }
                else if (currentStateTimer == tongueFiringTimeout)
                {
                    changeState(State.eRetractingHammer);
                }
                break;
            }
            case eRetractingHammer:
            {
                currentStateTimer--;
                extendTongue(false);
                if (currentStateTimer == 0)
                {
                    changeState(State.eFoodInMouth);
                }
                break;
            }
            case eSpittingBlock:
            {
                currentStateTimer--;
                if (currentStateTimer == 0)
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
            case eSpitting:
            {
                currentStateTimer--;
                if (currentStateTimer == 0)
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
                currentStateTimer--;
                if (currentStateTimer == 0)
                {
                    changeState(State.eStart);
                }
                break;
            }
            case eSwinging:
            {
                Vec2 direction = mJoint.m_bodyB.getPosition().sub(mJoint.m_bodyA.getPosition());
                float actualLength = direction.normalize();
                setTongue(direction, actualLength);
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
                position = _position;
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
                position = _position;
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
                position = _position;
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
                position = _position;
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
    boolean mIsTongueActive = false;
    private void changeState(State _state)
    {
        switch (_state)
        {
            case eStart:
            {
                //no tongue
                if(mIsTongueActive == true)
                {
                    mAIController.mEntity.mSkin.stopAnim("tng");
                    mIsTongueActive = false;
                }
                currentStateTimer = 0;
                break;
            }
            case eFiringTongue:
            {
                //render tongue
                if(mIsTongueActive == false)
                {
                    mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                    mIsTongueActive = true;
                }
                break;
            }
            case eRetractingTongue:
            {
                //render tongue
                if(mIsTongueActive == false)
                {
                    mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                    mIsTongueActive = true;
                }
                break;
            }
            case eStuckToBlock:
            {
                //render tongue
                if(mIsTongueActive == false)
                {
                    mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                    mIsTongueActive = true;
                }
                break;
            }
            case eRetractingWithBlock:
            {
                //render tongue
                if(mIsTongueActive == false)
                {
                    mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                    mIsTongueActive = true;
                }
                break;
            }
            case eFoodInMouth:
            {
                //no tongue
                if(mIsTongueActive == true)
                {
                    mAIController.mEntity.mSkin.stopAnim("tng");
                    mIsTongueActive = false;
                }
                currentStateTimer = 0;
                break;
            }
            case eFiringHammer:
            {
                //render tongue
                if(mIsTongueActive == false)
                {
                    mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                    mIsTongueActive = true;
                }
                currentStateTimer = 0;
                break;
            }
            case eRetractingHammer:
            {
                //render tongue
                if(mIsTongueActive == false)
                {
                    mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                    mIsTongueActive = true;
                }
                break;
            }
            case eSpittingBlock:
            {
                //no tongue
                if(mIsTongueActive == true)
                {
                    mAIController.mEntity.mSkin.stopAnim("tng");
                    mIsTongueActive = false;
                }
                currentStateTimer = setAnimation("SpittingBlock");
                spitBlock();
                break;
            }
            case eSpitting:
            {
                //no tongue
                if(mIsTongueActive == true)
                {
                    mAIController.mEntity.mSkin.stopAnim("tng");
                    mIsTongueActive = false;
                }
                currentStateTimer = setAnimation("Spitting");
                break;
            }
            case eIdleAnimation:
            {
                if(mIsTongueActive == true)
                {
                    //no tongue
                    mAIController.mEntity.mSkin.stopAnim("tng");
                    mIsTongueActive = false;
                }
                currentStateTimer = setAnimation("Idle");
                break;
            }
            case eSwinging:
            {
                if(mIsTongueActive == false)
                {
                    //render tongue
                    mAIController.mEntity.mSkin.startAnim("tng", false, 0.0f);
                    mIsTongueActive = true;
                }
                Vec2 direction = position.sub(mAIController.mEntity.mBody.getPosition());
                direction.normalize();
                setTongue(direction, ((float)currentStateTimer/(float)tongueFiringTimeout)*tongueLength);
                direction = direction.mul(((float)currentStateTimer/(float)tongueFiringTimeout)*tongueLength);
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
        mAIController.spitBlock(position, mTileType);
    }
}
