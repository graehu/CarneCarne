/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound.SoundPlayers;

import Sound.iSoundPlayer;

/**
 *
 * @author alasdair
 */
public class SinglePlayBlocker implements iSoundPlayer
{
    iSoundPlayer mPlayer;
    public SinglePlayBlocker (iSoundPlayer _player)
    {
        mPlayer = _player;
    }
    public void play(Object _parameter)
    {
        if (!mPlayer.isPlaying())
        {
            mPlayer.play(_parameter);
        }
    }
    public void stop(Object _parameter)
    {
        mPlayer.stop(_parameter);
    }

    public boolean isPlaying()
    {
        return mPlayer.isPlaying();
    }
    
}
