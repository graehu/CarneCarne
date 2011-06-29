/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package States.Init;

import de.matthiasmann.twl.ComboBox;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.ListModel;
import de.matthiasmann.twl.model.SimpleChangableListModel;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.InitApp;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


/**
 *
 * @author a203945
 */
public class InitGUI{
    
    static final int[] WINDOW_RESOLUTIONS = {800,600,
                                             1280,1024};
    private ComboBox mResolutionComboBox = null;
    private InitApp mSbg;
    SortedSet<ModeModel> mModes = new TreeSet<ModeModel>();
    private boolean mIsFinished = false;
    public InitGUI(InitApp _sbg)
    {
        //get supported display modes
        DisplayMode desktopMode = Display.getDesktopDisplayMode();
        try {
            for(DisplayMode dm : Display.getAvailableDisplayModes()) {
                if( dm.getBitsPerPixel() == desktopMode.getBitsPerPixel() && /*FIXME hardcoded value*/
                    dm.isFullscreenCapable() &&
                    dm.getFrequency() == desktopMode.getFrequency() /*FIXME hardcoded value*/) {
                    mModes.add(new ModeModel(dm));
                }
            }
        } catch (LWJGLException ex) {
            Logger.getLogger(InitGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //hold onto game instance
        mSbg = _sbg;
        ListModel lResolutions = new SimpleChangableListModel<ModeModel>(mModes);
        mResolutionComboBox = new ComboBox<SimpleChangableListModel>(lResolutions);
        mResolutionComboBox.addCallback(new ComboCallback());
        mResolutionComboBox.setPosition(0,200);
        mResolutionComboBox.setSize(200, 50);    
    }
    public Widget getWidget()
    {
        return mResolutionComboBox;
    }
    public boolean getIsFinished()
    {
        return mIsFinished;
    }
    class ModeModel implements Comparable<ModeModel> 
    {
        DisplayMode mDisplayMode;
        int mWidth, mHeight;
        ModeModel(DisplayMode _dm)
        {
            mDisplayMode = _dm;
            mWidth = _dm.getWidth();
            mHeight = _dm.getHeight();
        }
        @Override
        public String toString()
        {
            return mWidth+"x"+mHeight;
        }

        public int compareTo(ModeModel o) {
            //sort by width
            if(o.mWidth > mWidth)
                return 1;
            else if(o.mWidth < mWidth)
                return -1;
            else return 0;
        }
        
    }
    class ComboCallback implements Runnable {
        public void run() {
            int selected = mResolutionComboBox.getSelected();
            ModeModel mode = (ModeModel)mResolutionComboBox.getModel().getEntry(selected);            
                      
            mSbg.setChosenResolution(mode.mWidth, mode.mHeight);
            mIsFinished = true;
        }
    }
    
    
}