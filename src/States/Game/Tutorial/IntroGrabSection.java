/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.Tutorial;

import Events.sEvents;
import Graphics.Skins.sSkinFactory;
import Level.sLevel;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class IntroGrabSection extends IntroSection
{
    public IntroGrabSection(Vec2 _position, int _playerNumber)
    {
        super(_position, _playerNumber, "XBoxTop", "XBoxTopTongue", 1.85f);
        sEvents.unblockEvent("MapClickEventL"+mPlayerNumber);
        HashMap params = new HashMap();
        params.put("ref", "SignTutorialEat");
        mSkin = sSkinFactory.create("static", params);
    }

    @Override
    public IntroSection updateImpl()
    {
        Vec2 tile = mPosition.add(new Vec2(4,0));
        if (sLevel.getPathInfo((int)tile.x,(int)tile.y).equals(sLevel.PathInfo.eAir))
        {
            return new IntroSmashSection(mPosition, mPlayerNumber);
        }
        return this;
    }
    @Override
    protected void renderInternal(float scale)
    {
        mSkin.setDimentions(448*scale, 300*scale);
        mSkin.render(600*scale,0);
    }
    
}
