/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Menu;

import Events.GenericEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import GUI.Components.Button;
import GUI.Components.Text;
import GUI.GUIManager;
import Graphics.sGraphicsManager;
import Input.sInput;
import States.StateChanger;
import Utils.sFontLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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
public class StateMenu extends BasicGameState implements iEventListener
{
    static int mPlayer = 0;
    
    Image mScreenShot;
    StateChanger mChangeToGame = null;
    StateChanger mChangeToTitle = null;
    int mQuitDelay = 0;
    
    Integer mGUIManager = null;
    Text mPausedByPlayerText = null;
    Button mResumeButton = null;
    Button mExitToTitleButton = null;
    
    //@Override
    public int getID() {
        return 4;
    }
    
    public void init(GameContainer _gc, StateBasedGame _sbg) throws SlickException {
        //create GUI
        UnicodeFont menuFont = sFontLoader.createFont("menu", 52);
        mGUIManager = GUIManager.create(_gc);
        
        mPausedByPlayerText = new Text(_gc, menuFont, "PAUSED", new Vector2f(730,200), true);
        mPausedByPlayerText.setColor(Color.yellow);
        
        mResumeButton = new Button(_gc, new Vector2f(720,400), new Vector2f());
        mResumeButton.addText(_gc, menuFont, "Resume", true);
        mResumeButton.setButtonStateColors(Color.gray, null, null);
        mResumeButton.setCallback(new Runnable() { public void run() 
        {
            returnToGame();
        }});
        
        mExitToTitleButton = new Button(_gc, new Vector2f(650,500), new Vector2f());
        mExitToTitleButton.addText(_gc, menuFont, "Exit To Title", true);
        mExitToTitleButton.setButtonStateColors(Color.gray, null, null);
        mExitToTitleButton.setCallback(new Runnable() { public void run() 
        {
            exitToTitle();
        }});
        
        GUIManager.use(mGUIManager).addRootComponent(mPausedByPlayerText); 
        GUIManager.use(mGUIManager).addRootComponent(mResumeButton); 
        GUIManager.use(mGUIManager).addRootComponent(mExitToTitleButton); 
        
        //init screenshot thingy
        Vec2 s = sGraphicsManager.getTrueScreenDimensions();
        mScreenShot = new Image((int)s.x, (int)s.y);
        mChangeToGame = new StateChanger(3, null, new BlobbyTransition(Color.black), _sbg);
        mChangeToTitle = new StateChanger(2, null, new BlobbyTransition(Color.black), _sbg);
        sEvents.subscribeToEvent("WindowResizeEvent", this);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException 
    {
        //update menu text
        mPausedByPlayerText.setTextString("Paused by Player " + (mPlayer+1));
        mPausedByPlayerText.resizeToText();
        mPausedByPlayerText.setLocalTranslation(new Vector2f(   840 - (mPausedByPlayerText.getWidth() * 0.5f),
                                                                mPausedByPlayerText.getY()));
        sEvents.subscribeToEvent("KeyDownEvent"+'Q'+mPlayer, this);
        sEvents.subscribeToEvent("KeyUpEvent"+'Q'+mPlayer, this);
        GUIManager.use(mGUIManager).listenToPlayer(mPlayer);
        GUIManager.use(mGUIManager).setAcceptingInput(true);
        super.enter(container, game);
        mQuitDelay = 0;
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException 
    {
        sEvents.unsubscribeToEvent("KeyDownEvent"+'Q'+mPlayer, this);
        sEvents.unsubscribeToEvent("KeyUpEvent"+'Q'+mPlayer, this);
        GUIManager.use(mGUIManager).setAcceptingInput(false);
        
        //reset PausedByPlayerText text
        mPausedByPlayerText.setTextString("PAUSED");
        mPausedByPlayerText.resizeToText();
        mPausedByPlayerText.setLocalTranslation(new Vector2f(730,200));
        
        sInput.update(16);
        
        super.leave(container, game);
    }
    
    

    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException 
    {
        _grphcs.drawImage(mScreenShot, 0, 0);
        GUIManager.use(mGUIManager).render(false);
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _delta) throws SlickException 
    {
        GUIManager.use(mGUIManager).update(_delta);
        sEvents.processEvents();
        sInput.update(_delta);
        mQuitDelay += _delta;
        //update crap
    }
    public Image getScreenShot()
    {
        return mScreenShot;
    }
    public static void setPlayerInControl(int _player)
    {
        mPlayer = _player;
    }

    public boolean trigger(iEvent _event) 
    {
        if(mQuitDelay > 1000 && _event.getName().equals("KeyDownEvent"+'Q'+mPlayer))
        {
            returnToGame();
        }
        if(_event.getName().equals("WindowResizeEvent"))
        {
            try 
            {
                Vec2 s = sGraphicsManager.getTrueScreenDimensions();
                mScreenShot.destroy();
                mScreenShot = new Image((int)s.x, (int)s.y);
            } 
            catch (SlickException ex) 
            {
                Logger.getLogger(StateMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }
    
    private void returnToGame()
    {
        sEvents.triggerEvent(new GenericEvent("GameUnpaused"));
        mChangeToGame.setTransitions(null, new BlobbyTransition(Color.black));
        mChangeToGame.run();
    }
    private void exitToTitle()
    {
        mChangeToTitle.setTransitions(null, new BlobbyTransition(Color.black));
        mChangeToTitle.run();
    }
    
}
