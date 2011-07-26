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
public class CarcassFactory implements iEntityFactory
{
    String mTextures[];
    public CarcassFactory()
    {
        mTextures = new String[Entity.CauseOfDeath.eCauseOfDeathMax.ordinal()];
        mTextures[Entity.CauseOfDeath.eSpikes.ordinal()] = "characters/bdy";
        mTextures[Entity.CauseOfDeath.eFire.ordinal()] = mTextures[Entity.CauseOfDeath.eSpikes.ordinal()];
    }
    public Entity useFactory(HashMap _parameters)
    {
        int cause = ((Entity.CauseOfDeath)_parameters.get("causeOfDeath")).ordinal();
        _parameters.put("ref", mTextures[cause]);
        iSkin skin = sSkinFactory.create("static", _parameters);
        Entity entity = new Carcass(skin);
        
        _parameters.put("userData", entity);
        entity.setBody(sWorld.useFactory("CarcassBody", _parameters));
        return entity;
    }
}
