/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import AI.iPathFinding.Instruction;


/**
 *
 * @author Graham
 */
public class aStarPathFinder implements iPathFinding 
{
    int x, y;
    int x2, y2;

    public aStarPathFinder()
    {
        x = y = x2 = y2 = -1;
    }

    public Instruction follow()
    {
        return Instruction.eWalkLeft;
    }
    
    public void updatePosition(int _newX, int _newY, int _newX2, int _newY2)
    {
        if (_newX != x && _newY != y && _newX2 != x2 && _newY2 != y2)
        {
            x = _newX;
            y = _newY;
            x2 = _newX2;
            y2 = _newY2;
            path();
        }
    }
    
    private void path()
    {
    }
    
}
