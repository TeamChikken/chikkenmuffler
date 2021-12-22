package com.tropicbliss.soundmuffler;

import com.tropicbliss.soundmuffler.events.handler.SoundEventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SoundMufflerClientMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    SoundEventHandler.init();
  }
}
