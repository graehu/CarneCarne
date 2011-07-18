/*
 * 
 */
package Graphics.Skins;

import Graphics.sGraphicsManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Aaron
 */
public class CharacterSkin implements iSkin
{
    static public class CharacterSubSkin
    {
        public CharacterSubSkin(String _ref, SubType _type, int _tileWidth, int _tileHeight)
        {
            this(_ref, _type, _tileWidth, _tileHeight, new Vec2(), 41);
        }
        public CharacterSubSkin(String _ref, SubType _type, int _tileWidth, int _tileHeight, Vec2 _offset)
        {
            this(_ref, _type, _tileWidth, _tileHeight, _offset, 41);
        }
        public CharacterSubSkin(String _ref, SubType _type, int _tileWidth, int _tileHeight, Vec2 _offset, int _duration)
        {
            mRef = _ref;
            mType= _type;
            mTileWidth = _tileWidth;
            mTileHeight = _tileHeight;
            mOffset = _offset;
            mDuration = _duration;
        }
        public enum SubType
        {
            e32Dir,
            eAnimated,
            eStatic
        }
        SubType mType;
        String mRef;
        int mTileWidth = 0, mTileHeight = 0;
        int mDuration = 0;
        Vec2 mOffset = null;
    }
    
    CharacterSkin(List<CharacterSubSkin> _subSkins) throws SlickException
    {
        for(CharacterSubSkin subSkin : _subSkins)
        {
            String absoluteRef = "assets/characters/" + subSkin.mRef + ".png";
            switch(subSkin.mType)
            {
                case e32Dir:
                {
                    SpriteSheet ss = new SpriteSheet(absoluteRef, subSkin.mTileWidth, subSkin.mTileHeight);
                    //ensure 18x2 tiles
                    int reqWidth = 18;
                    int reqHeight = 2;
                    if(ss.getHorizontalCount() != reqWidth || ss.getVerticalCount() != reqHeight)
                    {
                        System.err.println(absoluteRef + " MUST BE " + reqWidth + "x" + reqHeight + " TILES");
                        System.exit(1);
                    }
                    for(int y = 0; y < reqHeight; y++)
                    {
                        for(int x = 0; x < reqWidth; x++)
                        {
                            String name = subSkin.mRef + "_" + mDirections.get(x + (y*reqWidth));
                            iSkin skin = new StaticSkin(ss.getSprite(x, y));
                            addSubSkin(skin, name, subSkin.mOffset);
                        }
                    }
                    break;
                }
                case eAnimated:
                {
                    SpriteSheet ss = new SpriteSheet(absoluteRef, subSkin.mTileWidth, subSkin.mTileHeight);
                    iSkin skin = new AnimatedSkin(ss, subSkin.mDuration);
                    addSubSkin(skin, subSkin.mRef, subSkin.mOffset);
                    break;
                }
                case eStatic:
                {
                    Image image = new Image(absoluteRef);
                    iSkin skin = new StaticSkin(image);
                    addSubSkin(skin, subSkin.mRef, subSkin.mOffset);
                    break;
                }
            }
        }
    }
    
    static ArrayList<String> mDirections = /*S->W->N->E->S*/
            new ArrayList(Arrays.asList("s","ssbw", "sbw","ssw","swbs","sw","swbw","wsw","wbs",
                                        "w","wbn","wnw","nwbw","nw","nwbn","nnw","nbw", "nnbw",
                                        "n","nnbe", "nbe","nne","nebn","ne","nebe","ene","ebn", 
                                        "e","ebs","ese","sebe","se","sebs","sse","sbe", "ssbe"));
    
    List<iSkin> mSubSkins = new ArrayList<iSkin>();
    List<Vec2> mOffsets = new ArrayList<Vec2>();
    HashMap<String, Integer> mSkinNames = new HashMap<String, Integer>();
    Queue<Integer> mCurrentSkins = new PriorityQueue<Integer>();
    
    float mRotation = 0;
    
    
    protected final void addSubSkin(iSkin _skin, String _name, Vec2 _offset)
    {
        int arrayPos = mSubSkins.size();
        mSubSkins.add(_skin);
        mSkinNames.put(_name, arrayPos);
        mOffsets.add(_offset);
    }
    
    public void render(float _x, float _y)
    {
        for(Integer i : mCurrentSkins)
        {
            mSubSkins.get(i).render(_x + mOffsets.get(i).x, _y + mOffsets.get(i).y);
        }
    }
    
    public void setRotation(float _radians)
    {
        for(iSkin subSkin : mSubSkins)
        {
            subSkin.setRotation(_radians);
        }
    }

    public final float activateSubSkin(String _animation, boolean _isLooping, float _speed) {    
        if(mSkinNames.containsKey(_animation))
        {
            int ref = mSkinNames.get(_animation);
            if(mCurrentSkins.contains(ref))//return if already activated
                return 0;
            mCurrentSkins.add(ref);
            
            iSkin skin = mSubSkins.get(ref);
            skin.restart();
            skin.setIsLooping(_isLooping);
            skin.setSpeed(_speed);
            
            return skin.getDuration();
        }
        else return 0;
    }

    public final void deactivateSubSkin(String _animation) {
        mCurrentSkins.remove(mSkinNames.get(_animation));
    }
    
    public void restart() {
        //do nothing
    }
    
    public void setRotation(String _subSkin, float _radians)
    {
        if(mSkinNames.containsKey(_subSkin))
            mSubSkins.get(mSkinNames.get(_subSkin)).setRotation(_radians);
    }
    
    public void setDimentions(String _subSkin, float _w, float _h) {
        if(mSkinNames.containsKey(_subSkin))
            mSubSkins.get(mSkinNames.get(_subSkin)).setDimentions(_w, _h);
    }
    
    public void setOffset(String _subSkin, Vec2 _offset) {
            Integer ref = mSkinNames.get(_subSkin);
            if(ref != null)
                mOffsets.set(ref, _offset);
    }
    public Vec2 getOffset(String _subSkin) {
        Integer ref = mSkinNames.get(_subSkin);
        if(ref != null)
            return mOffsets.get(ref);
        else return null;
    }
    
    public void setAlpha(float _alpha) {
        for(iSkin subSkin : mSubSkins)
        {
            subSkin.setAlpha(_alpha);
        }
    }

    public void setIsLooping(boolean _isLooping) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSpeed(float _speed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public float getDuration() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDimentions(float _w, float _h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

}
