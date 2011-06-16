
package main;

import GUI.BasicTWLGameState;
import GUI.TWLStateBasedGame;
import java.net.URL;
import org.newdawn.slick.*;
import States.Game.StateGame;
import org.newdawn.slick.util.ResourceLoader;



public class Main extends TWLStateBasedGame
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
            app.setVSync(true);
            app.start();
        }
        catch(SlickException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected URL getThemeURL() {
        //boolean does = ResourceLoader.resourceExists("data/ui/simple.xml");
        URL magic = ResourceLoader.getResource("data/ui/simple.xml");
        return magic;
    }

    BasicTWLGameState mSplashState, mTitleState, mGameState;
    
    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        //Splash
        //mSplashState = new BasicTWLGameState();
        //title
        //mTitleState = new BasicTWLGameState();
        //game
        mGameState = new StateGame();
        addState(mGameState);        
    }

}
