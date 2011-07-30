
package GUI.Components;

import java.util.ArrayList;
import java.util.Iterator;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author Aaron
 */
public abstract class iComponent extends AbstractComponent {
    protected iComponent(GUIContext _context) 
    {
        super(_context);
    }
    protected iComponent(GUIContext _context, Vector2f _position, Vector2f _dimensions) 
    {
        super(_context);
        setDimensions(_dimensions);
        setLocalTranslation(_position);
    }
    
    private String      mName = null; //could be used for searching a hieracrchy by name
    private iComponent  mParent;
    private ArrayList<iComponent> mChildren = new ArrayList<iComponent>();
    private Vector2f    mDimensions = new Vector2f(0,0);
    private float       mLocalScale = 1.0f;
    private float       mLocalRotation = 0.0f;
    private Vector2f    mLocalTranslation = new Vector2f(0,0);
    private float       mGlobalScale = 1.0f;
    private float       mGlobalRotation = 0.0f;
    private Vector2f    mGlobalTranslation = new Vector2f(0,0);
    private Transform   mGlobalTransform = Transform.createTranslateTransform(0, 0);
    private boolean     mIsDestroyed = false;
    private boolean     mIsVisible = true;
    private Color       mColor = Color.white;
    private Shape       mShape = new Rectangle(0, 0, 1, 1);

    /*
     * -------primary functions-----------
     */
    public final boolean update(int _delta)
    {
        if(mParent == null)
        {
            updateInternal(_delta);
        }
        return false;
    }
    private boolean updateInternal(int _delta)
    {
        mGlobalScale = calcGlobalScale();
        mGlobalRotation = calcGlobalRotation();
        mGlobalTranslation = calcGlobalTranslation();
        mGlobalTransform = calcGlobalTransform();

        mShape = new Rectangle(0,0,getWidth(),getHeight());
        mShape = mShape.transform(getGlobalTransform());
        
        updateSelf(_delta);
        
        //update children
        Iterator<iComponent> itr = getChildIterator();
        while(itr.hasNext())
        {
            iComponent child = itr.next();
            child.updateInternal(_delta);
        }

        return true;
    }
    protected abstract boolean updateSelf(int _delta);
    
    public final void render(GUIContext guic, Graphics grphcs) throws SlickException
    {
        render(guic, grphcs, false);
    }
    public final void render(GUIContext guic, Graphics grphcs, boolean _debug) throws SlickException
    {
        //render only if root
        if(mParent == null)
        {
            //start render chain
            renderInternal(guic, grphcs, new Vector2f(0,0));
            if(_debug)
                renderInternalDebug(grphcs);
        }
    }
    private void renderInternal(GUIContext guic, Graphics grphcs, Vector2f _globalPos) throws SlickException
    {      
        float rot = getLocalRotation();
        float scale = getLocalScale();
        Vector2f trans = getLocalTranslation().add(_globalPos);
        Vector2f center = new Vector2f(trans.x + (getWidth() * 0.5f), trans.y + (getHeight() * 0.5f));
        
        grphcs.scale(scale, scale);
        grphcs.rotate(center.x, center.y, rot);
        {
            if(mIsVisible)
            {
                grphcs.setColor(mColor);
                renderSelf(guic, grphcs, trans);
            }

            //render children
            Iterator<iComponent> itr = getChildIterator();
            while(itr.hasNext())
            {
                iComponent child = itr.next();
                child.renderInternal(guic, grphcs, trans);
            }
        }
        grphcs.rotate(center.x, center.y, -rot);
        grphcs.scale(1/scale, 1/scale);
    }
    private final void renderInternalDebug(Graphics _graphics)
    {
        _graphics.setColor(Color.white);
        renderSelfDebug(_graphics) ;
        
        //render children
        Iterator<iComponent> itr = getChildIterator();
        while(itr.hasNext())
        {
            iComponent child = itr.next();
            child.renderInternalDebug(_graphics);
        }
    }
    protected void renderSelfDebug(Graphics _graphics)
    {
        _graphics.draw(getShape());
    }
    protected void renderSelf(GUIContext guic, Graphics grphcs, final Vector2f _globalPos) throws SlickException
    {
        grphcs.fillRect(_globalPos.x, _globalPos.y, getWidth(), getHeight());
    }
    
