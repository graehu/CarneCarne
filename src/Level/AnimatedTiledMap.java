/*
 * This class extends the tiledMap class to support animated tile
 * Assumes the there is a layer called "Animations"
 * Assumes horizontal animations
 * Animations are set but adding the propeties:
 *   animRef: ref   -- "bob"    (name of animation on the spritesheet)
 *   duration: t    -- 300      (frame duration in milliseconds)
 *   maxRandOffsetX: -- 29      (limit to random offset in X (pixels))
 *   maxRandOffsetY: -- 56      (limit to random offset in Y (pixels))
 */  

package Level;

import java.util.ArrayList;
import java.util.Random;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Animation;
import org.newdawn.slick.PackedSpriteSheet;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

/**
 * @author Aaron
 */
public class AnimatedTiledMap extends TiledMap{
    
    private ArrayList<Animation> mAnimatedObjects;
    private ArrayList<Vec2> mAnimationOffsets;
    private PackedSpriteSheet mTileMapAnimations;
    private boolean mIsLoaded = false;
    
    AnimatedTiledMap(String mMapRef) throws SlickException
    {
        //construct parent
        super(mMapRef);
        
        //initialise member variables
        mAnimatedObjects = new ArrayList<Animation>();
        mAnimationOffsets = new ArrayList<Vec2>();
        
    }
    
    public void initAnimationlayer(String _packedAnimSheetRef) throws SlickException
    {
        if(mIsLoaded == false)
        {
            mIsLoaded = true;
            
            Random rand = new Random();
            
            //load packed sprite sheet
            mTileMapAnimations = new PackedSpriteSheet(_packedAnimSheetRef);
            if(mTileMapAnimations == null)
                return;

            //iterate through animations layer
            int layerIndex = getLayerIndex("Animations");
            //if(layerIndex == -1)
                //return;
            for(int x = 0; x < getWidth(); x++)
            {
                for(int y = 0; y < getHeight(); y++)
                {
                    //get tileID
                    int tileID = getTileId(x, y, layerIndex);

                    String property;

                    //get animation name
                    property = getTileProperty(tileID, "animRef", null);
                    if(property == null)
                        continue;
                    String animRef = property;

                    //get duration
                    property = getTileProperty(tileID, "duration", null);
                    if(property == null)
                        continue;
                    int duration = Integer.parseInt(property);
                   
                    property = getTileProperty(tileID, "maxTimeOffset", null);
                    if(property == null)
                        continue;
                    int maxTimeOffset = Integer.parseInt(property);

                    //no fail - create animation
                    SpriteSheet sheet = mTileMapAnimations.getSpriteSheet(animRef);
                    Animation anim = new Animation(sheet,duration);
                    
                    //ofset start of animation
                    anim.update(rand.nextInt(maxTimeOffset));
                    
                    //randomise speed slightly
                    float randSpeed = rand.nextFloat()%1.25f;
                    anim.setSpeed(Math.max(0.75f, randSpeed));
                    
                    mAnimationOffsets.add(new Vec2( x * anim.getWidth(),
                                                    y * anim.getHeight()));
                    mAnimatedObjects.add(anim);
                }
            }
        }
    }
    
     /*
     * _x:      x translation
     * _y:      y translation
     * _width:  width of screen (pixels)
     * _height: height of screen (pixels)
     */
    public void renderAnimatedLayer(float _x, float _y, float _width, float _height)
    {
        //assumes lists of equal length
        Animation anim;
        Vec2 offset;
        for(int i = 0; i < mAnimatedObjects.size(); i++)
        {
            anim = mAnimatedObjects.get(i);
            offset = mAnimationOffsets.get(i);
            
            float xPos = offset.x + _x;
            float yPos = offset.y + _y;
            
            if( xPos > 0 && xPos < _width &&
                yPos > 0 && yPos < _height)
                anim.draw(_x + offset.x, _y + offset.y);
        }
    }
}
