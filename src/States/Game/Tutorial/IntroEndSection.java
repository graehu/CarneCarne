/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.Tutorial;

import Events.PlayerEndedTutorialEvent;
import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import Graphics.Skins.sSkinFactory;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class IntroEndSection extends IntroSection implements iEventListener
{
    int mNewTimer = 0;
    public IntroEndSection(Vec2 _position, int _playerNumber)
    {
        super(_position, _playerNumber, null, null, 0.0f);
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
    protected void renderInternal(float scale)
    {
        mSkin.setDimentions(448*scale, 300*scale);
        mSkin.render(600*scale,0);
    }

    public boolean trigger(iEvent _event)
    {
        mNewTimer = 120;
        return false;
    }
    
}
