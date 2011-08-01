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
public class NullPlayer implements iSoundPlayer
{

    public void play(Object _parameter)
    {
    }

    public boolean isPlaying()
    {
        return false;
    }

    public void stop(Object _parameter)
    {
    }
    
}
