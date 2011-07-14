/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Entities.PlayerEntity;
import Entities.sEntityFactory;
import Events.WindowResizeEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Camera.FreeCamera;
import Graphics.Camera.iCamera;
import Graphics.sGraphicsManager;
import Level.sLevel;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author alasdair
 */
public class IntroMode implements iGameMode, iEventListener
{
    PlayerEntity mPlayer;
    Body mGroundBody;
    iCamera mSwitchedCamera;
    IntroSection mSection;
    public IntroMode()
    {
        try
        {
        }
        finally
        {
            assert(false);
        }
        mSection = new IntroStartSection();
    }
    boolean inited = false;
    public iGameMode update(float _time)
    {
        mSection = mSection.update();
        if (mSection == null)
        {
            cleanup();
            sWorld.switchCamera(mSwitchedCamera);
            sLevel.loadLevel();
            sEvents.unsubscribeToEvent("WindowResizeEvent", this);
            return new RaceMode();
        }
        //sLevel.update();
        sWorld.update(_time);
        sEvents.processEvents();
        return this;
    }
    private void cleanup()
    {
        //sWorld.destroyBody(mGroundBody);
        mPlayer.destroy();
        sWorld.destroyBody(mPlayer.mBody);
    }
    public void render(Graphics _graphics)
    {
        if (!inited)
        {
            Vec2 s = sGraphicsManager.getTrueScreenDimensions();
            HashMap parameters = new HashMap();
            mSwitchedCamera = sWorld.switchCamera(new FreeCamera(new Rectangle(0,0,s.x, 0 + s.y))); // 66*64.0f
            parameters.put("position", new Vec2(4,72));
            parameters.put("playerNumber", 0);
            //parameters.put("checkPoint", new PlayerSpawnZone((int)position.x, (int)position.y, (int)position.x+1, (int)position.y+1, startZone));
            parameters.put("checkPoint", null);
            mPlayer = (PlayerEntity)sEntityFactory.create("Player",parameters);
            //sEvents.triggerEvent(new PlayerCreatedEvent(player,1));
            /*parameters.put("position", new Vec2(5,7.5f));
            mGroundBody = sWorld.useFactory("GroundBody", parameters);*/
            sEvents.subscribeToEvent("WindowResizeEvent", this);
            inited = true;
        }
        sWorld.getCamera().render();
        mSection.render();
    }

    public boolean trigger(iEvent _event)
    {
        WindowResizeEvent event = (WindowResizeEvent)_event;
        Vec2 s = sGraphicsManager.getTrueScreenDimensions();
        mSwitchedCamera.resize(new Rectangle(0, 0, s.x, s.y));
        return true;
    }
}
