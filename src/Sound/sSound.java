/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound;

import java.util.HashMap;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.openal.SoundStore;

/**
 *
 * @author Graham
 */

public class sSound
{
    
    private static HashMap mSounds;    
       
    private sSound()
    {
    }
    public static void init()
    { 
        mSounds = new HashMap();
    }
    public static void play(String _soundName)
    {
        SoundData data = ((SoundData)mSounds.get(_soundName));
        Sound sound = data.getSound();
        sound.playAt(data.getPitch(), data.getVolume(), data.getPosition().x, data.getPosition().y, 0);
    }
    public static void stop(String _soundName)
    {
        SoundData data = ((SoundData)mSounds.get(_soundName));
        data.getSound().stop();        
    }
    public static void setPosition(String _soundName, Vec2 _position)
    {
        SoundData data = ((SoundData)mSounds.get(_soundName));
        data.setPosition(_position);
    }
    public static void setPitch(String _soundName, float _pitch)
    {
        SoundData data = ((SoundData)mSounds.get(_soundName));
        data.setPitch(_pitch);
    }
    public static void setVolume(String _sound, float _volume)
    {
        SoundData data = ((SoundData)mSounds.get(_sound));
        data.setVolume(_volume);
    }
    public static void setLooping(String _soundName, boolean _looping)
    {
        SoundData data = ((SoundData)mSounds.get(_soundName));
        data.setLooping(_looping);
    }
    
    public static void loadSound(String _soundName, String _soundFile) throws SlickException
    {
        SoundData newSound = new SoundData(_soundFile);
        mSounds.put(_soundName, newSound);
    }
    public static boolean isPlaying(String _soundName)
    {
        SoundData data = ((SoundData)mSounds.get(_soundName));
        return data.getSound().playing();
    }
    
    public static void setSFXVolume(float _volume)
    {
        SoundStore.get().setSoundVolume(_volume);
    }
        public static void setMusicVolume(float _volume)
    {
        SoundStore.get().setMusicVolume(_volume);
    }
    
    public static void muteSFX(boolean _mute)
    {
        SoundStore.get().setSoundsOn(_mute);
    }
    
    public static void muteMusic(boolean _mute)
    {
        SoundStore.get().setMusicOn(_mute);
    }
    
}
