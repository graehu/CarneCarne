/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import World.sWorld;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class ShoulderButtonEvent extends MapClickEvent {

    public ShoulderButtonEvent(Vec2 _direction, boolean _leftButton, int _player)
    {
        super(_direction, _leftButton, _player);
        Vec2 carnePosition = sWorld.translateToPhysics(new Vec2(400-32,300-32));
        carnePosition = carnePosition.add(_direction.mul(500));
        mPosition = sWorld.translateToWorld(carnePosition);
    }

    
}
