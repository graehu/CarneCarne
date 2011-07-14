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
class IntroSmashSection extends IntroSection
{
    iSkin mSkin;
    public IntroSmashSection()
    {
        sEvents.unblockEvent("MapClickEventL"+0);
        sEvents.blockEvent("MapClickEventR"+0);
        HashMap params = new HashMap();
        params.put("ref", "SmashIt");
        mSkin = sSkinFactory.create("static", params);
    }

    @Override
    public IntroSection updateImpl()
    {
        if (sLevel.getPathInfo(2, 66).equals(sLevel.PathInfo.eAir))
        {
            return new IntroSpitSection();
        }
        return this;
    }

    @Override
    public void render()
    {
        mSkin.render(0, 0);
    }
    
}
