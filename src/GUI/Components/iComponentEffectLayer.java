/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import java.util.ArrayDeque;

/**
 *
 * @author a203945
 */
public class iComponentEffectLayer {
    
    iComponentEffectLayer(iComponent _component)
    {
        mComponent = _component;
    }
    
    iComponent mComponent;
    ArrayDeque<iComponentEffect> mEffects = new ArrayDeque<iComponentEffect>();
    
    void render(int _x, int _y, float _rotation)
    {
        for(iComponentEffect effect : mEffects)
        {
            effect.render(_x, _y, _rotation);
        }
    }
    void update(int _delta)
    {
        for(iComponentEffect effect : mEffects)
        {
            effect.update(_delta);
        }
    }
    void pushEffect(iComponentEffect _effect)
    {
        mEffects.push(_effect);
    }
    void popEffect()
    {
        mEffects.pop();
    }
}
