/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package HUD;

import GUI.Components.GraphicalComponent;
import Graphics.sGraphicsManager;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    static int fps = 24;
    static int timer = 0;
    
    public Revolver(String _ref, Vector2f _position) {
        super(sGraphicsManager.getGUIContext(), _position, new Vector2f());
        try {
            mSpriteSheet = new SpriteSheet(_ref, 64,64);
        } catch (SlickException ex) {
            Logger.getLogger(Revolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        mImage = mSpriteSheet.getSprite(0,0);
        setDimentionsToImage();
    }
    public Revolver(String _ref)  {
        super(sGraphicsManager.getGUIContext());
        try {
            mSpriteSheet = new SpriteSheet(_ref, 64,64);
        } catch (SlickException ex) {
            Logger.getLogger(Revolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        mImage = mSpriteSheet.getSprite(0,0);
        setDimentionsToImage();
    }
    
    enum Ammo
    {
        eDefault,
        eMeat,
        eChilli,
        eWaterMellon,
        eGum
    }
    
    int mTargetFrame = 0;
    int mCurrentFrame = 0;
    SpriteSheet mSpriteSheet = null;
    
    public void setAmmo(Ammo _ammo)
    {
        switch(_ammo)
        {
            case eDefault:
                mTargetFrame = 0;
            case eMeat:
                mTargetFrame = 1 * framesPerAmmo;
            case eChilli:
                mTargetFrame = 2 * framesPerAmmo;
            case eWaterMellon:
                mTargetFrame = 3 * framesPerAmmo;
            case eGum:
                mTargetFrame = 4 * framesPerAmmo;              
        }
    }

    @Override
    protected boolean updateSelf(int _delta) 
    {
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
    
    
}
