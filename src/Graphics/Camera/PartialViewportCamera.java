/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Camera;

import org.jbox2d.dynamics.Body;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author alasdair
 */
class PartialViewportCamera extends BodyCamera {

    public PartialViewportCamera(Body _body, Rectangle _viewPort, boolean _topSplit)
    {
        super(_body, _viewPort, _topSplit);
    }    
}
