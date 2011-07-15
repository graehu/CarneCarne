/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.PlayerEndedTutorialEvent;
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
class IntroEndSection extends IntroSection implements iEventListener
{
    iSkin mSkin;
    int mNewTimer = 0;
    public IntroEndSection(Vec2 _position, int _playerNumber)
    {
        super(_position, _playerNumber);
        sEvents.unblockAllEvents();
        HashMap params = new HashMap();
        params.put("ref", "SignTutorialFinish");
        mSkin = sSkinFactory.create("static", params);
        sEvents.subscribeToEvent("AllPlayersTutorialEndedEvent", this);
        sEvents.triggerEvent(new PlayerEndedTutorialEvent(mPlayerNumber));
    }

    @Override
    public IntroSection updateImpl()
    {
        if (mNewTimer != 0)
        {
            mNewTimer--;
            if (mNewTimer == 0)
            {
                return null;
            }
        }
        return this;
    }

    @Override
    public void render()
    {
        mSkin.render(0,0);
    }

    public boolean trigger(iEvent _event)
    {
        mNewTimer = 120;
        return false;
    }
    
}
