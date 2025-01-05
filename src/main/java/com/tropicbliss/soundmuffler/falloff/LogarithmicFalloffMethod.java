package com.tropicbliss.soundmuffler.falloff;

public class LogarithmicFalloffMethod implements IFalloffMethod {
    @Override
    public float calculate(float scale, float minVolume, float volume) {
        float logFactor = (float) Math.log1p(scale * 9); // log1p(x) = log(1 + x)
        float normalized = logFactor / (float) Math.log(10); // Normalize to range [0, 1]
        return minVolume + normalized * (volume - minVolume);
    }
}
