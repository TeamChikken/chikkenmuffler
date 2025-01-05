// Adapted from https://github.com/MorningSage/ExtremeSoundMuffler-Fabric/blob/master/src/main/java/morningsage/extremesoundmuffler/events/handlers/SoundEventHandler.java by tropicbliss on 22/12/2021

package com.tropicbliss.soundmuffler.events.handler;

import com.tropicbliss.soundmuffler.SoundMufflerClientMod;
import com.tropicbliss.soundmuffler.block.SoundMufflerBlockEntity;
import com.tropicbliss.soundmuffler.events.SoundPlayingEvents;
import net.minecraft.client.MinecraftClient;

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
      if (muffler == null || muffler.isRemoved() || !muffler.hasWorld()) {
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

    if (closestMuffler == null) {
      return;
    }

    float scale = (float)((Math.sqrt(closestDistance) - SoundMufflerClientMod.MUFFLER_RANGE) / SoundMufflerClientMod.FALLOFF_RANGE);

    // Player within base range? Then set volume to min
    if (scale < 0) {
      soundInfo.setVolume(SoundMufflerClientMod.MIN_VOLUME);
      return;
    }

    // Player too far, no muffling applicable
    if (scale > 1) {
      return;
    }

    soundInfo.setVolume(SoundMufflerClientMod.FALLOFF_METHOD.calculate(scale, SoundMufflerClientMod.MIN_VOLUME, soundInfo.getVolume()));
  }
}
