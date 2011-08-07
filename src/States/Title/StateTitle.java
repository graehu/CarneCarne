
/* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Title;

import GUI.Components.Button;
import GUI.Components.Effects.AnimatedEffect;
import GUI.Components.GraphicalComponent;
import GUI.Components.ScrollableComponent;
import GUI.Components.TextField;
import GUI.Components.ToggleButton;
import GUI.GUIManager;
import Graphics.sGraphicsManager;
import Input.sInput;
import Sound.sSound;
import States.Game.StateGame;
import Utils.sFontLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.BlobbyTransition;

/**
 *
 * @author Aaron
 */
public class StateTitle extends BasicGameState 
{
   // @Override
    public int getID() {
       return 2;
    }
    
    enum MenuState
    {
        eCenter,
        eLeft,
        eRight
    }

    GameContainer cont = null;
    boolean inited = false;
    Integer mGUIManagerGlobal = null;
    Integer mCurrentGUIState = null;
    Integer mCurrentLeftGUIState = null;
    Integer mGUIManagerCenterMain = null;
    Integer mGUIManagerLeftAdventure = null;
    Integer mGUIManagerLeftRace = null;
    Integer mGUIManagerLeftFootball = null;
    Integer mGUIManagerLeftOptions = null;
    Integer mGUIManagerRight = null;
    
    
    @Override
    public void enter(final GameContainer _gc, final StateBasedGame _sbg) throws SlickException 
    {
        cont = _gc;
        if(!inited) //we do this here so if the state is not used it is not inited
        {
            inited = true;
            mGUIManagerGlobal = GUIManager.create(_gc);
            mGUIManagerCenterMain = GUIManager.create(_gc);
            mGUIManagerLeftAdventure = GUIManager.create(_gc);
            mGUIManagerLeftRace = GUIManager.create(_gc);
            mGUIManagerLeftFootball = GUIManager.create(_gc);
            mGUIManagerLeftOptions = GUIManager.create(_gc);
            mGUIManagerRight = GUIManager.create(_gc);
            mCurrentGUIState = mGUIManagerCenterMain;
            mCurrentLeftGUIState = mGUIManagerLeftOptions;

            //initalise music
            sSound.loadFile("menu1", "assets/music/Menu1.ogg");

            //initialise fonts
            mUIFont = sFontLoader.createFont("manastirka",72);    
            mUIFont.setPaddingAdvanceX(-2);
            mInputFont = sFontLoader.createFont("BIRTH OF A HERO", 72, false, true); 

            //initialise background & foreground
            mBackground = new GraphicalComponent(_gc);
            mBackground.setImage("ui/title/bg.png");
            mBackground.setDimentionsToImage();
            mForeground = new GraphicalComponent(_gc);
            mForeground.setImage("ui/title/fg.png");
            mForeground.setDimentionsToImage();

            //calculate scale relative to background and screen size
            Vec2 screen = sGraphicsManager.getTrueScreenDimensions();
            mScale = screen.x/mBackground.getWidth();

            //initialise logo
            mLogo = new GraphicalComponent(_gc, new Vector2f(1690.5f,-470f), new Vector2f());
            mLogo.setImage("ui/title/logo.png");
            mLogo.setDimentionsToImage();

            //initialise animations
            mCarrotAnimation = new GraphicalComponent(_gc, new Vector2f(1200,10), new Vector2f(170, 140));
            mCarrotAnimation.mEffectLayer.pushEffect(new AnimatedEffect("ui/title/car_ss.png", 170, 140, 41));
            mBrocAnimation = new GraphicalComponent(_gc, new Vector2f(3453-60,10), new Vector2f(23, 39));
            mBrocAnimation.mEffectLayer.pushEffect(new AnimatedEffect("ui/title/broc_ss.png", 23, 39, 41));

            //initalise paralax
            Vector2f paralaxDim = new Vector2f(screen.x/mScale,400);
            Vector2f paralaxPos = new Vector2f(0,0);
            //FIXME: want this to be a child to root, need to implement scaling compoents in heirarchy
            mParalax0 = new ScrollableComponent(_gc, paralaxPos, paralaxDim);
            mParalax0.setImage("ui/title/p0.png");
            mParalax1 = new ScrollableComponent(_gc, paralaxPos, paralaxDim);
            mParalax1.setImage("ui/title/p1.png");
            mParalax2 = new ScrollableComponent(_gc, paralaxPos, paralaxDim);
            mParalax2.setImage("ui/title/p2.png");

            //add carrot and broc animations to the middle paralax layer
            mParalax1.addChild(mCarrotAnimation);
            mParalax1.addChild(mBrocAnimation);
            mParalax0.addChild(mLogo);

            //initialise buttons
            Vector2f buttonDim = new Vector2f(200/mScale,50/mScale);
            mNameField = new TextField(_gc, mInputFont, 0, 0, (int)buttonDim.x, (int)buttonDim.y);
            mNameField.setMaxLength(15);
            mNameField.setDefaultText("Name");
            mNameField.setTextColor(new Color(0,0,0));
            mNameField.setSelectedTextColor(new Color(187,139,44));
            mAdventureButton = new Button(_gc, new Vector2f(), buttonDim);
            mAdventureButton.addText(_gc, mUIFont, "AdvEnturE MOdE", true); //uppercase 'e' 's' because lowercase is crap
            mRaceButton = new Button(_gc, new Vector2f(), buttonDim);
            mRaceButton.addText(_gc, mUIFont, "RacE MOdE", true);
            mFootballButton = new Button(_gc, new Vector2f(), buttonDim);
            mFootballButton.addText(_gc, mUIFont, "FOOtball MOdE", true); // lowercase 'o' is crap
            mOptionsButton = new Button(_gc, new Vector2f(), buttonDim);
            mOptionsButton.addText(_gc, mUIFont, "OptiOnS", true); 
            mHighScoresButton = new Button(_gc, new Vector2f(), buttonDim);
            mHighScoresButton.addText(_gc, mUIFont, "HighScorES", true);
            
            mParticlesToggle = new ToggleButton(_gc, new Vector2f(200,-200), buttonDim, true);
            mParticlesToggle.addText(_gc, mUIFont, "ParticlES On", true);
            mParticlesToggle.setToggleText("ParticlES On", "ParticlES Off");
            
            mLightingToggle = new ToggleButton(_gc, new Vector2f(200,-125), buttonDim, true);
            mLightingToggle.addText(_gc, mUIFont, "Lighting On", true);
            mLightingToggle.setToggleText("Lighting On", "Lighting Off");
            
            mDone = new Button(_gc, new Vector2f(200,-50), buttonDim);
            mDone.addText(_gc, mUIFont, "dOnE", true); //uppercase 'D' is crap
            
            mParalax0.addChild(mParticlesToggle);
            mParalax0.addChild(mLightingToggle);
            mParalax0.addChild(mDone);
            
            //add to GUIManager in render order
            GUIManager.use(mGUIManagerGlobal).addRootComponent(mBackground);
            
            GUIManager.use(mGUIManagerGlobal).addRootComponent(mParalax2);
            GUIManager.use(mGUIManagerGlobal).addRootComponent(mParalax1);
            GUIManager.use(mGUIManagerGlobal).addRootComponent(mParalax0);
            
            GUIManager.use(mGUIManagerCenterMain).addRootComponent(mNameField);
            GUIManager.use(mGUIManagerCenterMain).addRootComponent(mAdventureButton);
            GUIManager.use(mGUIManagerCenterMain).addRootComponent(mRaceButton);
            GUIManager.use(mGUIManagerCenterMain).addRootComponent(mFootballButton);
            GUIManager.use(mGUIManagerCenterMain).addRootComponent(mOptionsButton);
            GUIManager.use(mGUIManagerCenterMain).addRootComponent(mHighScoresButton);
            
            GUIManager.use(mGUIManagerGlobal).addRootComponent(mForeground);
            
            GUIManager.use(mGUIManagerLeftOptions).addSelectable(mParticlesToggle);
            GUIManager.use(mGUIManagerLeftOptions).addSelectable(mLightingToggle);
            GUIManager.use(mGUIManagerLeftOptions).addSelectable(mDone);
            
            GUIManager.use(mGUIManagerLeftAdventure).addSelectable(mDone);
            GUIManager.use(mGUIManagerLeftRace).addSelectable(mDone);
            GUIManager.use(mGUIManagerLeftFootball).addSelectable(mDone);
            
            calcUI();

            mAdventureButton.setCallback(new Runnable() {
                public void run() {
                    mState = MenuState.eLeft;
                    for(Button b : GUIManager.use(mCurrentLeftGUIState).getSelectableList())
                    {
                        b.setIsVisible(false);
                        b.setAcceptingInput(false);
                    }
                    for(Button b : GUIManager.use(mGUIManagerLeftAdventure).getSelectableList())
                    {
                        b.setIsVisible(true);
                        b.setAcceptingInput(true);
                    }
                    mCurrentLeftGUIState = mGUIManagerLeftAdventure;
                }
            });
            mRaceButton.setCallback(new Runnable() {
                public void run() {
                    mState = MenuState.eLeft;
                    for(Button b : GUIManager.use(mCurrentLeftGUIState).getSelectableList())
                    {
                        b.setIsVisible(false);
                        b.setAcceptingInput(false);
                    }
                    for(Button b : GUIManager.use(mGUIManagerLeftRace).getSelectableList())
                    {
                        b.setIsVisible(true);
                        b.setAcceptingInput(true);
                    }
                    mCurrentLeftGUIState = mGUIManagerLeftRace;
                }
            });
            mFootballButton.setCallback(new Runnable() {
                public void run() {
                    mState = MenuState.eLeft;
                    for(Button b : GUIManager.use(mCurrentLeftGUIState).getSelectableList())
                    {
                        b.setIsVisible(false);
                        b.setAcceptingInput(false);
                    }
                    for(Button b : GUIManager.use(mGUIManagerLeftFootball).getSelectableList())
                    {
                        b.setIsVisible(true);
                        b.setAcceptingInput(true);
                    }
                    mCurrentLeftGUIState = mGUIManagerLeftFootball;
                }
            });
            mOptionsButton.setCallback(new Runnable() {
                public void run() {
                    mState = MenuState.eLeft;
                    for(Button b : GUIManager.use(mCurrentLeftGUIState).getSelectableList())
                    {
                        b.setIsVisible(false);
                        b.setAcceptingInput(false);
                    }
                    for(Button b : GUIManager.use(mGUIManagerLeftOptions).getSelectableList())
                    {
                        b.setIsVisible(true);
                        b.setAcceptingInput(true);
                    }
                    mCurrentLeftGUIState = mGUIManagerLeftOptions;
                }
            });
            mHighScoresButton.setCallback(new Runnable() {
                public void run() {
                    //mState = MenuState.eRight; //FIXME
                }
            });
            
            mParticlesToggle.setOnCallback(new Runnable() { public void run() {
                    sGraphicsManager.setAllowParticles(true);}});
            mParticlesToggle.setOffCallback(new Runnable() { public void run() {
                    sGraphicsManager.setAllowParticles(false);}});
            
            mLightingToggle.setOnCallback(new Runnable() { public void run() {
                    sGraphicsManager.setAllowShaders(true);}});
            mLightingToggle.setOffCallback(new Runnable() { public void run() {
                    sGraphicsManager.setAllowShaders(false);}});
            
            mDone.setCallback(new Runnable() {
                public void run() {
                    mState = MenuState.eCenter;
                }
            });
            
            initLevelLists(_sbg);
        }
        GUIManager.use(mCurrentGUIState).setAcceptingInput(true);
        //container.setMouseCursor("ui/title/mouse.png", 0, 62); //FIXME: break in fullscreen
        sSound.playAsMusic("menu1", true);
        super.enter(_gc, _sbg);
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        container.setDefaultMouseCursor();
        sSound.stop("menu1");
        GUIManager.use(mCurrentGUIState).setAcceptingInput(false);
    }
    
    
    GraphicalComponent mBackground = null, mForeground = null;
    GraphicalComponent mLogo = null;
    ScrollableComponent mParalax0 = null;
    ScrollableComponent mParalax1 = null;
    ScrollableComponent mParalax2 = null;
    GraphicalComponent mCarrotAnimation = null;
    GraphicalComponent mBrocAnimation = null;
    TextField mNameField = null;
    Button mAdventureButton = null;
    Button mRaceButton = null;
    Button mFootballButton = null;
    Button mOptionsButton = null;
    Button mHighScoresButton = null;
    ToggleButton mParticlesToggle = null;
    ToggleButton mLightingToggle = null;
    ToggleButton mFullScreenButton = null;
    Button mDone = null;
    float mScale = 1.0f;
    float mOffset = 0.0f;
    MenuState mState = MenuState.eCenter;
    UnicodeFont mUIFont = null;
    Font mInputFont = null;
    
