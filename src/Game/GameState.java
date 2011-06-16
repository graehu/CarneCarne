/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Entities.sEntityFactory;
import Events.KeyDownEvent;
import Events.MapClickEvent;
import Events.sEvents;
import GUI.BasicTWLGameState;
import GUI.RootPane;
import Graphics.sSkinFactory;
import Level.sLevel;
import Physics.sPhysics;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.GUI;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.InputAdapter;

/**
 *
 * @author a203945
 */
public class GameState extends BasicTWLGameState {

    private Button btn;
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException 
    {         
        super.enter(container, game);
    }
    public void leave(GameContainer container, StateBasedGame game) throws SlickException 
    {
        super.leave(container, game);
    }
    @Override
    protected RootPane createRootPane() {
        // don't call getRootPane() in this method !!
        RootPane rootPane = super.createRootPane();
        
        // specify a theme name instead of the default "state"+getID()
        rootPane.setTheme("mainMenu");

        // create and add our button, the position and size is done in layoutRootPane()
        btn = new Button("Hello World");
        btn.addCallback(new Runnable() {
            public void run() {
                System.out.println("It works!");
            }
        });
        rootPane.add(btn);

        // return the root pane so that it is available for getRootPane()
        return rootPane;
    }

    @Override
    protected void layoutRootPane() {
        btn.adjustSize();   // size the button according to it's preferred size
        btn.setPosition(100, 100);
    }
    private iGameMode mGameMode;  
    
    @Override
    public int getID() {
        return 0;
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        createRootPane();
        mGameMode = new PlayMode();
        sEvents.init();
        sEntityFactory.init();
        sSkinFactory.init();
        sPhysics.init();
        sLevel.init();
        HashMap parameters = new HashMap();
        parameters.put("position",new Vec2(0,0));
        sEntityFactory.create("Player",parameters);
    }

    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        mGameMode.render();
    }
    
    @Override
    public void keyPressed(int _key, char _c)
    {
        sEvents.triggerEvent(new KeyDownEvent(_c));
    }
    
    @Override 
    public void mousePressed(int _button, int _x, int _y)
    {
        if (_button == Input.MOUSE_LEFT_BUTTON)
        {
            Vec2 position = new Vec2(_x,_y);
            sEvents.triggerEvent(new MapClickEvent(sPhysics.translateToPhysics(position),true));
        }
        if (_button == Input.MOUSE_RIGHT_BUTTON)
        {
            Vec2 position = new Vec2(_x,_y);
            sEvents.triggerEvent(new MapClickEvent(sPhysics.translateToPhysics(position),false));
        }
    }
    int mTime = 0;
    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException 
    {       
       
        int delta = (int) (_gc.getTime() - mTime);
        mTime = (int) _gc.getTime();
        mGameMode.update(delta);
    }

    
}
