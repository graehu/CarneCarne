/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import AI.iPathFinding.Command;
import Entities.Entity;
import Level.sLevel;
import Level.sLevel.PathInfo;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import org.jbox2d.common.Vec2;




/**
 *
 * @author Graham
 */
public class AStar implements iPathFinding 
{
    int mXStart, mYStart;
    int mXTarget, mYTarget;
    int mMaxDepth;
    Entity mEntity;
    Command mCommand;
    private LinkedList<Node> mWorkingSet = new LinkedList();
    private LinkedList<Node> mClosedSet = new LinkedList();
    private LinkedList<Vec2> mPath = new LinkedList();
    iHeuristic mHeuristic;
    ///private Array bestPath;
    ///private Array heuristicPath;
    ///private Array totalCost;
    ///private Path mPath;
    
    public AStar(Entity _entity, iHeuristic _Heuristic)
    {
        // pass entity type into constructor to work out
        // pathing costs per entity.
        mXStart = mYStart = mXTarget = mYTarget = -1;
        mEntity = _entity;
        mHeuristic = _Heuristic;
        mMaxDepth = 999999; //large number (y)
    }
    
    public void setMaxDepth(int _maxDepth)
    {
        mMaxDepth = _maxDepth;
    }

    public Command follow()
    {
        if (mXTarget > mXStart)
        {
                mCommand = Command.eMoveRight;
        }
        else if(mXTarget < mXStart)
        {
            mCommand = Command.eMoveLeft;
        }
                
        else
        {
            mCommand = Command.eStandStill;
        }
        
        /*if((sLevel.getPathInfo(mXStart, mYStart) == PathInfo.eNotPassable || sLevel.getPathInfo(mXStart, mYStart+1) == PathInfo.eAir) &&  (mCommand != Command.eMoveRight))
        {
            mCommand = Command.eStandStill;
        }
        if((sLevel.getPathInfo(mXStart+1, mYStart) == PathInfo.eNotPassable || sLevel.getPathInfo(mXStart+1, mYStart+1) == PathInfo.eAir) && (mCommand != Command.eMoveLeft))
        {
            mCommand = Command.eStandStill;
        }*/
        return mCommand;
    }
    
    public void updatePath(int _xStart, int _yStart, int _xTarget, int _yTarget)
    {       
        if(_xStart != mXStart || _yStart != mYStart || _xTarget != mXTarget || _yTarget != mYTarget)
        {
            mXStart = _xStart;
            mYStart = _yStart;
            mXTarget = _xTarget;
            mYTarget = _yTarget;
            path();
        }
    }
    
    private boolean path()
    { 
        mWorkingSet.clear();
        mClosedSet.clear();
        mPath.clear();
        Node startNode = new Node(mXStart,mYStart);
        mWorkingSet.add(startNode);
        mWorkingSet.peek().mCost = mHeuristic.getCost(mXStart, mXStart, mXTarget, mXTarget);
        
        Node targetNode = new Node(mXTarget, mYTarget);
        targetNode.mCost = mHeuristic.getCost(mXTarget, mYTarget, mXTarget, mXTarget); //this should equal zero.
        
        Node workingNode;
        int maxDepth = 0;
        
        while(maxDepth < mMaxDepth && mWorkingSet.size() != 0)
        {
            workingNode = mWorkingSet.peek();
            if(workingNode == targetNode)
                break;
            
            mWorkingSet.remove();
            mClosedSet.add(workingNode);
            
            for (int x = -1; x < 2; x++) 
            {
                for (int y = -1; y < 2; y++) 
                {
                    if ((x == 0) && (y == 0))
                        continue;
                    
                    int xp = x + workingNode.mX;
                    int yp = y + workingNode.mY;
                    
                    if(sLevel.getPathInfo(xp, yp) == PathInfo.eAir)
                    {
                        Node neighbour = new Node(xp,yp);
                        //neighbour.mCost = mHeuristic.getCost(xp,yp, mXTarget, mXTarget);
                        float stepCost = workingNode.mCost + mHeuristic.getCost(xp,yp, mXTarget, mXTarget);
                        
                        if(stepCost < neighbour.mCost) 
                        {
                            if (mWorkingSet.contains(neighbour)) 
                            {
                                mWorkingSet.remove(neighbour);
                            }
                            if (mClosedSet.contains(neighbour)) 
                            {
                                mClosedSet.remove(neighbour);
                            }
                        }
                        
                        if (!mWorkingSet.contains(neighbour) && !(mClosedSet.contains(neighbour)))
                        {
                                neighbour.mCost = stepCost;
                                neighbour.mHeuristic = mHeuristic.getCost(xp,yp, mXTarget, mXTarget);
                                neighbour.mParent = workingNode;
                                neighbour.mDepth = neighbour.mParent.mDepth + 1;
                                maxDepth = Math.max(maxDepth, neighbour.mDepth);
                                mWorkingSet.add(neighbour);
                        }
                        
                    }
                }
            }
                // since we'e've run out of search 
		// there was no path. Just return null
            
		if (workingNode.mParent == null)
                {
			return false;
		}
		
		while (targetNode != startNode)
                {
                    //mPath.push(new Vec2(targetNode.mX, targetNode.mY));
                    mPath.addFirst(new Vec2(targetNode.mX, targetNode.mY));
                    targetNode = targetNode.mParent;
		}
		mPath.push(new Vec2(mXStart, mYStart));
                return true;
        }
        
        return false;
    }

}

