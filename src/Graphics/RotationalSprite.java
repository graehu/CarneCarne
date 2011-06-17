/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

/**
 *
 * @author alasdair
 */
public class RotationalSprite extends iSprite {
    
    private float mRotation;
    public RotationalSprite(iSkin _skin)
    {
        super(_skin);
        mRotation = 0.0f;
    }
    
    public void setRotation(float _rotation)
    {
        mRotation = (float) (_rotation*(180/Math.PI));
    }
    public void render()
    {
        mSkin.render(mPosition.x,mPosition.y);
        mSkin.setRotation(mRotation);   
    }
}