    ArrayList<Button> mAdventureLevels = new ArrayList<Button>();
    ArrayList<Button> mRaceLevels = new ArrayList<Button>();
    ArrayList<Button> mFootballLevels = new ArrayList<Button>();

    public void init(final GameContainer _gc, final StateBasedGame _sbg) throws SlickException {
        
        
    }
    
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        if(mGUIManagerGlobal != null)
            GUIManager.use(mGUIManagerGlobal).render(false);
        if(mGUIManagerCenterMain != null)
            GUIManager.use(mGUIManagerCenterMain).render(false);
        if(mGUIManagerRight != null)
            GUIManager.use(mGUIManagerRight).render(false);
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException 
    {
        sSound.poll(_i);
        sInput.update(_i);
        Input input = _gc.getInput();
        if(input.isKeyDown(Input.KEY_F11))
            sGraphicsManager.toggleFullscreen();  
        
        updateMenuState();
        
        if(mGUIManagerGlobal != null)
        {
            GUIManager.use(mGUIManagerGlobal).update(_i);
        }
        if(mCurrentGUIState != null)
        {
            GUIManager.use(mCurrentGUIState).update(_i);
        }
    }
    private void changeGUIState(int _newState)
    {
        if(mCurrentGUIState == _newState)
            return;
        
        GUIManager.use(mCurrentGUIState).setAcceptingInput(false);
        GUIManager.use(_newState).setAcceptingInput(true);
        mCurrentGUIState = _newState;
    }
    protected void updateMenuState()
    {
        float scrollTime = 0.5f;
        switch(mState)
        {
            case eLeft:
            {
                changeGUIState(mCurrentLeftGUIState);
                mParalax0.scrollTo(new Vector2f(0,0), new Vector2f(scrollTime, 0));
                mParalax1.scrollTo(new Vector2f(0,0), new Vector2f(scrollTime, 0));
                mParalax2.scrollTo(new Vector2f(0,0), new Vector2f(scrollTime, 0));
                break;
            }
            case eCenter:
            {
                changeGUIState(mGUIManagerCenterMain);
                mParalax0.scrollTo(new Vector2f(0.5f,0), new Vector2f(scrollTime, 0));
                mParalax1.scrollTo(new Vector2f(0.5f,0), new Vector2f(scrollTime, 0));
                mParalax2.scrollTo(new Vector2f(0.5f,0), new Vector2f(scrollTime, 0));
                break;
            }
            case eRight:
            {
                changeGUIState(mGUIManagerRight);
                mParalax0.scrollTo(new Vector2f(1f,0), new Vector2f(scrollTime, 0));
                mParalax1.scrollTo(new Vector2f(1f,0), new Vector2f(scrollTime, 0));
                mParalax2.scrollTo(new Vector2f(1f,0), new Vector2f(scrollTime, 0));
                break;
            }
        }
    }
    //calculates the positions relative to the background
    protected void calcUI()
    {
        //calc scale and offset values
        Vec2 screen = sGraphicsManager.getTrueScreenDimensions();
        //scale by x to fit but keep aspect ratio using offset
        mScale = screen.x/mBackground.getWidth();
        mOffset = (screen.y - mBackground.getHeight()*mScale) * 0.5f;
        Vector2f vOffset = new Vector2f(0, mOffset);
        
        mBackground.setLocalTranslation(vOffset);
        mForeground.setLocalTranslation(vOffset);
        
        Vector2f buttonPos = new Vector2f(mBackground.getWidth() * 0.6f, mOffset + (mBackground.getHeight() * 0.3f));
        int buttonOffset = (int)75;
        
        mNameField.setLocalTranslation(new Vector2f(buttonPos.x, buttonPos.y));
        
        mAdventureButton.setLocalTranslation(new Vector2f(buttonPos.x, buttonPos.y + buttonOffset + 10)); //constant to provide extra offset for namefield
        mAdventureButton.setDimensionsToText();
        
        mRaceButton.setLocalTranslation(new Vector2f(buttonPos.x, buttonPos.y + 2*buttonOffset));
        mRaceButton.setDimensionsToText();
        
        mFootballButton.setLocalTranslation(new Vector2f(buttonPos.x, buttonPos.y + 3*buttonOffset - 10));
        mFootballButton.setDimensionsToText();
        
        mOptionsButton.setLocalTranslation(new Vector2f(buttonPos.x, buttonPos.y + 4*buttonOffset - 20));
        mOptionsButton.setDimensionsToText();
        
        mHighScoresButton.setLocalTranslation(new Vector2f(buttonPos.x, buttonPos.y + 5*buttonOffset - 30)); //FIXME: booooooooooodge
        mHighScoresButton.setDimensionsToText();
        
        float paralaxOffset = -34.0f; //tweak
        mParalax0.setLocalTranslation(new Vector2f(0,mOffset + (mBackground.getHeight()-mParalax0.getImageHeight()) + paralaxOffset));
        mParalax1.setLocalTranslation(new Vector2f(0,mOffset + (mBackground.getHeight()-mParalax1.getImageHeight()) + paralaxOffset));
        mParalax2.setLocalTranslation(new Vector2f(0,mOffset + (mBackground.getHeight()-mParalax2.getImageHeight()) + paralaxOffset));
    }
    
