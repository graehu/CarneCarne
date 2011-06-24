/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Level;

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
                if (spawn.equals("AI"))
                {
                    parameters.put("position",new Vec2(i,ii));
                    sEntityFactory.create("Zombie",parameters);                    
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
            }
        }
        while (!playerPositions.isEmpty())
        {
            parameters.put("position", playerPositions.pop());
            sEntityFactory.create("Player",parameters);
        }
    }
}
