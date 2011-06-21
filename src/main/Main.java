
package main;

import GUI.TWL.BasicTWLGameState;
import GUI.TWL.TWLStateBasedGame;
import java.net.URL;
import org.newdawn.slick.*;
import States.Game.StateGame;
import States.Splash.StateSplash;
import States.Title.StateTitle;

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
        //nativeLibLoader.setupPath();
        NativeLibLoader nativeLibLoaderJinput = new NativeLibLoader("java.library.path");
        nativeLibLoaderJinput.init();
       // nativeLibLoaderJinput.setupPath();
        try
        {
            AppGameContainer app = new AppGameContainer(new Main());
            app.setDisplayMode(800, 600, false);
            //app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), true);
            app.setVSync(true);
            app.setTargetFrameRate(60);
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
        URL magic = Thread.currentThread().getContextClassLoader().getResource("ui/simple.xml");
        return magic;
    }

    BasicTWLGameState mSplashState, mTitleState, mGameState;
    
    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        //Splash
        mSplashState = new StateSplash();
        addState(mSplashState);
        //title
        mTitleState = new StateTitle();
        addState(mTitleState);
        //game
        mGameState = new StateGame();
        addState(mGameState); 
        //FIXME: should start on splash
        //enterState(2, null, new BlobbyTransition(new Color(0,0,0)));
        enterState(2, null, null);
    }

}
