/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import AI.iPathFinding.Command;
import Entities.Entity;
import Level.sLevel;
import Level.sLevel.PathInfo;
import java.util.Iterator;
import java.util.LinkedList;
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
    private LinkedList<Node> mNodes = new LinkedList();
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
        mMaxDepth = 13; //large number (y)
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
        
        if((sLevel.getPathInfo(mXStart, mYStart) == PathInfo.eNotPassable || sLevel.getPathInfo(mXStart, mYStart+1) == PathInfo.eAir) &&  (mCommand != Command.eMoveRight))
        {
            mCommand = Command.eStandStill;
        }
        if((sLevel.getPathInfo(mXStart+1, mYStart) == PathInfo.eNotPassable || sLevel.getPathInfo(mXStart+1, mYStart+1) == PathInfo.eAir) && (mCommand != Command.eMoveLeft))
        {
            mCommand = Command.eStandStill;
        }
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
        
        Node workingNode = null; 
        int maxDepth = 0;
        
        while(maxDepth < mMaxDepth && mWorkingSet.size() != 0)
        {
            workingNode = mWorkingSet.peek();
            if(workingNode.mX == targetNode.mX && workingNode.mY == targetNode.mY)
                break;
            
            mWorkingSet.remove();
            mClosedSet.add(workingNode);
            Node neighbour = null;
            
            for (int x = -1; x < 2; x++) 
            {
                for (int y = -1; y < 2; y++) 
                {
                    if ((x == 0) && (y == 0))
                    {
                        continue;
                    }
                    
                    int xp = x + workingNode.mX;
                    int yp = y + workingNode.mY;
                    
                    if(sLevel.getPathInfo(xp, yp) == PathInfo.eAir)
                    {
                        if(workingNode.mParent != null)
                        {
                            if(xp == workingNode.mParent.mX && yp == workingNode.mParent.mY)
                            {
                                neighbour = workingNode;
                            }
                            else
                            {
                                neighbour = null;
                            }
                        }
                        else
                        {
                            neighbour = null;
                        }
                        if(neighbour == null)
                        {
                            if(!mNodes.isEmpty())
                            {
                                Iterator<Node> it = mNodes.listIterator();
                                while(it.hasNext())
                                {
                                    Node node = it.next();
                                    if(node.mX == xp && node.mY == yp)
                                    {
                                        neighbour = node;
                                        break;
                                    }
                                    else
                                    {
                                        neighbour = null;
                                    }
                                }
                            }
                        }
                        if(neighbour == null)
                        {
                            neighbour = new Node(xp,yp);
                            neighbour.mCost = mHeuristic.getCost(xp,yp, mXTarget, mXTarget);
                            mNodes.add(neighbour);
                        }
                        
                        float stepCost = 0;
                        
                        if((xp > workingNode.mX || xp < workingNode.mX)^(yp > workingNode.mY || yp < workingNode.mY))
                        {
                            stepCost = 1 +  workingNode.mCost;
                        }
                        else
                        {
                            stepCost = 1.4f + workingNode.mCost;
                        }
                        
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
        }
        
        if (workingNode.mParent == null)
        {
            return false;
	}
		
		while (true)
                {
                    if(workingNode.mX == startNode.mX && workingNode.mY == startNode.mY)
                        break;
                    mPath.addFirst(new Vec2(workingNode.mX, workingNode.mY));
                    workingNode = workingNode.mParent;
		}
		mPath.push(new Vec2(mXStart, mYStart));
                return true;
    }

}

