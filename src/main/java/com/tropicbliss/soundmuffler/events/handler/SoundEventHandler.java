package com.tropicbliss.soundmuffler.events.handler;

import com.tropicbliss.soundmuffler.SoundMufflerClientMod;
import com.tropicbliss.soundmuffler.block.SoundMufflerBlockEntity;
import com.tropicbliss.soundmuffler.events.SoundPlayingEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.state.property.Properties;

public class SoundEventHandler {

  private static final MinecraftClient client = MinecraftClient.getInstance();

  public static void init() {
    SoundPlayingEvents.SOUND_VOLUME_EVENT.register(SoundEventHandler::getSoundVolume);
  }

  private static void getSoundVolume(SoundPlayingEvents.SoundInfo soundInfo) {
    if (client.world == null) {
      return;
    }

    SoundMufflerBlockEntity closestMuffler = null;
    double closestDistance = Double.MAX_VALUE;

    // Find the closest muffler to the audio source
    for (int i = SoundMufflerClientMod.mufflers.size() -1; i >= 0; i--) {
      SoundMufflerBlockEntity muffler = SoundMufflerClientMod.mufflers.get(i);

      // Check if the muffler is valid
      if (muffler == null || muffler.isRemoved() || !muffler.hasWorld() || muffler.getWorld() == null || muffler.getWorld().getBlockState(muffler.getPos()).get(Properties.POWERED)) {
        SoundMufflerClientMod.mufflers.remove(muffler);
        continue;
      }

      // Check if the muffler is closer
      double distance = muffler.getPos().getSquaredDistance(soundInfo.getX(), soundInfo.getY(), soundInfo.getZ());
      if (distance < closestDistance) {
        closestMuffler = muffler;
        closestDistance = distance;
      }
    }

    // If no muffler was found, skip applying any modifications
    if (closestMuffler == null) {
      return;
    }

    float scale = (float)((Math.sqrt(closestDistance) - closestMuffler.getMufflerRange()) / closestMuffler.getFalloffRange());

    // Audio source too far, no muffling applicable
    if (scale > 1) {
      return;
    }

    // Audio source within base range? Then set volume to min
    if (scale < 0) {
      soundInfo.setVolume(closestMuffler.getMinVolume());
      return;
    }

    // Set volume to result of falloff method
    soundInfo.setVolume(closestMuffler.getFalloffMethod().getFalloffMethod().calculate(scale, closestMuffler.getMinVolume(), soundInfo.getVolume()));
  }
}
