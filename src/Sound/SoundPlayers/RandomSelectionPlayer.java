/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound.SoundPlayers;

import Sound.iSoundPlayer;
import Sound.sSound;
import java.util.Random;

/**
 *
 * @author alasdair
 */
public class RandomSelectionPlayer implements iSoundPlayer
{
    String mNames[];
    Random mRandom;

    public RandomSelectionPlayer(String _names[])
    {
        mNames = _names;
        mRandom = new Random(System.currentTimeMillis());
    }
    public RandomSelectionPlayer(String _prefix, int _size)
    {
        mNames = new String[_size];
        for (int i = 0; i < _size; i++)
        {
            mNames[i] = _prefix + i;
        }
        mRandom = new Random(System.currentTimeMillis());
    }
    
    public void play(Object _parameter)
    {
        int rand = mRandom.nextInt(mNames.length);
        sSound.play(mNames[rand], false);
    }
    public void stop(Object _parameter)
    {
        for (int i = 0; i < mNames.length; i++)
        {
            sSound.stop(mNames[i]);
        }
    }

    public boolean isPlaying()
    {
        for (int i = 0; i < mNames.length; i++)
        {
            if (sSound.isPlaying(mNames[i]))
            {
                return true;
            }
        }
        return false;
    }
    
}
