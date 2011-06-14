/*
 * Factory class that encapsulates the recycling of entities
 * deals in interfaces, internally handles entities.
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
    }
    public static iSkin create(String _factoryName)
    {
        try
        {
            iSkinFactory factory = (iSkinFactory)mFactories.get(_factoryName);
            return factory.useFactory();
        }
        catch(SlickException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
