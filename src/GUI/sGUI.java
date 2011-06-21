/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GUI.TWL.RootPane;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.SimpleDialog;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.SlickException;

/**
 *
 * @author a203945
 */
public class sGUI {
    static RootPane mRootPane = null;
    
    private sGUI()
    {
        
    }
    
    public static void createDialogueBox(int _x, int _y, String _msg)
    {
        createDialogueBox(mRootPane, _x, _y, _msg);
    }
    
    public static ScrollPane createDialogueBox(RootPane _rootPane, int _x, int _y, String _msg)
    {
        mRootPane = _rootPane;
        if(mRootPane != null)
        {
            ScrollPane scrollPane = new ScrollPane();
            _rootPane.add(scrollPane);
            return scrollPane;
        }
        else
        {
            return null;
        }
    }
    
    public static void setRootPane(RootPane _rootPane)    
    {
        mRootPane = _rootPane;
    }
}
