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
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class IntroJumpSection extends IntroSection implements iEventListener
{
    IntroSection mReturn;
    iSkin mSkin;
    public IntroJumpSection(Vec2 _position, int _playerNumber)
    {
        super(_position, _playerNumber);
        sEvents.unblockEvent("KeyDownEvent"+'w'+mPlayerNumber);
        sEvents.subscribeToEvent("KeyDownEvent"+'w'+mPlayerNumber, this);
        mReturn = this;
        HashMap params = new HashMap();
        params.put("ref", "SignTutorialJump");
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
        mReturn = new IntroGrabSection(mPosition, mPlayerNumber);
        return false;
    }
    
}