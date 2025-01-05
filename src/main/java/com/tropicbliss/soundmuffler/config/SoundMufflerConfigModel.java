package com.tropicbliss.soundmuffler.config;

import com.tropicbliss.soundmuffler.falloff.*;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@Modmenu(modId = "soundmuffler")
@Config(name = "soundmuffler_config", wrapperName = "SoundMufflerConfig")
public class SoundMufflerConfigModel {
    public float minVolume = 0;
    public double mufflerRange = 7;
    public double falloffRange = 10;

    public FalloffMethod falloffMethod = FalloffMethod.LINEAR;

    public enum FalloffMethod {
        LINEAR(new LinearFalloffMethod()),
        EXPONENTIAL(new ExponentialFalloffMethod()),
        LOGARITHMIC(new LogarithmicFalloffMethod()),
        INVERSE_SQUARED(new InverseSquaredFalloffMethod());

        private final IFalloffMethod falloffMethod;

        FalloffMethod(IFalloffMethod falloffMethod) {
            this.falloffMethod = falloffMethod;
        }

        public IFalloffMethod getFalloffMethod() {
            return falloffMethod;
        }
    }
}
