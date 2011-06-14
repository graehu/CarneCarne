/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Hashtable;
import Entities.Entity;
/**
 *
 * @author alasdair
 */
public class sEntityFactory {
    
    private static Hashtable mFactories;
    private sEntityFactory()
    {
        
    }
    
    public static void init()
    {
        mFactories = new Hashtable();
        mFactories.put("Player", new PlayerFactory());
    }
    
    public static Entity create(String _factoryname, Hashtable _parameters)
    {
        iEntityFactory factory = (iEntityFactory)mFactories.get(_factoryname);
        return factory.useFactory(_parameters);
    }
    
    
}
