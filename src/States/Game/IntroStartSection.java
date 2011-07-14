/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import Events.sEvents;

/**
 *
 * @author alasdair
 */
class IntroStartSection extends IntroSection
{
    public IntroStartSection()
    {
        sEvents.blockEvent("MapClickEventL"+0);
        sEvents.blockEvent("MapClickEventR"+0);
        sEvents.blockEvent("KeyDownEvent"+'w'+0);
    }

    @Override
    public IntroSection updateImpl()
    {
        if (mTimer > 60)
        {
            return new IntroJumpSection();
        }
        return this;
    }

    @Override
    public void render()
    {
    }
    
}
