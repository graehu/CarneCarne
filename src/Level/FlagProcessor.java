/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

import AI.sPathFinding;
import Entities.Entity;
import Entities.sEntityFactory;
import World.sWorld;
import java.util.HashMap;
import java.util.Stack;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author alasdair
 */
public class FlagProcessor
{
    private Stack<Vec2> playerPositions;
    FlagProcessor(TiledMap _tiledMap)
    {
        int width = _tiledMap.getWidth();
        int height = _tiledMap.getHeight();
        int layerIndex = _tiledMap.getLayerIndex("Flags");
        playerPositions = new Stack<Vec2>();
        HashMap parameters = new HashMap();
        
        for (int i = 0; i < width; i++)
        {
            for (int ii = 0; ii < height; ii++)
            {
                int id = _tiledMap.getTileId(i, ii, layerIndex);
                String spawn = _tiledMap.getTileProperty(id, "Spawn", "None");
                if (spawn.equals("Broccoli"))
                {
                    parameters.put("position",new Vec2(i,ii));
                    sEntityFactory.create("Broccoli",parameters);
                }
                else if(spawn.equals("Carrot"))
                {
                    parameters.put("position",new Vec2(i,ii));
                    sEntityFactory.create("Carrot",parameters);
                    
                }
                else if(spawn.equals("Pea"))
                {
                    parameters.put("position",new Vec2(i,ii));
                    sEntityFactory.create("Pea",parameters);
                }
                else if (spawn.equals("Player"))
                {
                    playerPositions.push(new Vec2(i,ii));
                }
                else if (spawn.equals("CheckPoint"))
                {
                    parameters.put("position",new Vec2(i,ii));
                    sWorld.useFactory("CheckPointFactory", parameters);
                }
                else if (spawn.equals("SeeSaw"))
                {
                    Vec2 dimensions = new Vec2(0,0);
                    dimensions.x = new Float(_tiledMap.getTileProperty(id, "Width", "192.0"));
                    dimensions.y = new Float(_tiledMap.getTileProperty(id, "Height", "64.0"));
                    dimensions = dimensions.mul(1.0f/64.0f);
                    parameters.put("dimensions",dimensions);
                    parameters.put("ref",_tiledMap.getTileProperty(id, "Image","Error, image not defined"));
                    parameters.put("position",new Vec2(i,ii));
                    sEntityFactory.create("SeeSaw", parameters);
                }
                else if (spawn.equals("Platform"))
                {
                    Vec2 dimensions = new Vec2(0,0);
                    dimensions.x = new Float(_tiledMap.getTileProperty(id, "Width", "3.0"));
                    dimensions.y = new Float(_tiledMap.getTileProperty(id, "Height", "1.0"));  
                    parameters.put("dimensions",dimensions);   
                    parameters.put("ref",_tiledMap.getTileProperty(id, "Image","Error, image not defined")); 
                    parameters.put("position",new Vec2(i,ii));
                    parameters.put("Type",_tiledMap.getTileProperty(id, "Type", "Error, platform type not defined"));
                    sEntityFactory.create("MovingPlatform", parameters);              
                }
            }
        }
        while (!playerPositions.isEmpty())
        {
            parameters.put("position", playerPositions.pop());
            Entity player = sEntityFactory.create("Player",parameters);
            sPathFinding.addPlayer(player);
        }
    }
}
