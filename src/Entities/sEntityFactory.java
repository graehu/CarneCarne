/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.HashMap;
/**
 *
 * @author alasdair
 */
public class sEntityFactory {
    
    private static HashMap<String, iEntityFactory> mFactories;
    private sEntityFactory()
    {
        
    }
    
    public static void init()
    {
        mFactories = new HashMap();
        mFactories.put("Player", new PlayerFactory());
        mFactories.put("SpatBlock", new SpatBlockFactory());
        mFactories.put("Zombie", new ZombieFactory());
        mFactories.put("Carrot", new CarrotFactory());
        mFactories.put("CaveIn", new CaveInFactory());
        mFactories.put("CaveInTileFactory", new CaveInTileFactory());
        mFactories.put("SeeSaw", new SeeSawFactory());
        mFactories.put("MovingPlatform", new MovingPlatformFactory());
    }
    
    public static Entity create(String _factoryname, HashMap _parameters)
    {
        iEntityFactory factory = (iEntityFactory)mFactories.get(_factoryname);
        return factory.useFactory(_parameters);
    }
    
    
}
