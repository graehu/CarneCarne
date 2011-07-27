/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Menu;

import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.sGraphicsManager;
import Input.sInput;
import States.StateChanger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.BlobbyTransition;

/**
 *
 * @author a203945
 */
public class StateMenu extends BasicGameState implements iEventListener{
    Image mScreenShot;
    StateChanger mChangeToGame = null;
    static int mPlayer = 0;
    int mQuitDelay = 0;
    //@Override
    public int getID() {
        return 4;
    }
    
    public void init(GameContainer _gc, StateBasedGame _sbg) throws SlickException {
        //do crap
        Vec2 s = sGraphicsManager.getTrueScreenDimensions();
        mScreenShot = new Image((int)s.x, (int)s.y);
        mChangeToGame = new StateChanger(3, null, new BlobbyTransition(Color.black), _sbg);
        sEvents.subscribeToEvent("WindowResizeEvent", this);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException 
    {
        sEvents.subscribeToEvent("KeyDownEvent"+'Q'+mPlayer, this);
        sEvents.subscribeToEvent("KeyUpEvent"+'Q'+mPlayer, this);
        super.enter(container, game);
        mQuitDelay = 0;
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException 
    {
        sEvents.unsubscribeToEvent("KeyDownEvent"+'Q'+mPlayer, this);
        sEvents.unsubscribeToEvent("KeyUpEvent"+'Q'+mPlayer, this);
        super.leave(container, game);
    }
    
    

    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        _grphcs.drawImage(mScreenShot, 0, 0);
        //render crap
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _delta) throws SlickException 
    {
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
            mChangeToGame.setTransitions(null, new BlobbyTransition(Color.black));
            mChangeToGame.run();
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
    
}
