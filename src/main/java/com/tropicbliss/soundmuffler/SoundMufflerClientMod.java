package com.tropicbliss.soundmuffler;

import com.tropicbliss.soundmuffler.block.SoundMufflerBlockEntity;
import com.tropicbliss.soundmuffler.events.handler.SoundEventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SoundMufflerClientMod implements ClientModInitializer {
  public static final double MUFFLER_RANGE = 20;
  public static final double MIN_VOLUME = .0F;

  // Lazy global for the closest muffler...
  public static List<SoundMufflerBlockEntity> mufflers = new ArrayList<>();

  @Override
  public void onInitializeClient() {
    SoundEventHandler.init();
  }
}
