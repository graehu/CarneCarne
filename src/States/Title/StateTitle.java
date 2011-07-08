/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Title;

import Events.iEvent;
import Events.iEventListener;
import Events.sEvents;
import GUI.Components.ScrollableComponent;
import Graphics.sGraphicsManager;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author a203945
 */
public class StateTitle extends BasicGameState implements iEventListener{
   // @Override
    public int getID() {
       return 2;
    }

    public void trigger(iEvent _event) {
        if(_event.getName().equals("WindowResizeEvent"))
        {
            calcUI();
        }
    }
    
    enum InternalState
    {
        eCenter,
        eLeft,
        eRight
    }
    Image mBackground = null, mForeground = null;
    ScrollableComponent mParalax0 = null;
    ScrollableComponent mParalax1 = null;
    ScrollableComponent mParalax2 = null;
    float mScale = 1.0f;
    float mOffset = 0.0f;
    InternalState mState = InternalState.eCenter;
    
    
    
    public void init(final GameContainer _gc, final StateBasedGame _sbg) throws SlickException {
        sEvents.subscribeToEvent("WindowResizeEvent", this);
        mBackground = new Image("ui/title/bg.png");
        mForeground = new Image("ui/title/fg.png");
        
        Vec2 screen = sGraphicsManager.getTrueScreenDimensions();
        Vector2f paralaxDim = new Vector2f(screen.x,400);
        Vector2f paralaxPos = new Vector2f(0,0);
        //FIXME: want this to be a child to root, need to implement scaling compoents in heirarchy
        mParalax0 = new ScrollableComponent(_gc, paralaxPos, paralaxDim);
        mParalax0.setImage("ui/title/p0.png");
        mParalax1 = new ScrollableComponent(_gc, paralaxPos, paralaxDim);
        mParalax1.setImage("ui/title/p1.png");
        mParalax2 = new ScrollableComponent(_gc, paralaxPos, paralaxDim);
        mParalax2.setImage("ui/title/p2.png");
        
        calcUI();
    }
    
    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        mBackground.draw(0, mOffset, mScale);  
        //render other stuff here
        mParalax2.render(_gc, _grphcs, true);
        mParalax1.render(_gc, _grphcs, true);
        mParalax0.render(_gc, _grphcs, true);
        mForeground.draw(0, mOffset, mScale);
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException {
        Input input = _gc.getInput();
        if(input.isKeyDown(Input.KEY_F11))
            sGraphicsManager.toggleFullscreen();  
        
        float speed = 0.001f;
        if(input.isKeyDown(Input.KEY_UP))
            mParalax0.scrollVerticalBy(-speed);
        if(input.isKeyDown(Input.KEY_DOWN))
            mParalax0.scrollVerticalBy(speed);
        if(input.isKeyDown(Input.KEY_LEFT))
            mParalax0.scrollHorizontalBy(-speed); 
        if(input.isKeyDown(Input.KEY_RIGHT))
            mParalax0.scrollHorizontalBy(speed);
        
        mParalax0.update(_i);
        mParalax1.update(_i);
        mParalax2.update(_i);
    }
    
    protected void calcUI()
    {
        //calc scale and offset values
        Vec2 screen = sGraphicsManager.getTrueScreenDimensions();
        //scale by x to fit but keep aspect ratio using offset
        mScale = screen.x/mBackground.getWidth();
        mOffset = (screen.y - mBackground.getHeight()*mScale) * 0.5f;
        
        mParalax0.setLocalScale(mScale);
        mParalax1.setLocalScale(mScale);
        mParalax2.setLocalScale(mScale);
        
        mParalax0.setLocalTranslation(new Vector2f(0,mOffset + (mBackground.getHeight()-mParalax0.getImageHeight())*mScale));
        mParalax1.setLocalTranslation(new Vector2f(0,mOffset + (mBackground.getHeight()-mParalax1.getImageHeight())*mScale));
        mParalax2.setLocalTranslation(new Vector2f(0,mOffset + (mBackground.getHeight()-mParalax2.getImageHeight())*mScale));
        
        mParalax0.setDimensions(new Vector2f(screen.x, mParalax0.getImageHeight()*mScale));
        mParalax1.setDimensions(new Vector2f(screen.x, mParalax1.getImageHeight()*mScale));
        mParalax2.setDimensions(new Vector2f(screen.x, mParalax2.getImageHeight()*mScale));
        
        
    }
    
}
