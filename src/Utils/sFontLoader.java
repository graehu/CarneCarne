/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author A203945
 */
public class sFontLoader {
    private sFontLoader(){}
    private static Map<String, TrueTypeFont> mFontList = new HashMap<String, TrueTypeFont>();
    private static Font mDefaultFont = null;
    
    public static Font getDefaultFont(){return mDefaultFont;}
    public static void setDefaultFont(String _ref)
    {
        mDefaultFont = createFont(_ref, 100);
    }
    /*
     * loads truetype font from file, prepends dir, appends ".ttf"
     */
    public static TrueTypeFont createFont(String _ref)
    {
        return createFont(_ref, 32, false, false);
    }
    public static TrueTypeFont createFont(String _ref, int _size)
    {
        return createFont(_ref, _size, false, false);
    }
    public static TrueTypeFont createFont(String _ref, int _size, boolean _bold, boolean _italic)
    {
        if(mFontList.containsKey(_ref))
        {
            return mFontList.get(_ref);
        }
        else
        {
            String dir = "ui/fonts/";
            TrueTypeFont font = null;
            try
            {
                java.awt.Font jfont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream(dir+_ref+".ttf"));
                font = new TrueTypeFont(jfont, true);
                if(font != null)
                    mFontList.put(_ref, font);
            } 
            catch (Exception ex) {Logger.getLogger(sFontLoader.class.getName()).log(Level.SEVERE, null, ex);}
            
            return font;
        }
    }
}
