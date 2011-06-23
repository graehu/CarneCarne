/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Graphics.sGraphicsManager;
import World.sWorld;
import de.matthiasmann.twl.ComboBox;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.ListModel;
import de.matthiasmann.twl.model.SimpleChangableListModel;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author a203945
 */
public class OptionsGUI{
    
    static final int[] WINDOW_RESOLUTIONS = {800,600,
                                             1280,1024};
    private ComboBox mResolutionComboBox = null;
    private StateBasedGame mSbg;
    public OptionsGUI(StateBasedGame _sbg)
    {
        //hold onto game instance
        mSbg = _sbg;
        ArrayList list = new ArrayList<ModeModel>();
        for(int i = 0; i < WINDOW_RESOLUTIONS.length; i+=2)
        {
            list.add(new ModeModel(WINDOW_RESOLUTIONS[i],WINDOW_RESOLUTIONS[i+1]));
        }
        ListModel lResolutions = new SimpleChangableListModel<ModeModel>(list);
        mResolutionComboBox = new ComboBox<SimpleChangableListModel>(lResolutions);
        mResolutionComboBox.addCallback(new ComboCallback());
        mResolutionComboBox.setPosition(0,200);
        mResolutionComboBox.setSize(200, 50);    
    }
    public Widget getWidget()
    {
        return mResolutionComboBox;
    }
    class ModeModel 
    {
        int mWidth, mHeight;
        ModeModel(int _width, int _height)
        {
            mWidth = _width;
            mHeight = _height;
        }
        @Override
        public String toString()
        {
            return mWidth+"x"+mHeight;
        }
        
    }
    class ComboCallback implements Runnable {
        public void run() {
            
            int selected = mResolutionComboBox.getSelected();
            ModeModel mode = (ModeModel)mResolutionComboBox.getModel().getEntry(selected);
            AppGameContainer app = (AppGameContainer)mSbg.getContainer();
            try {app.setDisplayMode(mode.mWidth, mode.mHeight, false);}
            catch (SlickException ex) {Logger.getLogger(OptionsGUI.class.getName()).log(Level.SEVERE, null, ex);}
            sGraphicsManager.setScreenDimensions(new Vec2(mode.mWidth, mode.mHeight));
            //FIXME: NEED TO ADJUST GUI RESOLUTION SOMEHOW
            sWorld.resizeViewport(new Rectangle(0,0,mode.mWidth, mode.mHeight));
            
            
        }
    }
}