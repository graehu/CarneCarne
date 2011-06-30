/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Menu;

import GUI.TWL.BasicTWLGameState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author a203945
 */
public class StateMenu extends BasicTWLGameState{
    Image mScreenShot;
    @Override
    public int getID() {
        return 4;
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        //do crap
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        grphcs.drawImage(mScreenShot, 0, 0);
        //render crap
    }

    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        //update crap
    }
    public void setScreenShot(Image _img)
    {
        mScreenShot = _img;
        //mScreenShot.setAlpha(0.5f);
    }
    
}
