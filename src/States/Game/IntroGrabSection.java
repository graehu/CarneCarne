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
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class IntroGrabSection extends IntroSection
{
    iSkin mSkin;
    public IntroGrabSection(Vec2 _position, int _playerNumber)
    {
        super(_position, _playerNumber);
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
    public void render()
    {
        mSkin.render(0,0);
    }
    
}
