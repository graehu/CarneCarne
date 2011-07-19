/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ShaderUtils;

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
    FloatBuffer aux = BufferUtils.createFloatBuffer(4);
    FloatBuffer fbpos = BufferUtils.createFloatBuffer(4);
    FloatBuffer color = BufferUtils.createFloatBuffer(4);
    
    public LightSource addLightSource(Vector2f _position)
    {
        return addLightSource(_position, Color.white, 0.0f, 200.0f, 0.0f);
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
        //GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ZERO);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        
        Vec2 s = sGraphicsManager.getScreenDimensions();
        
        //WARNING: these buffers are allocated off the heap and are not properly garbage collected
        //the clear method resets the 'pointer' to the head but doesn't deallocate the memory
        //use with freak'n care
        aux.clear();
        aux.put(new float[]{(float)mLightSources.size(), s.x, s.y, mAmbience}).flip();
        
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, aux);

        for(int i = 0; i < mLightSources.size(); i++)
        {
            fbpos.clear();
            fbpos.put(new float[]{mLightSources.get(i).mPosition.x, mLightSources.get(i).mPosition.y, mLightSources.get(i).mRadius, 0.0f}).flip();

            color.clear();
            color.put(new float[]{  mLightSources.get(i).mColor.a, 
                                    mLightSources.get(i).mColor.g, 
                                    mLightSources.get(i).mColor.b, 
                                    0}).flip();
        
            GL11.glEnable(GL11.GL_LIGHT0 + i);
            GL11.glLight(GL11.GL_LIGHT0 + i, GL11.GL_POSITION, fbpos);
            GL11.glLight(i, i, aux);
            GL11.glLight(GL11.GL_LIGHT0 + i, GL11.GL_DIFFUSE, color);
            
        }
        super.startShader();
    }
         
    public void endShader()
    {
        //GL11.glDisable(GL11.GL_BLEND);
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
