/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ShaderUtils;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author a203945
 */
public class LightSource 
{
    LightSource(Vector2f _position, Color _color, float _constantAttentuation, float _radius, float _quadraticAttentuation)
    {
        mPosition = _position;
        mColor = _color;
        mConstantAttentuation = _constantAttentuation;
        mRadius = _radius;
        mQuadraticAttentuation = _quadraticAttentuation;
    }
    
    Vector2f mPosition = null;
    Color mColor = Color.white;
    float mConstantAttentuation = 0.5f;
    float mRadius = 0.0f;
    float mQuadraticAttentuation = 0.0f;
        
    public void update(int _delta)
    {

    }
}
