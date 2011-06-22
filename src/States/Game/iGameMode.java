/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Game;

import org.newdawn.slick.Graphics;

/**
 *
 * @author alasdair
 */
public interface iGameMode {
    
    public void update(float _time);
    public void render(Graphics _graphics);
}
