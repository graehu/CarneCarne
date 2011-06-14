/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.HashMap;
import Entities.Entity;
/**
 *
 * @author alasdair
 */
public class sEntityFactory {
    
    private static HashMap mFactories;
    private sEntityFactory()
    {
        
    }
    
    public static void init()
    {
        mFactories = new HashMap();
        mFactories.put("Player", new PlayerFactory());
    }
    
    public static Entity create(String _factoryname, HashMap _parameters)
    {
        iEntityFactory factory = (iEntityFactory)mFactories.get(_factoryname);
        return factory.useFactory(_parameters);
    }
    
    
}
