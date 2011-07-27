/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.Tutorial;

import Events.MapClickEvent;
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
class IntroSpitSection extends IntroSection implements iEventListener
{
    IntroSection mReturn;
    public IntroSpitSection(Vec2 _position, int _playerNumber)
    {
        super(_position, _playerNumber, "XBoxTop", "XBoxTopSpit", 1.85f);
        sEvents.blockEvent("MapClickEventL"+mPlayerNumber);
        sEvents.unblockEvent("MapClickEventR"+mPlayerNumber);
        HashMap params = new HashMap();
        params.put("ref", "SignTutorialSpit");
        mSkin = sSkinFactory.create("static", params);
        sEvents.subscribeToEvent("MapClickEventR"+mPlayerNumber, this);
        mReturn = this;
    }

    @Override
    public IntroSection updateImpl()
    {
        return mReturn;
    }

    @Override
    protected void renderInternal(float scale)
    {
        mSkin.setDimentions(448*scale, 300*scale);
        mSkin.render(600*scale,0);
    }

    public boolean trigger(iEvent _event)
    {
        MapClickEvent event = (MapClickEvent)_event;
        mReturn = new IntroSwingSection(mPosition, mPlayerNumber);
        return false;
    }
    
}
