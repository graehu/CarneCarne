
package main;

import Events.sEvents;
import GUI.TWL.BasicTWLGameState;
import GUI.TWL.TWLStateBasedGame;
import Graphics.sGraphicsManager;
import Input.sInput;
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
//            InitApp initApp = new InitApp();
//            AppGameContainer init = new AppGameContainer(initApp);
//            init.setDisplayMode(800, 600, false);
//            init.setVSync(true);
//            init.setForceExit(false);
//            init.start();
//            init.destroy();
//            init = null;
            
            AppGameContainer app = new AppGameContainer(new Main());
            //app.setMouseGrabbed(true);
            app.setDisplayMode(800, 600, false);
            app.setVSync(true);
            app.setTargetFrameRate(60);
            app.setMouseGrabbed(true);
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
    public void initStatesList(GameContainer _gc) throws SlickException {  
        //_gc.setDefaultFont(sFontLoader.createFont("default"));                
        sGraphicsManager.init((AppGameContainer)_gc);
        sInput.init(_gc); 
        sEvents.init();
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
