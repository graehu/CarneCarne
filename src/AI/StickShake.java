/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

/**
 *
 * @author alasdair
 */
public class StickShake
{
    private final static int shakeNumRequired = 3;
    private final static int edgeHitTimeout = 10;
    private final static int zeroValueTimeout = 10;
    int shakeCounter;
    int hitEdgeCounter;
    int zeroValueTimer;
    float lastShake;
    enum ShakeState
    {
        eNotMoving,
        eMovingLeft,
        eMovingRight,
        eHitLeft,
        eHitRight,
        eShakeStateMax
    }
    ShakeState shakeState;
    StickShake()
    {
        shakeCounter = 0;
        hitEdgeCounter = 0;
        lastShake = 0.0f;
        shakeState = ShakeState.eNotMoving;
    }
    public boolean shake(float _value) /// Returns true if the stick has been shaken
    {
        if (_value == 0.0f)
        {
            zeroValueTimer++;
            if (zeroValueTimer == zeroValueTimeout)
            {
                shakeState = ShakeState.eNotMoving;
                shakeCounter = 0;
                zeroValueTimer = 0;
            }
            return false;
        }
        else
        {
            zeroValueTimer = 0;
            if (_value > 0.5f)
            {
                if (shakeState.equals(ShakeState.eHitRight))
                {
                    hitEdgeCounter++;
                    if (hitEdgeCounter == edgeHitTimeout)
                    {
                        hitEdgeCounter = 0;
                        shakeCounter = 0;
                        shakeState = ShakeState.eNotMoving;
                    }
                }
                else
                {
                    shakeState = ShakeState.eHitRight;
                    shakeCounter++;
                    hitEdgeCounter = 0;
                }
            }
            else
            if (_value < -0.5f)
            {
                if (shakeState.equals(ShakeState.eHitLeft))
                {
                    hitEdgeCounter++;
                    if (hitEdgeCounter == edgeHitTimeout)
                    {
                        hitEdgeCounter = 0;
                        shakeCounter = 0;
                        shakeState = ShakeState.eNotMoving;
                    }
                }
                else
                {
                    shakeState = ShakeState.eHitLeft;
                    shakeCounter++;
                    hitEdgeCounter = 0;
                }
            }
            if (shakeCounter == shakeNumRequired)
            {
                shakeState = ShakeState.eNotMoving;
                shakeCounter = 0;
                return true;
            }
        }
        return false;
        /*if (_value == 0.0f)
        {
            zeroValueTimer++;
            System.err.println("Zero value");
            return false;
        }
        else
        {
            zeroValueTimer = 0;
            switch (shakeState)
            {
                case eMovingLeft:
                {
                    if (_value < lastShake)
                    {
                        if (_value < -0.8f)
                        {
                            shakeState = ShakeState.eHitLeft;
                            hitEdgeCounter = 0;
                            shakeCounter++;
                            if (shakeCounter == shakeNumRequired)
                            {
                                shakeCounter = 0;
                                shakeState = ShakeState.eNotMoving;
                                lastShake = _value;
                                return true;
                            }
                        }
                    }
                    else
                    {
                        shakeCounter = 0;
                        shakeState = ShakeState.eNotMoving;
                    }
                    break;
                }
                case eMovingRight:
                {
                    if (_value > lastShake)
                    {
                        if (_value > 0.8f)
                        {
                            shakeState = ShakeState.eHitRight;
                            hitEdgeCounter = 0;
                            shakeCounter++;
                            if (shakeCounter == shakeNumRequired)
                            {
                                shakeCounter = 0;
                                shakeState = ShakeState.eNotMoving;
                                lastShake = _value;
                                return true;
                            }
                        }
                    }
                    else
                    {
                        shakeCounter = 0;
                        shakeState = ShakeState.eNotMoving;
                    }
                    break;
                }
                case eHitLeft:
                {
                    if (lastShake > _value)
                    {
                        shakeState = ShakeState.eMovingRight;
                    }
                    else if (!(lastShake < _value))
                    {
                        hitEdgeCounter++;
                        if (hitEdgeCounter == edgeHitTimeout)
                        {
                            shakeCounter = 0;
                        }
                    }
                    break;
                }
                case eHitRight:
                {
                    if (lastShake < _value)
                    {
                        shakeState = ShakeState.eMovingLeft;
                    }
                    else if (!(lastShake > _value))
                    {
                        hitEdgeCounter++;
                        if (hitEdgeCounter == edgeHitTimeout)
                        {
                            shakeCounter = 0;
                        }
                    }
                    break;
                }
                case eNotMoving:
                {
                    if (lastShake < _value)
                    {
                        shakeState = ShakeState.eMovingLeft;
                    }
                    else if (lastShake > _value)
                    {
                        shakeState = ShakeState.eMovingRight;
                    }
                    break;
                }
            }
            lastShake = _value;
            System.err.println("State: " + shakeState + " value: " + lastShake + " shakeCounter: " + shakeCounter);
            return false;
        }*/
    }
}
