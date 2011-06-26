/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import java.util.HashMap;
import org.jbox2d.dynamics.Body;

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
        entity.mBody = (Body)_parameters.get("body");
        return entity;
    }
    
}
