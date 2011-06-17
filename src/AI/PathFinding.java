/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Entities.Entity;
import java.util.Stack;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.util.pathfinding.Path;

/**
 *
 * @author Graham
 */
public interface PathFinding 
{ 
    
    public Path findPath(Entity _traveller, Vec2 _start, Vec2 _destination);
    
}
