/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Title;

import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import GUI.Components.Button;
import GUI.Components.GraphicalComponent;
import GUI.Components.ScrollableComponent;
import GUI.Components.TextField;
import Graphics.sGraphicsManager;
import Utils.sFontLoader;
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
 * @author a203945
 */
public class StateTitle extends BasicGameState implements iEventListener{
   // @Override
    public int getID() {
       return 2;
    }

    public void trigger(iEvent _event) {
        if(_event.getName().equals("WindowResizeEvent"))
        {
            calcUI();
        }
    }
    
    enum InternalState
    {
        eCenter,
        eLeft,
        eRight
    }
    
    GraphicalComponent mBackground = null, mForeground = null;
    GraphicalComponent mLogo = null;
    ScrollableComponent mParalax0 = null;
    ScrollableComponent mParalax1 = null;
    ScrollableComponent mParalax2 = null;
    TextField mNameField = null;
    Button mAdventureButton = null;
    Button mRaceButton = null;
    Button mOptionsButton = null;
    Button mHighScoresButton = null;
    float mScale = 1.0f;
    float mOffset = 0.0f;
    InternalState mState = InternalState.eCenter;
    UnicodeFont mUIFont = null;
    Font mInputFont = null;

    public void init(final GameContainer _gc, final StateBasedGame _sbg) throws SlickException {
        sEvents.subscribeToEvent("WindowResizeEvent", this);
        
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
        mLogo = new GraphicalComponent(_gc, new Vector2f(), new Vector2f());
        mLogo.setImage("ui/title/logo.png");
        mLogo.setDimentionsToImage();
        
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
        
        //initialise buttons
        Vector2f buttonDim = new Vector2f(200/mScale,50/mScale);
        mNameField = new TextField(_gc, mInputFont, 0, 0, (int)buttonDim.x, (int)buttonDim.y);
        mNameField.setMaxLength(15);
        mNameField.setDefaultText("Name");
        mNameField.setTextColor(new Color(0,0,0));
        mNameField.setSelectedTextColor(new Color(187,139,44));
        mAdventureButton = new Button(_gc, new Vector2f(), buttonDim);
        mAdventureButton.addText(_gc, mUIFont, "AdvEnturE ModE", true); //uppercase 'e' 's' because lowercase is crap
        mRaceButton = new Button(_gc, new Vector2f(), buttonDim);
        mRaceButton.addText(_gc, mUIFont, "RacE ModE", true);
        mOptionsButton = new Button(_gc, new Vector2f(), buttonDim);
        mOptionsButton.addText(_gc, mUIFont, "OptionS", true);
        mHighScoresButton = new Button(_gc, new Vector2f(), buttonDim);
        mHighScoresButton.addText(_gc, mUIFont, "HighScorES", true);
        
        calcUI();
         
        mAdventureButton.setCallback(new Runnable() {
            public void run() {
                _sbg.enterState(3, null, new BlobbyTransition(Color.black));
            }
        });
        
    }
    
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        boolean debug = false;
        mBackground.render(_gc, _grphcs, debug);
        //render other stuff here
        mParalax2.render(_gc, _grphcs, debug);
        mLogo.render(_gc, _grphcs, debug);
        mParalax1.render(_gc, _grphcs, debug);
        mParalax0.render(_gc, _grphcs, debug);
        
        mForeground.render(_gc, _grphcs, debug);
        
        mNameField.render(_gc, _grphcs, debug);
        mAdventureButton.render(_gc, _grphcs, debug);
        mRaceButton.render(_gc, _grphcs, debug);
        mOptionsButton.render(_gc, _grphcs, debug);
        mHighScoresButton.render(_gc, _grphcs, debug);
        
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException {
        Input input = _gc.getInput();
        if(input.isKeyDown(Input.KEY_F11))
            sGraphicsManager.toggleFullscreen();  
        
        float speed = 0.005f;
        if(input.isKeyDown(Input.KEY_LEFT))
        {
            mParalax0.scrollHorizontalBy(-speed); 
            mParalax1.scrollHorizontalBy(-speed);
            mParalax2.scrollHorizontalBy(-speed);
        }
        if(input.isKeyDown(Input.KEY_RIGHT))
        {
            mParalax0.scrollHorizontalBy(speed);
            mParalax1.scrollHorizontalBy(speed);
            mParalax2.scrollHorizontalBy(speed);
        }
        
        mParalax0.update(_i);
        mParalax1.update(_i);
        mParalax2.update(_i);
        
        mNameField.update(_i);
        mAdventureButton.update(_i);
        mRaceButton.update(_i);
        mOptionsButton.update(_i);
        mHighScoresButton.update(_i);
    }
    
    protected void calcUI()
    {
        //calc scale and offset values
        Vec2 screen = sGraphicsManager.getTrueScreenDimensions();
        //scale by x to fit but keep aspect ratio using offset
        mScale = screen.x/mBackground.getWidth();
        mOffset = (screen.y - mBackground.getHeight()*mScale) * 0.5f;
        Vector2f vOffset = new Vector2f(0, mOffset);
        
        mBackground.setLocalScale(mScale);
        mBackground.setLocalTranslation(vOffset);
        mForeground.setLocalScale(mScale);
        mForeground.setLocalTranslation(vOffset);
        
        mLogo.setLocalScale(mScale);
        mLogo.setLocalTranslation(new Vector2f(mBackground.getWidth() * 0.1f, mOffset + (mBackground.getHeight() * 0.15f)));
        
        Vector2f buttonPos = new Vector2f(mBackground.getWidth() * 0.6f, mOffset + (mBackground.getHeight() * 0.3f));
        int buttonOffset = (int)75;
        
        mNameField.setLocalScale(mScale);
        mNameField.setLocalTranslation(new Vector2f(buttonPos.x, buttonPos.y));
        
        mAdventureButton.setLocalScale(mScale);
        mAdventureButton.setLocalTranslation(new Vector2f(buttonPos.x, buttonPos.y + buttonOffset + 10)); //constant to provide extra offset for namefield
        mAdventureButton.setDimensionsToText();
        
        mRaceButton.setLocalScale(mScale);
        mRaceButton.setLocalTranslation(new Vector2f(buttonPos.x, buttonPos.y + 2*buttonOffset));
        mRaceButton.setDimensionsToText();
        
        mOptionsButton.setLocalScale(mScale);
        mOptionsButton.setLocalTranslation(new Vector2f(buttonPos.x, buttonPos.y + 3*buttonOffset - 10));
        mOptionsButton.setDimensionsToText();
        
        mHighScoresButton.setLocalScale(mScale);
        mHighScoresButton.setLocalTranslation(new Vector2f(buttonPos.x, buttonPos.y + 4*buttonOffset - 20)); //FIXME: booooooooooodge
        mHighScoresButton.setDimensionsToText();
        
        mParalax0.setLocalScale(mScale);
        mParalax1.setLocalScale(mScale);
        mParalax2.setLocalScale(mScale);
        
        mParalax0.setLocalTranslation(new Vector2f(0,mOffset + (mBackground.getHeight()-mParalax0.getImageHeight())));
        mParalax1.setLocalTranslation(new Vector2f(0,mOffset + (mBackground.getHeight()-mParalax1.getImageHeight())));
        mParalax2.setLocalTranslation(new Vector2f(0,mOffset + (mBackground.getHeight()-mParalax2.getImageHeight())));
    }
    
}
