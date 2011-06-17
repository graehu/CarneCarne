/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.MapClickReleaseEvent;
import Entities.sEntityFactory;
import Events.KeyDownEvent;
import Events.MapClickEvent;
import Events.MouseMoveEvent;
import Events.sEvents;
import GUI.BasicTWLGameState;
import GUI.RootPane;
import Graphics.sSkinFactory;
import Level.sLevel;
import World.sWorld;
import de.matthiasmann.twl.Button;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author a203945
 */
public class StateGame extends BasicTWLGameState {

    private iGameMode mGameMode; 
    private int mTime = 0; //counter for calculating delta time
    private Button btn; //for testing
    
    public int getID() {
        return 2;
    }
    
    @Override
    public void keyPressed(int _key, char _c)
    {
    }
    
    @Override 
    public void mousePressed(int _button, int _x, int _y)
    {
        if (_button == Input.MOUSE_LEFT_BUTTON)
        {
            Vec2 position = new Vec2(_x,_y);
            sEvents.triggerEvent(new MapClickEvent(sWorld.translateToPhysics(position),true));
        }
        if (_button == Input.MOUSE_RIGHT_BUTTON)
        {
            Vec2 position = new Vec2(_x,_y);
            sEvents.triggerEvent(new MapClickEvent(sWorld.translateToPhysics(position),false));
        }
    }
    public void mouseReleased(int _button, int _x, int _y)
    {
        if (_button == Input.MOUSE_LEFT_BUTTON)
        {
            Vec2 position = new Vec2(_x,_y);
            sEvents.triggerEvent(new MapClickReleaseEvent(sWorld.translateToPhysics(position),true));
        }
        if (_button == Input.MOUSE_RIGHT_BUTTON)
        {
            Vec2 position = new Vec2(_x,_y);
            sEvents.triggerEvent(new MapClickReleaseEvent(sWorld.translateToPhysics(position),false));
        }
    }
    public void mouseMoved(int oldx, int oldy, int newx, int newy)
    {
        sEvents.triggerEvent(new MouseMoveEvent(new Vec2(newx,newy)));
    }
    
    
    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException 
    {       
        //movement input is handled here as we need to detect 'isDown' every frame rather than 'pressed' as an event
        if(_gc.getInput().isKeyDown(Input.KEY_W))
            sEvents.triggerEvent(new KeyDownEvent('w'));
        if(_gc.getInput().isKeyDown(Input.KEY_A))
            sEvents.triggerEvent(new KeyDownEvent('a'));
        if(_gc.getInput().isKeyDown(Input.KEY_S))
            sEvents.triggerEvent(new KeyDownEvent('s'));
        if(_gc.getInput().isKeyDown(Input.KEY_D))
            sEvents.triggerEvent(new KeyDownEvent('d'));
        
        int delta = (int) (_gc.getTime() - mTime);
        mTime = (int) _gc.getTime();
        mGameMode.update(delta);
    }
    
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        mGameMode.render();
    }

    @Override
    //callback for when the game enters this state
    public void enter(GameContainer container, StateBasedGame game) throws SlickException 
    {         
        super.enter(container, game);
    }
    
    @Override
    //callback for when the game leaves this state
    public void leave(GameContainer container, StateBasedGame game) throws SlickException 
    {
        super.leave(container, game);
    }
    
    @Override
    protected RootPane createRootPane() {
        // don't call getRootPane() in this method !!
        RootPane myRootPane = super.createRootPane();
        
        // specify a theme name instead of the default "state"+getID()
        myRootPane.setTheme("mainMenu");

        // create and add our button, the position and size is done in layoutRootPane()
        btn = new Button("Hello World");
        btn.addCallback(new Runnable() {
            public void run() {
                System.out.println("It works!");
            }
        });
        myRootPane.add(btn);

        // return the root pane so that it is available for getRootPane()
        return myRootPane;
    }

    @Override
    //this is where we can layout the elements of the UI
    protected void layoutRootPane() {
        btn.adjustSize();   // size the button according to it's preferred size
        btn.setPosition(100, 100);
    } 
    
    public void init(GameContainer _gc, StateBasedGame _sbg) throws SlickException {
        _gc.getInput().enableKeyRepeat();
        boolean temp = _gc.getInput().isKeyRepeatEnabled();
        
        createRootPane();
        mGameMode = new PlayMode();
        sEvents.init();
        sEntityFactory.init();
        sSkinFactory.init();
        sWorld.init();
        sLevel.init();
        HashMap parameters = new HashMap();
        parameters.put("position",new Vec2(0,0));
        sEntityFactory.create("Player",parameters);
    }    
}
