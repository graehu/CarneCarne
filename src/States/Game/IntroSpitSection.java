/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.MapClickEvent;
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
class IntroSpitSection extends IntroSection implements iEventListener
{
    iSkin mSkin;
    IntroSection mReturn;
    public IntroSpitSection()
    {
        sEvents.blockEvent("MapClickEventL"+0);
        sEvents.unblockEvent("MapClickEventR"+0);
        HashMap params = new HashMap();
        params.put("ref", "SpitOrSwallow");
        mSkin = sSkinFactory.create("static", params);
        sEvents.subscribeToEvent("MapClickEventR"+0, this);
        mReturn = this;
    }

    @Override
    public IntroSection updateImpl()
    {
        return mReturn;
    }

    @Override
    public void render()
    {
        mSkin.render(0, 0);
    }

    public boolean trigger(iEvent _event)
    {
        MapClickEvent event = (MapClickEvent)_event;
        mReturn = new IntroSwingSection();
        return false;
    }
    
}
