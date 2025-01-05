package com.tropicbliss.soundmuffler;

import com.tropicbliss.soundmuffler.block.SoundMufflerBlockEntity;
import com.tropicbliss.soundmuffler.config.SoundMufflerConfig;
import com.tropicbliss.soundmuffler.events.handler.SoundEventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SoundMufflerClientMod implements ClientModInitializer {
  public static List<SoundMufflerBlockEntity> mufflers = new ArrayList<>();

  @Override
  public void onInitializeClient() {
    SoundMufflerConfig.load();
    SoundEventHandler.init();
  }
}
