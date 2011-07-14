/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import java.util.HashMap;

/**
 *
 * @author alasdair
 */
class IntroJumpSection extends IntroSection implements iEventListener
{
    IntroSection mReturn;
    iSkin mSkin;
    public IntroJumpSection()
    {
        sEvents.unblockEvent("KeyDownEvent"+'w'+0);
        sEvents.subscribeToEvent("KeyDownEvent"+'w'+0, this);
        mReturn = this;
        HashMap params = new HashMap();
        params.put("ref", "JumpYouWhore");
        mSkin = sSkinFactory.create("static", params);
    }

    @Override
    public IntroSection updateImpl()
    {
        return mReturn;
    }
    @Override
    public void render()
    {
        mSkin.render(0,0);
    }

    public boolean trigger(iEvent _event)
    {
        mReturn = new IntroGrabSection();
        return false;
    }
    
}
