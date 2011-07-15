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
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class IntroSwingSection extends IntroSection implements iEventListener
{
    iSkin mSkin;
    int mTimer = 0;
    IntroSection mSection;
    public IntroSwingSection(Vec2 _position, int _playerNumber)
    {
        super(_position, _playerNumber);
        Vec2 tile = mPosition.sub(new Vec2(1,3));
        sEvents.unblockEvent("MapClickEventL"+mPlayerNumber);
        sLevel.placeTile((int)tile.x,(int)tile.y, 17);
        HashMap params = new HashMap();
        params.put("ref", "SignTutorialSwing");
        mSkin = sSkinFactory.create("static", params);
        sEvents.subscribeToEvent("PlayerSwingEvent" + mPlayerNumber, this);
        mSection = this;
    }

    @Override
    public IntroSection updateImpl()
    {
        /*if (mTimer != 0)
        {
            mTimer--;
            if (mTimer == 0)
            {
                return 
            }
        }
        return this;*/
        return mSection;
    }

    @Override
    public void render()
    {
        mSkin.render(0, 0);
    }

    public boolean trigger(iEvent _event)
    {
        mSection = new IntroEndSection(mPosition, mPlayerNumber);
        return false;
    }
    
}
