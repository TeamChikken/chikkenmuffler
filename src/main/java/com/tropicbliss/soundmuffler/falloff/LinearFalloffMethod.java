package com.tropicbliss.soundmuffler.falloff;

public class LinearFalloffMethod implements IFalloffMethod {
    @Override
    public float calculate(float scale, float minVolume, float volume) {
        return minVolume + scale * (volume - minVolume); // Linear interpolation
    }
}
