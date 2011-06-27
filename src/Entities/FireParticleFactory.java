/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
class FireParticleFactory implements iEntityFactory
{

    public FireParticleFactory() {
    }

    public Entity useFactory(HashMap _parameters)
    {
        _parameters.put("ref", "ChilliSpit");
        iSkin skin = sSkinFactory.create("static", _parameters);
        Vec2 velocity = (Vec2)_parameters.get("velocity");
        FireParticle entity = new FireParticle(skin, velocity);
        _parameters.put("userData", entity);
        entity.mBody = sWorld.useFactory("FireParticleBody", _parameters);
        return entity;
    }
}
