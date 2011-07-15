/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Entities.PlayerEntity;
import Entities.sEntityFactory;
import Events.GenericEvent;
import Events.TutorialSpawnEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Camera.FreeCamera;
import Graphics.sGraphicsManager;
import Level.sLevel;
import World.sWorld;
import java.util.ArrayList;
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
    ArrayList<PlayerEntity> mPlayers;
    ArrayList<IntroSection> mSections;
    Body mGroundBody;
    int mEndedPlayers = 0;
    public IntroMode()
    {
        mPlayers = new ArrayList<PlayerEntity>();
        mSections = new ArrayList<IntroSection>();
        sEvents.subscribeToEvent("TutorialSpawnEvent", this);
        sEvents.subscribeToEvent("PlayerEndedTutorialEvent", this);
    }
    boolean inited = false;
    public iGameMode update(float _time)
    {
        boolean sectionsLeft = false;
        for (int i = 0; i < mSections.size(); i++)
        {
            if (mSections.get(i) != null)
            {
                mSections.set(i, mSections.get(i).update());
                if (mSections.get(i) != null)
                    sectionsLeft = true;
                mPlayers.get(i).mIntroSection = mSections.get(i);
            }
        }
        if (!sectionsLeft)
        {
            Vec2 s = sGraphicsManager.getTrueScreenDimensions();
            sWorld.switchCamera(new FreeCamera(new Rectangle(0,0,s.x, 0 + s.y)));
            iGameMode raceMode = new RaceMode();
            cleanup();
            sLevel.loadLevel();
            if (inited)
                sEvents.unsubscribeToEvent("WindowResizeEvent", this);
            sEvents.unsubscribeToEvent("TutorialSpawnEvent", this);
            return raceMode;
        }
        //sLevel.update();
        sWorld.update(_time);
        sEvents.processEvents();
        return this;
    }
    private void cleanup()
    {
        //sWorld.destroyBody(mGroundBody);
        if (inited)
        {
            for (PlayerEntity player: mPlayers)
            {
                player.destroy();
                sWorld.destroyBody(player.mBody);
            }
        }
    }
    public void render(Graphics _graphics)
    {
        if (!inited)
        {
            sEvents.subscribeToEvent("WindowResizeEvent", this);
            inited = true;
        }
        sWorld.getCamera().render();
        /*for (IntroSection section: mSections)
        {
            if (section != null)
                section.render();
        }*/
    }

    public boolean trigger(iEvent _event)
    {
        if (_event.getName().equals("TutorialSpawnEvent"))
        {
            TutorialSpawnEvent event = (TutorialSpawnEvent)_event;
            IntroSection introSection = new IntroStartSection(event.getPosition(), event.getPlayerNumber());
            mSections.add(introSection);
            HashMap parameters = new HashMap();
            parameters.put("position", event.getPosition());
            parameters.put("playerNumber", event.getPlayerNumber());
            parameters.put("checkPoint", null);
            PlayerEntity player = (PlayerEntity)sEntityFactory.create("Player",parameters);
            player.mIntroSection = introSection;
            mPlayers.add(player);
        }
        else if (_event.getName().equals("PlayerEndedTutorialEvent"))
        {
            mEndedPlayers++;
            if (mEndedPlayers == mPlayers.size())
            {
                sEvents.triggerEvent(new GenericEvent("AllPlayersTutorialEndedEvent"));
                return false;
            }
        }
        else
        {
            /*WindowResizeEvent event = (WindowResizeEvent)_event;
            Vec2 s = sGraphicsManager.getTrueScreenDimensions();
            mSwitchedCamera.resize(new Rectangle(0, 0, s.x, s.y));*/
        }
        return true;
    }
}