    private void initLevelLists(final StateBasedGame _sbg)
    {
        List<String> adventureLevelRefs = Arrays.asList("Level1","Level2");
        List<String> raceLevelRefs = Arrays.asList("RaceReloaded","Ice_Race", "BigBox");
        List<String> footballLevelRefs = Arrays.asList("The_Match");
        
        UnicodeFont levelListFont = sFontLoader.createFont("levelList", 52);
        int step = 0;
        for(String ref : adventureLevelRefs)
        {
            //create button
            Button button = new Button(cont, new Vector2f(200,-300 + (60*step)), new Vector2f());
            button.addText(cont, levelListFont, ref, true);
            button.setDimensionsToText();
            button.setIsVisible(false);
            //add to paralax
            mParalax0.addChild(button);
            //add to GUIManager and internal list
            GUIManager.use(mGUIManagerLeftAdventure).addSelectable(button);
            mAdventureLevels.add(button);
            //setup call back to load level
            button.setCallback(new StartLevel(ref) {public void run() {
                    StateGame.setGameType(StateGame.GameType.eAdventure, mRef);
                    _sbg.enterState(3, null, new BlobbyTransition(Color.black));
                }});
            step++;
        }
        step = 0;
        for(String ref : raceLevelRefs)
        {
            //create button
            Button button = new Button(cont, new Vector2f(200,-300 + (60*step)), new Vector2f());
            button.addText(cont, levelListFont, ref, true);
            button.setDimensionsToText();
            button.setIsVisible(false);
            //add to paralax
            mParalax0.addChild(button);
            //add to GUIManager and internal list
            GUIManager.use(mGUIManagerLeftRace).addSelectable(button);
            mRaceLevels.add(button);
            //setup call back to load level
            button.setCallback(new StartLevel(ref) {public void run() {
                    StateGame.setGameType(StateGame.GameType.eRace, mRef);
                    _sbg.enterState(3, null, new BlobbyTransition(Color.black));
                }});
            step++;
        }
        step = 0;
        for(String ref : footballLevelRefs)
        {
            //create button
            Button button = new Button(cont, new Vector2f(200,-300 + (60*step)), new Vector2f());
            button.addText(cont, levelListFont, ref, true);
            button.setDimensionsToText();
            button.setIsVisible(false);
            //add to paralax
            mParalax0.addChild(button);
            //add to GUIManager and internal list
            GUIManager.use(mGUIManagerLeftFootball).addSelectable(button);
            mFootballLevels.add(button);
            //setup call back to load level
            button.setCallback(new StartLevel(ref) {public void run() {
                    StateGame.setGameType(StateGame.GameType.eFootball, mRef);
                    _sbg.enterState(3, null, new BlobbyTransition(Color.black));
                }});
            step++;
        }
    }
    
    
    
}

abstract class StartLevel implements Runnable
{
    StartLevel(String _ref)
    {
        mRef = _ref;
    }
    String mRef = "NoLevel";

    abstract public void run();
}