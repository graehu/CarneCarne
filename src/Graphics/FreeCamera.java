/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Events.KeyDownEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import org.jbox2d.common.Vec2;

/**
 *
 * @author a203945
 */
public class FreeCamera implements iCamera, iEventListener {
    
    int xPixel, yPixel;
    
    public FreeCamera()
    {
        xPixel = 400;
        yPixel = 300;
        sEvents.subscribeToEvent("KeyDownEvent" + "w", this);
        sEvents.subscribeToEvent("KeyDownEvent" + "a", this);
        sEvents.subscribeToEvent("KeyDownEvent" + "s", this);
        sEvents.subscribeToEvent("KeyDownEvent" + "d", this);
    }
    public Vec2 translateToWorld(Vec2 _physicsSpace)
    {
        Vec2 worldSpace = new Vec2(_physicsSpace.x-(xPixel/64.0f),_physicsSpace.y-(yPixel/64.0f));
        return new Vec2(400.0f+((worldSpace.x*64.0f)*64.0f), 300.0f+((worldSpace.y)*64.0f));
    }
    public Vec2 translateToPhysics(Vec2 _worldSpace)
    {
        Vec2 physicsSpace = new Vec2(_worldSpace.x/64.0f,_worldSpace.y/64.0f);
        physicsSpace.x += xPixel/64.0f;
        physicsSpace.y += yPixel/64.0f;
        physicsSpace.x -= 400.0f/64.0f;
        physicsSpace.y -= 300.0f/64.0f;
        return physicsSpace;        
    }
    public Vec2 getPixelTranslation()
    {
        return new Vec2(400.0f+(-xPixel),300.0f+(-yPixel));        
    }

    public void trigger(iEvent _event) {
        KeyDownEvent event = (KeyDownEvent)_event;
        switch (event.getKey())
        {
            case 'w':
            {
                yPixel -= 5;
                break;
            }
            case 'a':
            {
                xPixel -= 5;
                break;
            }
            case 's':
            {
                yPixel += 5;
                break;
            }
            case 'd':
            {
                xPixel += 5;
                break;
            }
        }
    }
}
