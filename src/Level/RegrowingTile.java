/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

/**
 *
 * @author alasdair
 */
public class RegrowingTile {
    
    public RegrowingTile(int _x, int _y, int _rootId, int _timer)
    {
        x = _x;
        y = _y;
        mRootId = _rootId;
        timer = _timer;
    }
    int x, y;
    int mRootId;
    int timer;
}
