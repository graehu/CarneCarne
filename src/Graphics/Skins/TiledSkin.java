/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Skins;

import Entities.CaveIn;
import Entities.CaveIn.Tile;
import Graphics.sGraphicsManager;
import World.sWorld;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;

/**
 *
 * @author alasdair
 */
public class TiledSkin implements iSkin
{
    ArrayList<CaveIn.Tile> mTiles;
    public TiledSkin(ArrayList<Tile> _tiles)
    {
        mTiles = _tiles;
    }
    public void render(float _x, float _y)
    {
        for (CaveIn.Tile tile: mTiles)
        {
            Vec2 position = tile.mPosition.clone();
            position.x += _x;
            position.y += _y;
            Vec2 pixelPosition = sWorld.translateToWorld(position);
            tile.mImage.draw(pixelPosition.x, pixelPosition.y);
        }
    }

    public void setRotation(float _radians)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    
    
    public void setDimentions(float _w, float _h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public float getDuration() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void restart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setIsLooping(boolean _isLooping) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSpeed(float _speed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setRotation(String _animation, float _radians) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public float startAnim(String _animation, boolean _isLooping, float _speed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stopAnim(String _animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void restart(String _animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDimentions(String _animation, float _w, float _h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setOffset(String _animation, Vec2 _offset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vec2 getOffset(String _animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
