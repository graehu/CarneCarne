/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Graphics.sGraphicsManager;
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
        Vec2 s = sGraphicsManager.getScreenDimensions();
        //FIXME: assume carne is in the cenre of the screen....
        Vec2 carnePosition = sWorld.translateToPhysics(new Vec2((s.x/2)-32,(s.x/2)-32)); //const 32 to offset to carne position
        carnePosition = carnePosition.add(_direction.mul(500));
        mPosition = sWorld.translateToWorld(carnePosition);
    }

    
}
