/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import GUI.TWL.BasicTWLGameState;
import GUI.TWL.TWLStateBasedGame;
import States.Init.StateInit;
import java.net.URL;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Aaron
 */
public class InitApp extends TWLStateBasedGame{
    
    private BasicTWLGameState mInitState;
    static protected int mChosenX = 1680;
    static protected int mChosenY = 1050;
    
    public void setChosenResolution(int _x, int _y)
    {
        mChosenX = _x;
        mChosenY = _y;
    }
    
    public InitApp()
    {
        super("CarneCarne: Settings");
    }
    
    @Override
    protected URL getThemeURL() {
        //boolean does = ResourceLoader.resourceExists("data/ui/simple.xml");
        URL magic = Thread.currentThread().getContextClassLoader().getResource("ui/simple.xml");
        return magic;
    }
    
    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        //mInitState = new StateInit();
        //addState(mInitState);
    }
    
}
