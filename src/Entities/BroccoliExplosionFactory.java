/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import World.sWorld;
import java.util.HashMap;

/**
 *
 * @author alasdair
 */
public class BroccoliExplosionFactory implements iEntityFactory
{

    public Entity useFactory(HashMap _parameters)
    {
        Entity entity = new BroccoliExplosion(null, (Integer)_parameters.get("duration"));
        _parameters.put("userData", entity);
        entity.setBody(sWorld.useFactory("BroccoliExplosionBody", _parameters));
        return entity;
    }
    
}
