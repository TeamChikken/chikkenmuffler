package com.tropicbliss.soundmuffler.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = "soundmuffler")
@Config(name = "soundmuffler_config", wrapperName = "SoundMufflerConfig")
@Sync(Option.SyncMode.OVERRIDE_CLIENT) // Send config from server to client
public class SoundMufflerConfigModel {
    @PredicateConstraint("positive")
    public float minMinVolume = 0f;

    @PredicateConstraint("positive")
    public float maxMinVolume = 1f;

    @PredicateConstraint("positive")
    public double minMufflerRange = 0d;

    @PredicateConstraint("positive")
    public double maxMufflerRange = 7d;

    @PredicateConstraint("positive")
    public double minFalloffRange = 0d;

    @PredicateConstraint("positive")
    public double maxFalloffRange = 10d;

    public static boolean positive(double value) {
        return value >= 0d;
    }

    public static boolean positive(float value) {
        return value >= 0f;
    }
}