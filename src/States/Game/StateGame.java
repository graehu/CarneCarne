/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.ShoulderButtonEvent;
import Events.AnalogueStickEvent;
import Events.MapClickReleaseEvent;
import Entities.sEntityFactory;
import Events.KeyDownEvent;
import Events.MapClickEvent;
import Events.MouseMoveEvent;
import Events.sEvents;
import GUI.sGUI;
import Graphics.Particles.sParticleManager;
import GUI.TWL.BasicTWLGameState;
import GUI.TWL.RootPane;
import Graphics.sGraphicsManager;
import Graphics.sSkinFactory;
import Level.sLevel;
import Sound.sSound;
import World.sWorld;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ScrollPane;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.BlobbyTransition;

/**
 *
 * @author a203945
 */
public class StateGame extends BasicTWLGameState {

    private iGameMode mGameMode; 
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
    
    /* XBox Controls
     * 0 - A
     * 5 - R1
     * 6 - Select
     * 8 - L3
     * 9 - R3
     */
    static private float stickEpsilon = 0.1f;
    static private float shoulderButtonEpsilon = 0.1f;
    static private boolean xAxisHit = false;
    static private boolean yAxisHit = false;
    static private boolean zAxisHit = false;
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
        
        if(_gc.getInput().isButtonPressed(5, 0))
            sEvents.triggerEvent(new KeyDownEvent('w'));
        float xAxis = _gc.getInput().getAxisValue(0, 1);
        if (xAxisHit)
        {
            if (xAxis > stickEpsilon || xAxis < -stickEpsilon)
            {
                sEvents.triggerEvent(new AnalogueStickEvent(xAxis));
            }
        }
        else if (xAxis != -1.0f)
        {
            xAxisHit = true;
        }
        Vec2 rightStick = new Vec2(_gc.getInput().getAxisValue(0, 3),_gc.getInput().getAxisValue(0, 2));
        
        float shoulderButtons = _gc.getInput().getAxisValue(0,4);
        if (zAxisHit)
        {
            if (shoulderButtons > shoulderButtonEpsilon)
            {
                sEvents.triggerEvent(new ShoulderButtonEvent(rightStick,true));
            }
            else if (shoulderButtons < -shoulderButtonEpsilon)
            {
                sEvents.triggerEvent(new ShoulderButtonEvent(rightStick,false));
            }
        }
        else if (shoulderButtons != -1.0f)
        {
            zAxisHit = true;
        }
        
        if (rightStick.length() > 0.2f)
        {
            sEvents.triggerEvent(new RightStickEvent(rightStick));
        }
        mGameMode.update(_i);
        //update particles
        sParticleManager.update(_i);
    }
    
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException
    {
        Vec2 s = sGraphicsManager.getScreenDimentions();
        ShapeFill fill = new GradientFill(new Vector2f(0,0), new Color(159,111,89), new Vector2f(s.x,s.y), new Color(186, 160, 149), false);
        Rectangle shape = new Rectangle(0,0, s.x, s.y);
        _gc.getGraphics().fill(shape, fill);
        mGameMode.render();
        
        //render particles
        Vec2 worldTrans = sWorld.translateToWorld(new Vec2(0,0));
        sParticleManager.render((int)worldTrans.x, (int)worldTrans.y, (int)s.x, (int)s.y, 0);
    }

    @Override
    //callback for when the game enters this state
    public void enter(GameContainer container, StateBasedGame game) throws SlickException 
    {         
        super.enter(container, game);
        //TEST: PARTICLE SYSTEM
        sParticleManager.createSystem("particleSystems/testSystem.xml", 500, 500, 3.0f);
        sSound.play("ambiance");
        // create and add our button, the position and size is done in layoutRootPane()
        btn = new Button("Hello World");
        btn.addCallback(new Runnable() {
            public void run() {
                //StateGame.mgame.enterState(3,new BlobbyTransition(Color.green),null);
            }
        });
        
        scrollPane = sGUI.createDialogueBox(getRootPane(), 20, 20, "RAWR!");
        scrollPane.setContent(btn);
        
        //container.getGraphics().setDrawMode(Graphics.MODE_ALPHA_BLEND);
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
        
        
               
        // return the root pane so that it is available for getRootPane()
        return myRootPane;
    }
ScrollPane scrollPane;
    @Override
    //this is where we can layout the elements of the UI
    protected void layoutRootPane() {
        btn.adjustSize();   // size the button according to it's preferred size
        btn.setPosition(100, 100);
       
        scrollPane.setPosition(0, 0);
            scrollPane.setSize(50, 50);
            scrollPane.setEnabled(true);
            scrollPane.setVisible(true);
        
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
        sSound.init();
        sSound.loadSound("ambiance", "data/assets/sound/sfx/level_ambiance.ogg");
        sSound.setLooping("ambiance", true);
    }    
}
