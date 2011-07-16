/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game.Tutorial;

import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
abstract public class IntroSection
{
    protected int mTimer;
    protected Vec2 mPosition;
    protected int mPlayerNumber;
    public IntroSection(Vec2 _position, int _playerNumber)
    {
        mTimer = 0;
        mPosition = _position;
        mPlayerNumber = _playerNumber;
    }
    
    public IntroSection update()
    {
        mTimer++;
        return updateImpl();
    }
    abstract public IntroSection updateImpl();
    abstract public void render();
}
