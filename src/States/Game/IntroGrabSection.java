/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.sEvents;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import Level.sLevel;
import java.util.HashMap;

/**
 *
 * @author alasdair
 */
class IntroGrabSection extends IntroSection
{
    iSkin mSkin;
    public IntroGrabSection()
    {
        sEvents.unblockEvent("MapClickEventL"+0);
        HashMap params = new HashMap();
        params.put("ref", "EatIt");
        mSkin = sSkinFactory.create("static", params);
    }

    @Override
    public IntroSection updateImpl()
    {
        if (sLevel.getPathInfo(7, 72).equals(sLevel.PathInfo.eAir))
        {
            return new IntroSmashSection();
        }
        return this;
    }
    @Override
    public void render()
    {
        mSkin.render(0,0);
    }
    
}
