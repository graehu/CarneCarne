/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

/**
 *
 * @author alasdair
 */
abstract public class IntroSection
{
    protected int mTimer;

    public IntroSection()
    {
        mTimer = 0;
    }
    
    public IntroSection update()
    {
        mTimer++;
        return updateImpl();
    }
    abstract public IntroSection updateImpl();
    abstract public void render();
}
