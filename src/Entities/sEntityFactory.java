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
        mFactories.put("Zombie", new PeaFactory());
        mFactories.put("Carrot", new CarrotFactory());
        mFactories.put("Broccoli", new BroccoliFactory());
        mFactories.put("Pea", new PeaFactory());
        mFactories.put("CaveIn", new CaveInFactory());
        mFactories.put("CaveInTileFactory", new CaveInTileFactory());
        mFactories.put("SeeSaw", new SeeSawFactory());
        mFactories.put("MovingPlatform", new MovingPlatformFactory());
        mFactories.put("FireParticle", new FireParticleFactory());
        mFactories.put("Explosion", new ExplosionFactory());
        mFactories.put("Carcass", new CarcassFactory());
    }
    
    public static Entity create(String _factoryname, HashMap _parameters)
    {
        iEntityFactory factory = (iEntityFactory)mFactories.get(_factoryname);
        return factory.useFactory(_parameters);
    }
    
    
}
