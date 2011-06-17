/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.iSkin;
import Graphics.sSkinFactory;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
class SpatBlockFactory implements iEntityFactory{

    public SpatBlockFactory()
    {
    }

    public Entity useFactory(HashMap _parameters)
    {
        HashMap skinDef = new HashMap();
        skinDef.put("ref", "ChewedBlock");
        iSkin skin = sSkinFactory.create("static", skinDef);
        
        SpatBlock entity = new SpatBlock(skin);
        
        _parameters.put("entity", entity);
        Body body = sWorld.useFactory("SpatBlockFactory", _parameters);
        
        entity.mBody = body;
        
        return entity;
    }
    
}
