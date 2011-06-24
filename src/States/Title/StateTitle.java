/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Title;

import States.Init.OptionsGUI;
import GUI.TWL.BasicTWLGameState;
import de.matthiasmann.twl.ComboBox;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.model.BooleanModel;
import de.matthiasmann.twl.model.ListModel;
import de.matthiasmann.twl.model.SimpleBooleanModel;
import de.matthiasmann.twl.model.SimpleChangableListModel;
import java.util.ArrayList;
import java.util.Arrays;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author a203945
 */
public class StateTitle extends BasicTWLGameState{

    @Override
    public int getID() {
       return 2;
    }
    BooleanModel mFullScreen = new SimpleBooleanModel();
    public void init(final GameContainer _gc, final StateBasedGame _sbg) throws SlickException {
        createRootPane();
        // create and add our button, the position and size is done in layoutRootPane()
        ToggleButton fullScreenToggle = new ToggleButton(mFullScreen);
        fullScreenToggle.setPosition(500,500);
        fullScreenToggle.adjustSize();
        getRootPane().add(fullScreenToggle);
       
        
        
        
        
    }
    
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException {
        //this is the only place that the resolution should change so only change it in sGraphicsManager here
    }
    
}
