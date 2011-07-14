/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.sEvents;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class IntroEndSection extends IntroSection
{
    iSkin mSkin;
    public IntroEndSection(Vec2 _position, int _playerNumber)
    {
        super(_position, _playerNumber);
        sEvents.unblockAllEvents();
        HashMap params = new HashMap();
        params.put("ref", "SignTutorialFinish");
        mSkin = sSkinFactory.create("static", params);
    }

    @Override
    public IntroSection updateImpl()
    {
        if (mTimer > 120)
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
