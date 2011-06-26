/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AI.StupidPlatformController;
import AI.iPlatformController;
import Graphics.Skins.iSkin;
import Graphics.Skins.sSkinFactory;
import World.sWorld;
import java.util.HashMap;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author alasdair
 */
class MovingPlatformFactory implements iEntityFactory
{

    public MovingPlatformFactory() {
    }

    public Entity useFactory(HashMap _parameters)
    {
        iSkin skin = sSkinFactory.create("static", _parameters);
        iPlatformController controller = null;
        String platformType = (String)_parameters.get("Type");
        if (platformType.equals("StupidPlatform"))
        {
            controller = new StupidPlatformController();
        }
        MovingPlatform entity = new MovingPlatform(skin, controller);
        _parameters.put("userData", entity);
        Body body = sWorld.useFactory("MovingPlatformBodyFactory", _parameters);
        entity.mBody = body;
        return entity;
    }
    
}
