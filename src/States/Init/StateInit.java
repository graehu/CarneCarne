/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Init;

import GUI.TWL.BasicTWLGameState;
import main.InitApp;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author A203945
 */
public class StateInit extends BasicTWLGameState {

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        rootPane = null;
        super.enter(container, game);
        
        OptionsGUI wid = new OptionsGUI((InitApp)game);
        getRootPane().add(wid.getWidget());
    }

    
    public void init(GameContainer _gc, StateBasedGame _sbg) throws SlickException {
        createRootPane();
    }

    public void render(GameContainer _gc, StateBasedGame _sbg, Graphics _grphcs) throws SlickException {
        TextureImpl.bindNone();
        TextureImpl.unbind();
        Renderer.get().glEnable(SGL.GL_TEXTURE_2D);
    }

    public void update(GameContainer _gc, StateBasedGame _sbg, int _i) throws SlickException {
    }
    
}
