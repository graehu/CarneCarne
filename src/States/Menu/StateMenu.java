/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Menu;

import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import GUI.TWL.BasicTWLGameState;
import States.StateChanger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.BlobbyTransition;

/**
 *
 * @author a203945
 */
public class StateMenu extends BasicTWLGameState implements iEventListener{
    Image mScreenShot;
    StateChanger mChangeToGame = null;
    int mPlayer = 0;
    @Override
    public int getID() {
        return 4;
    }

    public void init(GameContainer _gc, StateBasedGame _sbg) throws SlickException {
        //do crap
        mChangeToGame = new StateChanger(3, null, new BlobbyTransition(Color.black), _sbg);
    }

    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        _grphcs.drawImage(mScreenShot, 0, 0);
        //render crap
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _delta) throws SlickException {
        mChangeToGame.run();
        //update crap
    }
    public void setScreenShot(Image _img)
    {
        mScreenShot = _img;
        mScreenShot.setAlpha(0.5f);
    }
    public void setPlayerinControl(int _player) throws Throwable
    {
        mPlayer = _player;
    }

    public void trigger(iEvent _event) 
    {
        if(_event.getName().equals("KeyDownEvent"+'Q'+mPlayer))
        {
            mChangeToGame.run();
        }
    }
    
}
