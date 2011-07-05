/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Title;

import GUI.Components.SelectableComponent;
import GUI.Components.iComponent;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.MouseOverArea;
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
    iComponent comp;
    public void init(final GameContainer _gc, final StateBasedGame _sbg) throws SlickException {
        comp = new SelectableComponent(_gc, null, new Vector2f(200,200), new Vector2f(200, 200));
        comp.addChild(new SelectableComponent(_gc, null, new Vector2f(50,50), new Vector2f(50, 50)));
        //comp.setLocalRotation(60);
    }
    
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        comp.render(_gc, _grphcs);
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException {
        comp.setLocalRotation(comp.getLocalRotation()+0.5f);
    }
    
}
