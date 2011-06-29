
package main;

import GUI.TWL.BasicTWLGameState;
import GUI.TWL.TWLStateBasedGame;
import Graphics.sGraphicsManager;
import java.net.URL;
import org.newdawn.slick.*;
import States.Game.StateGame;
import States.Menu.StateMenu;
import States.Splash.StateSplash;
import States.Title.StateTitle;

public class Main extends TWLStateBasedGame
{
    public Main()
    {
        super("CarneCarne!");
    }
    public static void main(String[] arguments)
    {
        //setup native libs
        //-Djava.library.path=lib/natives-win32
        NativeLibLoader nativeLibLoader = new NativeLibLoader("org.lwjgl.librarypath");
        nativeLibLoader.init();
        nativeLibLoader.setupPath();
        NativeLibLoader nativeLibLoaderJinput = new NativeLibLoader("net.java.games.input.librarypath");
        nativeLibLoaderJinput.init();
        nativeLibLoaderJinput.setupPath();
        try
        {
            InitApp initApp = new InitApp();
            AppGameContainer init = new AppGameContainer(initApp);
            init.setDisplayMode(800, 600, false);
            init.setVSync(true);
            init.setForceExit(false);
            init.start();
            init.destroy();
            
            AppGameContainer app = new AppGameContainer(new Main());
            //app.setMouseGrabbed(true);
            app.setDisplayMode(InitApp.mChosenX, InitApp.mChosenY, false);
            app.setVSync(true);
            app.setTargetFrameRate(60);
            
            //initialise graphics manager
            sGraphicsManager.init(InitApp.mChosenX, InitApp.mChosenY);
            
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

    BasicTWLGameState mSplashState, mTitleState, mGameState, mMenuState;
    
    @Override
    public void initStatesList(GameContainer gc) throws SlickException {   
        
        //Splash: state1
        mSplashState = new StateSplash();
        addState(mSplashState);
        //title: state2
        mTitleState = new StateTitle();
        addState(mTitleState);
        //game: state3
        mGameState = new StateGame();
        addState(mGameState); 
        //menu: state4
        mMenuState = new StateMenu();
        addState(mMenuState); 
        
        //FIXME: should start on splash
        //enterState(2, null, new BlobbyTransition(new Color(0,0,0)));
        enterState(3, null, null);
    }

    
    

}
