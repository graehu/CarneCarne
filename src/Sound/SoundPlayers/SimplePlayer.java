/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound.SoundPlayers;

import Sound.iSoundPlayer;
import Sound.sSound;

/**
 *
 * @author alasdair
 */
public class SimplePlayer implements iSoundPlayer
{
    String mName;
    public SimplePlayer(String _name)
    {
        mName = _name;
    }
    public void play(Object _parameter)
    {
        sSound.play(mName, false);
    }
    public void stop(Object _parameter)
    {
        sSound.stop(mName);
    }

    public boolean isPlaying()
    {
        return sSound.isPlaying(mName);
    }
    
}
