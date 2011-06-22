/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States;

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
        mSbg.enterState(mID,mLeave,mEnter);
    }
}
