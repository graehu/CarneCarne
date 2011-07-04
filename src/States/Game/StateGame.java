/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Entities.sEntityFactory;
import Events.KeyDownEvent;
import Events.PlayerCreatedEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Particles.ParticleSys;
import Graphics.Particles.sParticleManager;
import Graphics.sGraphicsManager;
import Graphics.Skins.sSkinFactory;
import Graphics.Sprites.sSpriteFactory;
import Input.sInput;
import Level.sLevel;
import States.StateChanger;
import Sound.sSound;
import World.sWorld;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.BlobbyTransition;

/**
 *
 * @author a203945
 */
public class StateGame extends BasicGameState implements iEventListener {

    public enum GameType
    {
        eRaceGame,
        eGameTypesMax
    }
    private GameType mGameType;
    GameType getGameType()
    {
        return mGameType;
    }
    private iGameMode mGameMode; 
    StateChanger mChangeToMenu;
    static private int mPlayers;
    static public Vec2 mMousePos = new Vec2(0,0);
    
    public StateGame()
    {
    }
    public void trigger(iEvent _event) {
        //on player creation subscribe to their input
        if(_event.getType().equals("PlayerCreatedEvent"))
        {
            PlayerCreatedEvent event = (PlayerCreatedEvent) _event;
            sEvents.subscribeToEvent("KeyDownEvent"+"Q"+event.getPlayerID(), this);
        }
        if(_event.getType().equals("KeyDownEvent"))
        {
            KeyDownEvent event = (KeyDownEvent) _event;
            if(event.getKey() == 'Q')
            {
                //goto menu
                mChangeToMenu.run();
            }
        }
    }
    
    public int getID()
    {
        return 3;
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _delta) throws SlickException 
    {
        sInput.update(_delta);
        mGameMode.update(_delta);
        //update particles
        sParticleManager.update(_delta);
    }
    
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs)
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        mGameMode.render(_gc.getGraphics());
        _grphcs.drawString("fucking work god damnit", 500, 500);
    }
    @Override
    //callback for when the game enters this state
    public void enter(GameContainer container, StateBasedGame game) throws SlickException 
    {         
        super.enter(container, game);
        //TEST: MANAGED SPRITE
//        HashMap params = new HashMap();
//        params.put("img", new Image("data/assets/splashbig.png"));
//        params.put("pos", new Vec2(500,500));
//        sSpriteFactory.create("simple", params);

        sSound.play("ambiance");
    }
    
    @Override
    //callback for when the game leaves this state
    public void leave(GameContainer container, StateBasedGame game) throws SlickException 
    {
        super.leave(container, game);
    }
    
//    @Override
//    protected RootPane createRootPane() {
//        // don't call getRootPane() in this method !!
//        RootPane myRootPane = super.createRootPane();
//        
//        // specify a theme name instead of the default "state"+getID()
//        myRootPane.setTheme("mainMenu");
//    
//        // return the root pane so that it is available for getRootPane()
//        return myRootPane;
//    }
//
//    @Override
//    //this is where we can layout the elements of the UI
//    protected void layoutRootPane() {        
//    } 
    
    public void init(GameContainer _gc, StateBasedGame _sbg) throws SlickException
    {
        //createRootPane();
        mGameType = GameType.eRaceGame;
        mGameMode = new RaceMode();
        
        //subscribe to events (must be done before further initialisation)
        sEvents.subscribeToEvent("PlayerCreatedEvent", this);
        
        sEntityFactory.init();
        sSkinFactory.init();
        sSpriteFactory.init();
        sWorld.init();
        sLevel.init();
        
        //create state changers
        mChangeToMenu = new StateChanger(4, new BlobbyTransition(), new BlobbyTransition(), _sbg);
        
        //Initialise sound
        sSound.init();
        sSound.loadSound("ambiance", "assets/sound/sfx/level_ambiance.ogg");
        sSound.setLooping("ambiance", true);

    }

}
