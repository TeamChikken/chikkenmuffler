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
      if (muffler == null || muffler.isRemoved()) {
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

    float linearPercentage = (float)(Math.sqrt(closestDistance) / SoundMufflerClientMod.MUFFLER_RANGE);

    // Player too far, no muffling applicable
    if (linearPercentage > 1) {
      return;
    }

    float volume = soundInfo.getVolume();

    // Clamp to valid range [MIN_VOLUME, 1]
    linearPercentage = (float)Math.max(SoundMufflerClientMod.MIN_VOLUME, Math.min(volume * linearPercentage, 1.0F));

    soundInfo.setVolume(linearPercentage);
  }
}
