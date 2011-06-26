/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

/**
 *
 * @author Graham
 */
public interface iPathFinding 
{ 
    enum Command
    {
        eWalkLeft,
        eWalkRight,
        eStandStill,
        eInstructionMax
    }
    public void updatePosition(int _xNow, int _yNow, int _xDest, int _yDest);
    
    public Command follow();
}
