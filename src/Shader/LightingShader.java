/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Shader;

import Graphics.sGraphicsManager;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import org.jbox2d.common.Vec2;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author a203945
 */
public class LightingShader extends Shader 
{
    static int maxLights = 8;
    private LightingShader(ShaderResourceManager srm, Collection<String> vertex, Collection<String> fragment) throws SlickException
    {
        super(srm, vertex, fragment);
    }
    
    ArrayList<LightSource> mLightSources = new ArrayList<LightSource>();
    float mAmbience = 0.5f;
    
    public LightSource addLightSource(Vector2f _position)
    {
        return addLightSource(_position, Color.white, 0.75f, 200.0f, 0.0f);
    }
    //returns null when max lights is reached
    public LightSource addLightSource(Vector2f _position, Color _color, float _constantAttentuation, float _radius, float _quadraticAttentuation)
    {
        int index = mLightSources.size();
        if(index < maxLights)
        {
            mLightSources.add(new LightSource(_position, _color, _constantAttentuation, _radius, _quadraticAttentuation));
            return mLightSources.get(index);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void startShader() 
    {
        Vec2 s = sGraphicsManager.getScreenDimensions();
        setUniformIntVariable("numLights", mLightSources.size());
        setUniformFloatVariable("screenDimentions", s.x, s.y);
        setUniformFloatVariable("ambience", mAmbience);

        for(int i = 0; i < mLightSources.size(); i++)
        {
            FloatBuffer fbpos = BufferUtils.createFloatBuffer(4);
            fbpos.put(new float[]{mLightSources.get(i).mPosition.x, mLightSources.get(i).mPosition.y, mLightSources.get(i).mRadius, 0.0f}).flip();

            FloatBuffer color = BufferUtils.createFloatBuffer(4);
            fbpos.put(new float[]{  mLightSources.get(i).mColor.r, 
                                    mLightSources.get(i).mColor.g, 
                                    mLightSources.get(i).mColor.b, 
                                    0}).flip();
        
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_LIGHT0 + i);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glLight(GL11.GL_LIGHT0 + i, GL11.GL_POSITION, fbpos);
            GL11.glLight(GL11.GL_LIGHT0 + i, GL11.GL_COLOR, color);
        }
        super.startShader();
    }
         
    public void endShader()
    {
        GL11.glDisable(GL11.GL_LIGHTING);
    }
    
    
    public static LightingShader makeShader(String vertexFileName, String fragmentFileName) throws SlickException
    {
        ArrayList<String> l1 = new ArrayList<String>();
        l1.add(vertexFileName);
        ArrayList<String> l2 = new ArrayList<String>();
        l2.add(fragmentFileName);

        return new LightingShader(ShaderResourceManagerImpl.getSRM(), l1, l2);
    }
}
