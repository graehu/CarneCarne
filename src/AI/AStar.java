/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import AI.iPathFinding.Command;
import Entities.Entity;
import Level.sLevel;
import Level.sLevel.PathInfo;



/**
 *
 * @author Graham
 */
public class AStar implements iPathFinding 
{
    int mXNow, mYNow;
    int mXDest, mYDest;
    Command mCommand;

    public AStar()
    {
        //pass entity type into constructor to work out
        //pathing costs per entity.
        mXNow = mYNow = mXDest = mYDest = -1;
        //mLeft = true;
    }

    public Command follow()
    {
           return mCommand;
    }
    
    public void updatePosition(int _newX, int _newY, int _newXDest, int _newYDest)
    {       
        //if(_newX != mXNow && _newY != mYNow && _newXDest != mXDest && _newYDest != mYDest)
        //{
            mXNow = _newX;
            mYNow = _newY;
            mXDest = _newXDest;
            mYDest = _newYDest;
            path();
        //}
        
    }
    
    private void path()
    {
        if (mXDest > mXNow)
        {
                mCommand = Command.eWalkRight;
        }
        else if(mXDest < mXNow)
        {
            mCommand = Command.eWalkLeft;
        }
        else
        {
            mCommand = Command.eStandStill;
        }
        
        //if(mCommand == Command.eWalkLeft)
        //{
        if((sLevel.getPathInfo(mXNow, mYNow) == PathInfo.eNotPassable || sLevel.getPathInfo(mXNow, mYNow+1) == PathInfo.eAir) &&  (mCommand != Command.eWalkRight))
        {
            mCommand = Command.eStandStill;
        }        
        //}
        //else if(mCommand == Command.eWalkRight)
        //{
        if((sLevel.getPathInfo(mXNow+1, mYNow) == PathInfo.eNotPassable || sLevel.getPathInfo(mXNow+1, mYNow+1) == PathInfo.eAir) && (mCommand != Command.eWalkLeft))
        {
            mCommand = Command.eStandStill;
        }
        //}
  
    }
    
}
