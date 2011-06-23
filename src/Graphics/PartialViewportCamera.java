/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import org.jbox2d.common.Vec2;
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
    public void resize(Rectangle _viewPort)
    {
        mViewPort = _viewPort;
    }
    protected void calculatePosition()
    {
        mPosition = mBody.getPosition();
        mPosition = mPosition.add(new Vec2(1.0f,1.0f));
        Vec2 s = sGraphicsManager.getTrueScreenDimensions();
        s.x = (mViewPort.getMaxX()- mViewPort.getX());
        s.y = (mViewPort.getMaxY()- mViewPort.getY());
        mTranslation = new Vec2(( (s.x/2)/64.0f) + 1.0f, ((s.y/2)/64.0f) + 1.0f);
        if (mPosition.x < mTranslation.x)
        {
            mTranslation.x -= ((mTranslation.x)-mPosition.x);
        }
        if (mPosition.y < mTranslation.y)
        {
            mTranslation.y -= ((mTranslation.y)-mPosition.y);
        }
        mTranslation = mTranslation.mul(64.0f);
        sGraphicsManager.setScreenDimensions(s);
    }
    
}
