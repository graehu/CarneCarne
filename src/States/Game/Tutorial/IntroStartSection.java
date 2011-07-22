/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.Tutorial;

import Events.sEvents;
import Graphics.Skins.sSkinFactory;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class IntroStartSection extends IntroSection
{
    public IntroStartSection(Vec2 _position, int _playerNumber)
    {
        super(_position, _playerNumber, null, null, 0.0f);
        sEvents.blockEvent("MapClickEventL"+mPlayerNumber);
        sEvents.blockEvent("MapClickEventR"+mPlayerNumber);
        sEvents.blockEvent("KeyDownEvent"+'w'+mPlayerNumber);
        HashMap params = new HashMap();
        params.put("ref", "tutorialJumpStart");
        mSkin = sSkinFactory.create("animated", params);
        mSkin.stopAt(12);
    }

    @Override
    public IntroSection updateImpl()
    {
        if (mTimer > 60)
        {
            return new IntroJumpSection(mPosition,mPlayerNumber);
        }
        return this;
    }

    @Override
    protected void renderInternal(float scale)
    {
        mSkin.setDimentions(450*scale, 225*scale);
        mSkin.render(600*scale,0);
    }

}
