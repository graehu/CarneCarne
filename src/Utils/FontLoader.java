/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.BufferedInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author A203945
 */
public class FontLoader {
    private FontLoader()
    {
        
    }
    /*
     * loads truetype font from file, prepends dir, appends ".ttf"
     */
    public static Font load(String _ref)
    {
        String dir = "ui/fonts/";
        java.awt.Font baseFont = null;
        try 
        {
            BufferedInputStream buffer = new BufferedInputStream(ResourceLoader.getResourceAsStream(dir+_ref+".ttf"));
            baseFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,buffer);
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(FontLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        baseFont = baseFont.deriveFont(java.awt.Font.PLAIN, 32);
        return new TrueTypeFont(baseFont, true);
    }
}
