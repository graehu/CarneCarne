/*
 * Factory class that encapsulates the recycling of entities
 * deals in interfaces, internally handles entities.
 * Factory params:
 * static:      "ref" (data path e.g "character/1.tmx") - prefixes with "data"
 *              --
 * animated:    "ref" (data path e.g "character/1") - prefixes with "data", appends with .png 
 *              "x1" - starting xPos (Integer)
 *              "y1" - starting yPos (Integer)
 *              "x2" - starting xPos (Integer)
 *              "y2" - starting yPos (Integer)
 *              "hScan" - horizontal animation? (boolean)
 *              "duration" - frame duration in ms (Integer)
 * character:   "ref" (data path e.g "character/1") - prefixes with "data", appends with .def
 *              "anims" (list of animations in the set)
 *              "duration"  - frame duration in ms (Integer)
 */

package Graphics;

import java.util.HashMap;
import org.newdawn.slick.SlickException;

/**
 *
 * @author aaron
 */

public class sSkinFactory {
    
    private static HashMap mFactories;
    private sSkinFactory()
    {
    }
    public static void init()
    {
        mFactories = new HashMap();
        mFactories.put("static", new StaticSkinFactory());
        mFactories.put("animated", new AnimatedSkinFactory());
        mFactories.put("character", new CharacterSkinFactory());
    }
    public static iSkin create(String _factoryName, HashMap _params)
    {
        return create(_factoryName, _params, false);
    }
    public static iSkin create(String _factoryName, HashMap _params, boolean _isAutoRendered)
    {
        try
        {
            iSkinFactory factory = (iSkinFactory)mFactories.get(_factoryName);
            iSkin skin = factory.useFactory(_params);
            return skin;
        }
        catch(SlickException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
