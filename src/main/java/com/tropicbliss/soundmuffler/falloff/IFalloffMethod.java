package com.tropicbliss.soundmuffler.falloff;

@FunctionalInterface
public interface IFalloffMethod {
    float calculate(float scale, float minVolume, float volume);
}
