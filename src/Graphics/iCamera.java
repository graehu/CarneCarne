/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import org.jbox2d.common.Vec2;

/**
 *
 * @author A203946
 */
public interface iCamera {
    
    public Vec2 translateToWorld(Vec2 _physicsSpace);
    public Vec2 translateToPhysics(Vec2 _worldSpace);
    public Vec2 getPixelTranslation();
}
