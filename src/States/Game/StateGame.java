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
import GUI.sGUI;
import Graphics.Particles.sParticleManager;
import GUI.TWL.BasicTWLGameState;
import GUI.TWL.RootPane;
import Graphics.sGraphicsManager;
import Graphics.Skins.sSkinFactory;
import Graphics.Sprites.sSpriteFactory;
import Level.sLevel;
import States.FullScreenChanger;
import States.StateChanger;
import Sound.sSound;
import World.sWorld;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ScrollPane;
import java.util.ArrayList;
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
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.BlobbyTransition;

/**
 *
 * @author a203945
 */
public class StateGame extends BasicTWLGameState {

    private iGameMode mGameMode; 
    private Button btn,btn2; // FIXME for testing
    static private int mPlayers;
    
    public int getID() {
        return 3;
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
            for (int i = 0; i < mPlayers; i++)
                sEvents.triggerEvent(new MapClickEvent(sWorld.translateToPhysics(position),true,i));
        }
        if (_button == Input.MOUSE_RIGHT_BUTTON)
        {
            Vec2 position = new Vec2(_x,_y);
            for (int i = 0; i < mPlayers; i++)
                sEvents.triggerEvent(new MapClickEvent(sWorld.translateToPhysics(position),false,i));
        }
    }
    public void mouseReleased(int _button, int _x, int _y)
    {
        if (_button == Input.MOUSE_LEFT_BUTTON)
        {
            Vec2 position = new Vec2(_x,_y);
            for (int i = 0; i < mPlayers; i++)
                sEvents.triggerEvent(new MapClickReleaseEvent(sWorld.translateToPhysics(position),true, i));
        }
        if (_button == Input.MOUSE_RIGHT_BUTTON)
        {
            Vec2 position = new Vec2(_x,_y);
            for (int i = 0; i < mPlayers; i++)
                sEvents.triggerEvent(new MapClickReleaseEvent(sWorld.translateToPhysics(position),false, i));
        }
    }
    public void mouseMoved(int oldx, int oldy, int newx, int newy)
    {
        for (int i = 0; i < mPlayers; i++)
            sEvents.triggerEvent(new MouseMoveEvent(new Vec2(newx,newy), i));
    }
    private ArrayList<XBoxController> xBoxControllers = new ArrayList<XBoxController>();
    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException /// FIXME wtf is '_i'?
    {
        mPlayers = _gc.getInput().getControllerCount();
        int i = 0;
        if(_gc.getInput().isKeyDown(Input.KEY_W))
            sEvents.triggerEvent(new KeyDownEvent('w', 0));
        if(_gc.getInput().isKeyDown(Input.KEY_A))
            sEvents.triggerEvent(new KeyDownEvent('a', 0));
        if(_gc.getInput().isKeyDown(Input.KEY_S))
            sEvents.triggerEvent(new KeyDownEvent('s', 0));
        if(_gc.getInput().isKeyDown(Input.KEY_D))
            sEvents.triggerEvent(new KeyDownEvent('d', 0));
        try
        {
            for (i = 0; i < xBoxControllers.size(); i++)
            {
                xBoxControllers.get(i).update(_gc.getInput());
                //updateControls(_gc.getInput(), i);
            }
            while (true)
            {
                xBoxControllers.add(new XBoxController(i));
                xBoxControllers.get(i).update(_gc.getInput());
                i++;
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            mPlayers = i;
            if (mPlayers == 0)
            {
                mPlayers = 1;
            }
        }
        mGameMode.update(_i);
        //update particles
        sParticleManager.update(_i);
    }
    
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        ShapeFill fill = new GradientFill(new Vector2f(0,0), new Color(159,111,89), new Vector2f(s.x,s.y), new Color(186, 160, 149), false);
        Rectangle shape = new Rectangle(0,0, s.x, s.y);
        _gc.getGraphics().fill(shape, fill);
        mGameMode.render(_gc.getGraphics());
        
        //render particles
        Vec2 worldTrans = sWorld.translateToWorld(new Vec2(0,0));
        sParticleManager.render((int)worldTrans.x, (int)worldTrans.y, (int)s.x, (int)s.y, 0);
        
        //render managed sprites
        sGraphicsManager.renderManagedSprites();
        
        _grphcs.drawString("AHHHHHHHHHHHHHHHHHHHHHH", 500, 500);
        
    }

    @Override
    //callback for when the game enters this state
    public void enter(GameContainer container, StateBasedGame game) throws SlickException 
    {         
        super.enter(container, game);
        //TEST: PARTICLE SYSTEM
        sParticleManager.createSystem("particleSystems/testSystem.xml", 500, 500, 3.0f);

        //TEST: MANAGED SPRITE
//        HashMap params = new HashMap();
//        params.put("img", new Image("data/assets/splashbig.png"));
//        params.put("pos", new Vec2(500,500));
//        sSpriteFactory.create("simple", params);

        sSound.play("ambiance");
        // create and add our button, the position and size is done in layoutRootPane()
        btn = new Button("Hello World");
        btn.addCallback(new Runnable() {
            public void run() {
                //StateGame.mgame.enterState(3,new BlobbyTransition(Color.green),null);
            }
        });  
        
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
        
        btn2.adjustSize();
        btn2.setPosition(400, 0);
       
        scrollPane.setPosition(0, 0);
            scrollPane.setSize(50, 50);
            scrollPane.setEnabled(true);
            scrollPane.setVisible(true);
        
    } 
    
    public void init(GameContainer _gc, StateBasedGame _sbg) throws SlickException {
        
        createRootPane();
        sGraphicsManager.setGraphics(_gc.getGraphics());
        mGameMode = new PlayMode();
        sEvents.init();
        sEntityFactory.init();
        sSkinFactory.init();
        sSpriteFactory.init();
        sWorld.init();
        Vec2 s = sGraphicsManager.getTrueScreenDimensions();
        sWorld.resizeViewport(new Rectangle(0,0,s.x, s.y));
        sLevel.init();
        
        //FIXME TEST: GUI!!!
        btn = new Button("Hello World");
        btn.addCallback(new StateChanger(3, new BlobbyTransition(Color.green), null, _sbg));
        btn2 = new Button("FullScreen");
        btn2.addCallback(new FullScreenChanger(_sbg));
        
        getRootPane().add(btn2);
        
        scrollPane = sGUI.createDialogueBox(getRootPane(), 20, 20, "RAWR!");
        scrollPane.setContent(btn);
        
        //Initialise sound
        sSound.init();
        sSound.loadSound("ambiance", "assets/sound/sfx/level_ambiance.ogg");
        sSound.setLooping("ambiance", true);

    }    
}
