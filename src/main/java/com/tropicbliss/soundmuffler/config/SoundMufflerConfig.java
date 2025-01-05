package com.tropicbliss.soundmuffler.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tropicbliss.soundmuffler.falloff.*;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SoundMufflerConfig {
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("sound_muffler.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static SoundMufflerConfig INSTANCE;

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


    public static SoundMufflerConfig getInstance() {
        if (INSTANCE == null) {
            load();
        }

        return INSTANCE;
    }

    // Sets the current instance with another one
    public static void setInstance(SoundMufflerConfig instance) {
        INSTANCE = instance;
    }

    public static void load() {
        INSTANCE = readFile();

        if (INSTANCE == null) {
            INSTANCE = new SoundMufflerConfig();
            save();
        }
    }

    public static void save() {
        if (INSTANCE == null) {
            INSTANCE = new SoundMufflerConfig();
        }

        writeFile(INSTANCE);
    }

    @Nullable
    private static SoundMufflerConfig readFile() {
        if (!Files.isRegularFile(CONFIG_FILE))
            return null;

        try (BufferedReader reader = Files.newBufferedReader(CONFIG_FILE)) {
            return GSON.fromJson(reader, SoundMufflerConfig.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void writeFile(SoundMufflerConfig instance) {
        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_FILE)) {
            GSON.toJson(instance, writer);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
