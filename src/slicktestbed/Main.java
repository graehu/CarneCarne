
package slicktestbed;

import org.newdawn.slick.*;
import Game.Game;

public class Main extends BasicGame
{
    public Main()
    {
        super("SlickTestbed");
    }
    public static void main(String[] arguments)
    {
        //setup native libs
        NativeLibLoader nativeLibLoader = new NativeLibLoader("org.lwjgl.librarypath");
        nativeLibLoader.init();
        nativeLibLoader.setupPath();
        try
        {
            AppGameContainer app = new AppGameContainer(new Main());
            app.setDisplayMode(800, 600, false);
            //app.setTargetFrameRate(60);
            app.setVSync(true);
            app.start();
        }
        catch(SlickException e)
        {
            e.printStackTrace();
        }
    }
    Game mGame;
    
    public void init(GameContainer container) throws SlickException
    {
        mGame = new Game();
    }

    public void update (GameContainer _container, int _delta) throws SlickException
    {
        mGame.update(_container, _delta);
    }

    public void render(GameContainer container, Graphics g) throws SlickException
    {
        mGame.render(container);
    }

}
