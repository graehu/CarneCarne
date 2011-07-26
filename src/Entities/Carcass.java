/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Graphics.Skins.iSkin;

/**
 *
 * @author alasdair
 */
public class Carcass extends Entity
{
    int mTimer;
    public Carcass(iSkin _skin)
    {
        super(_skin);
        mTimer = 0;
    }
    @Override
    public void update()
    {
    }
    
}
