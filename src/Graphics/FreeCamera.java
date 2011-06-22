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
        Vec2 s = sGraphicsManager.getScreenDimensions();
        xPixel = (int) (s.x/2);
        yPixel = (int) (s.y/2);
        sEvents.subscribeToEvent("KeyDownEvent" + "w", this);
        sEvents.subscribeToEvent("KeyDownEvent" + "a", this);
        sEvents.subscribeToEvent("KeyDownEvent" + "s", this);
        sEvents.subscribeToEvent("KeyDownEvent" + "d", this);
    }
    public Vec2 translateToWorld(Vec2 _physicsSpace)
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        Vec2 worldSpace = new Vec2(_physicsSpace.x-(xPixel/64.0f),_physicsSpace.y-(yPixel/64.0f));
        return new Vec2((s.x/2)+((worldSpace.x*64.0f)*64.0f), (s.y/2)+((worldSpace.y)*64.0f));
    }
    public Vec2 translateToPhysics(Vec2 _worldSpace)
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        Vec2 physicsSpace = new Vec2(_worldSpace.x/64.0f,_worldSpace.y/64.0f);
        physicsSpace.x += xPixel/64.0f;
        physicsSpace.y += yPixel/64.0f;
        physicsSpace.x -= (s.x/2)/64.0f;
        physicsSpace.y -= (s.y/2)/64.0f;
        return physicsSpace;        
    }
    public Vec2 getPixelTranslation()
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        return new Vec2((s.x/2)+(-xPixel),(s.y/2)+(-yPixel));        
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
