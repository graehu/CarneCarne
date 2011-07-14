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
import Level.sLevel;
import java.util.HashMap;

/**
 *
 * @author alasdair
 */
class IntroSwingSection extends IntroSection implements iEventListener
{
    IntroSection mSection;
    iSkin mSkin;
    public IntroSwingSection()
    {
        sEvents.unblockEvent("MapClickEventL"+0);
        sLevel.placeTile(2, 66, 17);
        HashMap params = new HashMap();
        params.put("ref", "Swing");
        mSkin = sSkinFactory.create("static", params);
        sEvents.subscribeToEvent("PlayerSwingEvent" + 0, this);
        mSection = this;
    }

    @Override
    public IntroSection updateImpl()
    {
        return mSection;
    }

    @Override
    public void render()
    {
        mSkin.render(0, 0);
    }

    public boolean trigger(iEvent _event)
    {
        mSection = new IntroEndSection();
        return false;
    }
    
}
