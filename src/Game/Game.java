/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import org.newdawn.slick.*;
import Physics.sPhysics;
import Events.sEvents;
import Entities.sEntityFactory;
import Events.KeyDownEvent;
import Graphics.sSkinFactory;
import Level.sLevel;
import java.util.Hashtable;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class Game {
    
    private iGameMode mGameMode;
    public Game() throws SlickException
    {
        mGameMode = new PlayMode();
        sEvents.init();
        sEntityFactory.init();
        sSkinFactory.init();
        sPhysics.init();
        sLevel.init();
        Hashtable parameters = new Hashtable();
        parameters.put("position",new Vec2(2,3));
        sEntityFactory.create("Player",parameters);
    }
    public void update(GameContainer _container, int _delta)
    {
        float time = _delta;
        Input input = _container.getInput();
        if (input.isKeyDown(Input.KEY_W))
        {
            sEvents.triggerEvent(new KeyDownEvent('w'));
        }
        if (input.isKeyDown(Input.KEY_A))
        {
            sEvents.triggerEvent(new KeyDownEvent('a'));
        }
        if (input.isKeyDown(Input.KEY_S))
        {
            sEvents.triggerEvent(new KeyDownEvent('s'));
        }
        if (input.isKeyDown(Input.KEY_D))
        {
            sEvents.triggerEvent(new KeyDownEvent('d'));
        }
        mGameMode.update(time);
    }
    public void render(GameContainer _container)
    {
        mGameMode.render();
    }
}
