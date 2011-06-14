/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import org.newdawn.slick.*;
import Physics.sPhysics;
import Events.sEvents;
import Entities.sEntityFactory;
import Graphics.sSkinFactory;
import Level.sLevel;
import java.util.HashMap;
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
        HashMap parameters = new HashMap();
        parameters.put("position",new Vec2(50,0));
        sEntityFactory.create("Player",parameters);
    }
    public void update(GameContainer _container, int _delta)
    {
        float time = _delta;
        mGameMode.update(time);
    }
    public void render(GameContainer _container)
    {
        mGameMode.render();
    }
}
