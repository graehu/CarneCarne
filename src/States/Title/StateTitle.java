/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Title;

import GUI.Components.Button;
import GUI.Components.GraphicalComponent;
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
    iComponent comp, bob, joe;
    public void init(final GameContainer _gc, final StateBasedGame _sbg) throws SlickException {
        comp = new Button(_gc, new Vector2f(200,200), new Vector2f(200, 200));
        bob = new Button(_gc, new Vector2f(50,50), new Vector2f(50, 50));
        joe = new Text(_gc, "BUGGER!", new Vector2f(10,10));
        
        comp.addChild(bob);
        bob.addChild(joe);
        
        ((GraphicalComponent)comp).setImage("assets/pea.png");
        ((GraphicalComponent)bob).setImage("assets/pea.png");
        ((Text)joe).setFont(sFontLoader.createFont("ambulance_shotgun", 40));
    }
    
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        comp.render(_gc, _grphcs, true);
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException {
        comp.update(_i);
        comp.setLocalRotation(comp.getLocalRotation()+0.5f);
        bob.setLocalRotation(bob.getLocalRotation()+0.5f);
        joe.setLocalRotation(joe.getLocalRotation()+0.5f);
    }
    
}
