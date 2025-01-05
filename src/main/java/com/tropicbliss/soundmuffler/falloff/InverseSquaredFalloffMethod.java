package com.tropicbliss.soundmuffler.falloff;

public class InverseSquaredFalloffMethod implements IFalloffMethod {
    @Override
    public float calculate(float scale, float minVolume, float volume) {
        float inverseSquaredFactor = 1 / ((scale * scale) + 1); // Avoid division by zero
        float normalized = 1 - inverseSquaredFactor;            // Inverse to make it decrease
        return minVolume + normalized * (volume - minVolume);
    }
}
