/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.sEvents;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import java.util.HashMap;

/**
 *
 * @author alasdair
 */
class IntroEndSection extends IntroSection
{
    iSkin mSkin;
    public IntroEndSection()
    {
        sEvents.unblockAllEvents();
        HashMap params = new HashMap();
        params.put("ref", "IntroEnd");
        mSkin = sSkinFactory.create("static", params);
    }

    @Override
    public IntroSection updateImpl()
    {
        if (mTimer > 60)
        {
            return null;
        }
        return this;
    }

    @Override
    public void render()
    {
        mSkin.render(0,0);
    }
    
}
