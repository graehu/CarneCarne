/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Title;

import GUI.Components.Button;
import GUI.Components.DraggableComponent;
import GUI.Components.GraphicalComponent;
import GUI.Components.SlideBar;
import GUI.Components.Text;
import GUI.Components.iComponent;
import Utils.sFontLoader;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author a203945
 */
public class StateTitle extends BasicGameState{
   // @Override
    public int getID() {
       return 2;
    }
    iComponent comp, bob, joe, jon;
    public void init(final GameContainer _gc, final StateBasedGame _sbg) throws SlickException {
        comp = new SlideBar(_gc, new Vector2f(200,200), new Vector2f(200, 50), new Vector2f(32, 32));
        
        ((SlideBar)comp).init(false, 32);
        
        ((SlideBar)comp).setImage("assets/pea.png", "assets/pea.png");
    }
    
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        comp.render(_gc, _grphcs, true);
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException {
        comp.update(_i);
    }
    
}
