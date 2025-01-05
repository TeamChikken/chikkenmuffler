package com.tropicbliss.soundmuffler;

import com.tropicbliss.soundmuffler.block.SoundMufflerBlockEntity;
import com.tropicbliss.soundmuffler.events.handler.SoundEventHandler;
import com.tropicbliss.soundmuffler.falloff.IFalloffMethod;
import com.tropicbliss.soundmuffler.falloff.InverseSquaredFalloffMethod;
import com.tropicbliss.soundmuffler.falloff.LinearFalloffMethod;
import com.tropicbliss.soundmuffler.falloff.LogarithmicFalloffMethod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SoundMufflerClientMod implements ClientModInitializer {
  // The range at which the minimum volume should always be played
  public static final double MUFFLER_RANGE = 10;

  // The range from the muffler range at which the volume starts falling off
  public static final double FALLOFF_RANGE = 10;

  // The minimum volume the muffler should allow
  public static final float MIN_VOLUME = .0F;

  public static final IFalloffMethod FALLOFF_METHOD = new LogarithmicFalloffMethod();

  public static List<SoundMufflerBlockEntity> mufflers = new ArrayList<>();

  @Override
  public void onInitializeClient() {
    SoundEventHandler.init();
  }
}
