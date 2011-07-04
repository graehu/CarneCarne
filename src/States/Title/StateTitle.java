/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Title;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author a203945
 */
public class StateTitle extends BasicGameState{
    MouseOverArea button;
   // @Override
    public int getID() {
       return 2;
    }
    public void init(final GameContainer _gc, final StateBasedGame _sbg) throws SlickException {
        Image buttonImg = new Image("assets/GumSpit.png");
        button = new MouseOverArea(_gc, buttonImg, 0, 0);
        button.setMouseOverColor(Color.yellow);
        button.setMouseDownColor(Color.green);
    }
    
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        button.render(_gc, _grphcs);
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException {
    }
    
}