    /*
     * ----------various getters and setters-------------
     */
    //returns shape translated in global space
    public final Shape getShape(){return mShape;}
    public final Transform getGlobalTransform(){return mGlobalTransform;}
    private final Transform calcGlobalTransform()
    {
        float rot = getLocalRotation() / (float)(180.0f/Math.PI);
        float scale = getLocalScale();
        Vector2f trans = getLocalTranslation();
        Vector2f center = new Vector2f(trans.x + (getWidth() * 0.5f), trans.y + (getHeight() * 0.5f));
        Transform scaleT = Transform.createScaleTransform(scale, scale);
        Transform rotateT = Transform.createRotateTransform(rot, center.x, center.y);
        Transform translateT = Transform.createTranslateTransform(getX(), getY());
        Transform transform = scaleT.concatenate(rotateT).concatenate(translateT);
        if(mParent != null)
        {
            transform = mParent.getGlobalTransform().concatenate(transform);
        }
        return transform;
    }
    public final float getGlobalScale(){return mGlobalScale;}
    private final float calcGlobalScale()
    {
        float scale = mLocalScale;
        if(mParent != null)
        {
            scale *= mParent.getGlobalScale();
        }
        return scale;
    }
    public final float getGlobalRotation(){return mGlobalRotation;}
    private final float calcGlobalRotation()
    {
        float rotation = mLocalRotation;
        if(mParent != null)
        {
            rotation += mParent.getGlobalRotation();
        }
        return rotation;
    }
    public final Vector2f getGlobalTranslation(){return mGlobalTranslation;}
    private final Vector2f calcGlobalTranslation()
    {
        Vector2f translation = mLocalTranslation.copy();
        if(mParent != null)
        {
            translation = translation.add(mParent.getGlobalTranslation());
        }
        return translation;
    }
    public final float getLocalScale(){return mLocalScale;}
    public final void setLocalScale(float _scaleFactor)
    {
        mLocalScale =_scaleFactor;
    }
    public final float getLocalRotation(){return mLocalRotation;}
    public final void setLocalRotation(float _degrees)
    {
        mLocalRotation =_degrees;
    }
    public final Vector2f getLocalTranslation(){return mLocalTranslation.copy();}
    public final void setLocalTranslation(Vector2f _position)
    {
        mLocalTranslation.x = _position.x;
        mLocalTranslation.y = _position.y;
    }
    public final Vector2f getDimensions(){return mDimensions.copy();}
    public final void setDimensions(Vector2f _dimensions)
    {
        mDimensions.x = _dimensions.x;
        mDimensions.y = _dimensions.y;
    }
    public final boolean isRoot(){return mParent == null;}
    public final boolean setParent(iComponent _parent)
    {
        if(mParent == null)
        {
            mParent = _parent;
            return true;
        }
        else return false;
    }
    public final Color getColor() {return mColor;}
    public final void setColor(Color _color){mColor =  _color;}
    public final boolean isVisible(){return mIsVisible;}
    public final void setIsVisible(boolean _isVisible){mIsVisible = _isVisible;}
    
    /*
     * --------methods for child control :P-----------
     */
    //returns false if child already has a parent
    public final boolean addChild(iComponent _child)
    {            
        if(_child.setParent(this) && !_child.equals(this))
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

    @Override
    public void setAcceptingInput(boolean acceptingInput) 
    {
        super.setAcceptingInput(acceptingInput);
        for(iComponent c : mChildren)
        {
            c.setAcceptingInput(acceptingInput);
        }
    }
    
    
    /*
     * -------methods for destruction----------
     */
    public final boolean isDestroyed()
    {
        return mIsDestroyed;
    }
    public final void destroy()
    {
        mParent.removeChild(this);
        destroyInternal();
    }
    protected void destroyInternal()
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
     * ------------Overrides for AbstractComponent------------
     */
    @Override
    public void setLocation(int _x, int _y) {
        if(mLocalTranslation == null) //required due to call in parent's constructor
            mLocalTranslation = new Vector2f(0,0);
        setLocalTranslation(new Vector2f(_x,_y));
    }
    @Override
    public int getX() {
        return (int)mLocalTranslation.x;
    }
    @Override
    public int getY() {
        return (int)mLocalTranslation.y;
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
