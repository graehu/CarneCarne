/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Init;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author A203945
 */
public class StateInit extends BasicGameState {

    InitGUI mInitGUI;
    
    public int getID() {
        return 0;
    }

    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
//        rootPane = null;
//        super.enter(container, game);
//        
//        mInitGUI = new InitGUI((InitApp)game);
//        getRootPane().add(mInitGUI.getWidget());
    }

    
    public void init(GameContainer _gc, StateBasedGame _sbg) throws SlickException {
        //createRootPane();
    }

    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        
        //TextureImpl.bindNone();
        //TextureImpl.unbind();
        //Renderer.get().glEnable(SGL.GL_TEXTURE_2D);
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException {
        if(mInitGUI.getIsFinished())
        {
            _gc.setForceExit(false);
            _gc.exit();
        }
        else if(_sbg.closeRequested())
        {
            _gc.setForceExit(true);
        }
    }
    
}
