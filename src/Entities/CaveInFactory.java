/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import World.sWorld;
import java.util.HashMap;

/**
 *
 * @author alasdair
 */
class CaveInFactory implements iEntityFactory
{
    public CaveInFactory() {
    }

    public Entity useFactory(HashMap _parameters)
    {
        iSkin skin = sSkinFactory.create("tiled", _parameters, false);
        CaveIn entity = new CaveIn(skin);
        _parameters.put("entity", entity);
        entity.mBody = sWorld.useFactory("TileArrayFactory", _parameters);
        return entity;
    }
    
}
