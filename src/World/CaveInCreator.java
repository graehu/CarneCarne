/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entities.CaveIn;
import Entities.sEntityFactory;
import java.util.HashMap;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.newdawn.slick.Image;

/**
 *
 * @author alasdair
 */
public class CaveInCreator
{
    CaveIn entity;
    public CaveInCreator()
    {
        HashMap parameters = new HashMap<String, Object>();
        entity = (CaveIn)sEntityFactory.create("CaveIn", parameters);
    }
    
    public void addFixture(int _xTile, int _yTile, Image _image)
    {
        FixtureDef def = new FixtureDef();
        def.density = 1.0f;
        PolygonShape shape = new PolygonShape();
        def.shape = shape;
        shape.setAsBox(0.5f, 0.5f, new Vec2(_xTile, _yTile), 0.0f);
        entity.mBody.createFixture(def);
        
        //entity.mSkin; // build the data first I think
    }
    
    public void finish()
    {
        
    }
}
