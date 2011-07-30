/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.HUD;

import GUI.Components.GraphicalComponent;
import Graphics.sGraphicsManager;
import Level.sLevel.TileType;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author a203945
 */
public class Revolver extends GraphicalComponent
{
    static int framesPerAmmo = 2;
    static int AmmoCount = 6;
    static int fps = 18;
    static int timer = 0;
    static int defaultAmmo = 5 * framesPerAmmo;
    
    public Revolver(String _ref, Vector2f _position) {
        super(sGraphicsManager.getGUIContext(), _position, new Vector2f());
        try {
            mSpriteSheet = new SpriteSheet(_ref, 190,181);
        } catch (SlickException ex) {
            Logger.getLogger(Revolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        mSpriteSheet.setFilter(Image.FILTER_LINEAR); //for improved scaling
        mImage = mSpriteSheet.getSprite(0,0);
        setDimentionsToImage();
    }
    public Revolver(String _ref)  {
        super(sGraphicsManager.getGUIContext());
        try {
            mSpriteSheet = new SpriteSheet(_ref, 190,181);
        } catch (SlickException ex) {
            Logger.getLogger(Revolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        mSpriteSheet.setFilter(Image.FILTER_LINEAR); //for improved scaling
        mImage = mSpriteSheet.getSprite(0,0);
        setDimentionsToImage();
    }
    
    int mTargetFrame = defaultAmmo;
    int mCurrentFrame = defaultAmmo;
    SpriteSheet mSpriteSheet = null;
    
    public void setAmmo(TileType _type)
    {
        switch(_type)
        {
            case eEdible:
                mTargetFrame = 0 * framesPerAmmo;
                break;
            case eMelonFlesh:
                mTargetFrame = 2 * framesPerAmmo;
                break;
            case eGum:
                mTargetFrame = 1 * framesPerAmmo;            
                break;
            case eChilli:
                mTargetFrame = 4 * framesPerAmmo;
                break;
            default:
            case eTileTypesMax:
                mTargetFrame = defaultAmmo;
                break;            
        }
    }

    @Override
    protected boolean updateSelf(int _delta) 
    {
        setLocalScale(1);
        //rotate the sprite relative to current position etc
        if(mCurrentFrame != mTargetFrame)
        {
            timer += _delta;
            if(timer > 1000/fps)
            {
                timer = 0;
                mCurrentFrame++;
                if(mCurrentFrame == framesPerAmmo*AmmoCount)
                    mCurrentFrame = 0;
            }
        }
        mImage = mSpriteSheet.getSprite(mCurrentFrame, 0);
        return super.updateSelf(_delta);
    }

    @Override
    protected void renderSelf(GUIContext guic, Graphics grphcs, Vector2f _globalPos) throws SlickException 
    {
        super.renderSelf(guic, grphcs, _globalPos);
    }
    
    
    
    
    
}
