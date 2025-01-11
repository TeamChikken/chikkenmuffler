package com.tropicbliss.soundmuffler.network;

import com.tropicbliss.soundmuffler.SoundMufflerMod;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SoundMufflerNetworkingConstants {
    @NotNull
    public static final Identifier MODIFY_MUFFLER_PACKET_ID = Objects.requireNonNull(Identifier.of(SoundMufflerMod.MOD_ID, "modify_muffler"));
}
