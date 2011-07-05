
package GUI.Components;

import java.util.ArrayList;
import java.util.Iterator;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author Aaron
 */
public abstract class iComponent extends AbstractComponent {

    protected iComponent(GUIContext _context, iComponent _parent) 
    {
        super(_context);
        
        //initialise member variables
        mParent = _parent;
        mEffectLayer = new iComponentEffectLayer(this);
        mChildren = new ArrayList<iComponent>();
        mRotation = 0.0f;
        mTranslation = new Vector2f(0,0);
        mDimensions = new Vector2f(0,0);
        mIsDestroyed = false;
    }
    
    public iComponentEffectLayer mEffectLayer;
    private iComponent mParent;
    private ArrayList<iComponent> mChildren;
    private float mRotation;
    private Vector2f mTranslation;
    private Vector2f mDimensions;
    private boolean mIsDestroyed;
    
    abstract boolean update(int _delta);
    public void render(GUIContext guic, Graphics grphcs) throws SlickException
    {
        if(mEffectLayer != null)
        {
            Vector2f trans = getGlobalTranslation();
            mEffectLayer.render((int)trans.x, (int)trans.y, getGlobalRotation());
        }
    }
    
    public final float getGlobalRotation()
    {
        float rotation = mRotation;
        if(mParent != null)
        {
            rotation += mParent.getGlobalRotation();
        }
        return rotation;
    }
    public final Vector2f getGlobalTranslation()
    {
        Vector2f translation = mTranslation.copy();
        if(mParent != null)
        {
            translation = translation.add(mParent.getGlobalTranslation());
        }
        return translation;
    }
    public final float getLocalRotation(){return mRotation;}
    public final void setLocalRotation(float _degrees)
    {
        mRotation =_degrees;
    }
    public final Vector2f getLocalTranslation(){return mTranslation.copy();}
    public final void setLocalTranslation(Vector2f _position)
    {
        mTranslation.x = _position.x;
        mTranslation.y = _position.y;
    }
    public final Vector2f getDimensions(){return mDimensions.copy();}
    public final void setDimensions(Vector2f _dimensions)
    {
        mDimensions.x = _dimensions.x;
        mDimensions.y = _dimensions.y;
    }
    public final boolean setParent(iComponent _parent)
    {
        if(mParent == null)
        {
            mParent = _parent;
            return true;
        }
        else return false;
    }
    //returns false if child already has a parent
    public final boolean addChild(iComponent _child)
    {
        if(_child.setParent(this))
        {
            mChildren.add(_child);
            return true;
        }
        else return false;
    }
    public final void removeChild(iComponent _child)
    {
        mChildren.remove(_child);
    }
    public final Iterator<iComponent> getChildIterator()
    {
        return mChildren.listIterator();
    }
    
    /*
     * methods for destruction
     */
    public final boolean isDestroyed()
    {
        return mIsDestroyed;
    }
    public final void destroy()
    {
        mParent.removeChild(this);
        destroy();
    }
    protected final void destroyInternal()
    {
        //set destroyed flag
        mIsDestroyed = true;
        //destroy children
        Iterator<iComponent> itr = mChildren.listIterator();
        while(itr.hasNext())
        {
            iComponent child = itr.next();
            child.destroyInternal();
        }
        //remove from parent
        mParent = null;
        
    }
     
    /*
     * Overrides for AbstractComponent
     */
    @Override
    public void setLocation(int _x, int _y) {
        if(mTranslation == null) //required due to call in parent's constructor
            mTranslation = new Vector2f(0,0);
        setLocalTranslation(new Vector2f(_x,_y));
    }
    @Override
    public int getX() {
        return (int)mTranslation.x;
    }
    @Override
    public int getY() {
        return (int)mTranslation.y;
    }
    @Override
    public int getWidth() {
        return (int)mDimensions.x;
    }
    @Override
    public int getHeight() {
        return (int)mDimensions.y;
    }
}
