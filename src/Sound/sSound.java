/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;

/**
 *
 * @author Graham
 */

public class sSound
{
    static float mDefaultGain = 0.5f;
    private static HashMap<String, Audio> mSounds = new HashMap();    
       
    private sSound()
    {
    }
    public static void init()
    {
        SoundStore.get().init();
    }
    public static void poll(int _delta)
    {
        SoundStore.get().poll(_delta);
    }
    protected static Audio getSound(String _soundName)
    {
        Audio audio = mSounds.get(_soundName);
        if(audio == null)
        {
            System.err.println("No such sound: " + _soundName);
            return null;
        }
        return audio;
    }
    public static void play(String _soundName)
    {
        play(_soundName, false, 1.0f, mDefaultGain);
    }
    public static void play(String _soundName, boolean _loop)
    {
        play(_soundName, _loop, 1.0f, mDefaultGain);
    }
    public static void play(String _soundName, boolean _loop, float _pitch, float _gain)
    {
        Audio audio = getSound(_soundName);
        if(audio != null)
        {
            audio.playAsSoundEffect(_pitch, _gain, _loop);
        }
    }
    public static void play3D(String _soundName, float _x, float _y, float _z)
    {
        play3D(_soundName, false, _x, _y, _z);
    }
    public static void play3D(String _soundName, boolean _loop, float _x, float _y, float _z)
    {
        play3D(_soundName, _loop, 1.0f, mDefaultGain, _x, _y, _z);
    }
    public static void play3D(String _soundName, boolean _loop, float _pitch, float _gain, float _x, float _y, float _z)
    {
        Audio audio = getSound(_soundName);
        if(audio != null)
        {
            audio.playAsSoundEffect(_pitch, _gain, _loop, _x, _y, _z);
        }
    }
    public static void playAsMusic(String _soundName, boolean _loop)
    {
        Audio audio = getSound(_soundName);
        audio.playAsMusic(1.0f, mDefaultGain, _loop);
    }   
    public static void stop(String _soundName)
    {
        Audio audio = getSound(_soundName);
        if(audio != null)
            mSounds.get(_soundName).stop();
    }
    public static void loadStream(String _soundName, String _soundFile)
    {
         Audio newAudio = null;
        try {newAudio = SoundStore.get().getOggStream(_soundFile);} 
        catch (IOException ex) {Logger.getLogger(sSound.class.getName()).log(Level.SEVERE, null, ex);}
        if(newAudio == null)
        {
            System.err.println("No such sound resource: " + _soundFile);
            return;
        }
        mSounds.put(_soundName, newAudio);
    }
    public static void loadFile(String _soundName, String _soundFile) throws SlickException
    {
        Audio newAudio = null;
        try {newAudio = SoundStore.get().getOgg(_soundFile);} 
        catch (IOException ex) {Logger.getLogger(sSound.class.getName()).log(Level.SEVERE, null, ex);}
        
        if(newAudio == null)
        {
            System.err.println("No such sound resource: " + _soundFile);
            return;
        }
        mSounds.put(_soundName, newAudio);
    }
    public static boolean isPlaying(String _soundName)
    {
        Audio audio = mSounds.get(_soundName);
        if(audio == null)
        {
            System.err.println("No such sound: " + _soundName);
            return false;
        }
        return mSounds.get(_soundName).isPlaying();
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
