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
    enum Instruction
    {
        eWalkLeft,
        eWalkRight,
        eStandStill,
        eInstructionMax
    }
    public void updatePosition(int x, int y, int x2, int y2);
    
    public Instruction follow();
}
