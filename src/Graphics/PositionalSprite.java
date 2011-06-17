/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

/**
 *
 * @author alasdair
 */
public class PositionalSprite extends iSprite {
    
    public PositionalSprite(iSkin _skin)
    {
        super(_skin);
    }
    
    public void render()
    {
        mSkin.render(mPosition.x,mPosition.y);      
    }
}
