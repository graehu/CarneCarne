/*
 * Factory class that encapsulates the recycling of entities
 * deals in interfaces, internally handles entities.
 * Factory params:
 * static:      "ref" (data path e.g "level/1.tmx") - prefixes with "data"
 *              --
 * animated:    "ref" (data path e.g "level/1.tmx") - prefixes with "data"
 *              "x1" - starting xPos (Integer)
 *              "y1" - starting yPos (Integer)
 *              "x2" - starting xPos (Integer)
 *              "y2" - starting yPos (Integer)
 *              "hScan" - horizontal animation? (boolean)
 *              "duration" - frame duration in ms (Integer)
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
    }
    public static iSkin create(String _factoryName, HashMap _params)
    {
        try
        {
            iSkinFactory factory = (iSkinFactory)mFactories.get(_factoryName);
            return factory.useFactory(_params);
        }
        catch(SlickException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
