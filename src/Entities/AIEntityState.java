/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author alasdair
 */
class AIEntityState
{
    enum State
    {
        eWalking,
        eFalling,
        eSwimming,
        eTar,
        eIce,
        eDead,
        eStatesMax
    }
    private State mState;
    private int mFloorContacts;
    private int mTarContacts;
    private int mIceContacts;

    AIEntityState()
    {
        mState = State.eFalling;
        mFloorContacts = mTarContacts = mIceContacts = 0;
    }
    
    State getState()
    {
        return mState;
    }
    
    void setState(int _floorContacts, int _tarContacts, int _iceContacts)
    {
        mFloorContacts = _floorContacts;
        mTarContacts = _tarContacts;
        mIceContacts = _iceContacts;
    }
    
    void kill()
    {
        
    }
    
    void unkill()
    {
        
    }
}
