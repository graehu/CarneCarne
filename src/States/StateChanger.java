/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States;

import Graphics.sGraphicsManager;
import States.Menu.StateMenu;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

/**
 *
 * @author a203945
 */
public class StateChanger implements Runnable{
    private static StateBasedGame mSbg;
    private static int mID = 0;
    private Transition mEnter,mLeave;
    public StateChanger(int _id, Transition _leave, Transition _enter, StateBasedGame _sbg)
    {
        mID = _id;
        mLeave = _leave;
        mEnter = _enter;
        mSbg = _sbg;
    }

    public void run() {
        if(mID == 4) //menu
        {
            try {
                int sx = (int)sGraphicsManager.getTrueScreenDimensions().x;
                int sy = (int)sGraphicsManager.getTrueScreenDimensions().y;
                Image screenShot = new Image(sx, sy);
                mSbg.getContainer().getGraphics().copyArea(screenShot, 0, 0);
                ((StateMenu)mSbg.getState(mID)).setScreenShot(screenShot);
            } catch (SlickException ex) {
                Logger.getLogger(StateChanger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        mSbg.enterState(mID,mLeave,mEnter);
    }
}
