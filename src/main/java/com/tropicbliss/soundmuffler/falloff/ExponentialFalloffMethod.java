package com.tropicbliss.soundmuffler.falloff;

public class ExponentialFalloffMethod implements IFalloffMethod{
    @Override
    public float calculate(float scale, float minVolume, float volume) {
        float exponentialFactor = (float)Math.pow(scale, 2); // Exponential curve
        return minVolume + exponentialFactor * (volume - minVolume);
    }
}
