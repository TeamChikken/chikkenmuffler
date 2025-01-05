// Adapted from https://github.com/MorningSage/ExtremeSoundMuffler-Fabric/blob/master/src/main/java/morningsage/extremesoundmuffler/events/handlers/SoundEventHandler.java by tropicbliss on 22/12/2021

package com.tropicbliss.soundmuffler.events.handler;

import com.tropicbliss.soundmuffler.SoundMufflerClientMod;
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

    // No blocks set
    if (SoundMufflerClientMod.ClosestMuffler == null) {
      return;
    }

    // Muffler was removed
    if (SoundMufflerClientMod.ClosestMuffler.isRemoved()) {
      SoundMufflerClientMod.ClosestMuffler = null;
      return;
    }

    float linearPercentage = (float)(SoundMufflerClientMod.ClosestRange / SoundMufflerClientMod.MUFFLER_RANGE);

    // Player too far, no muffling applicable
    if (linearPercentage > 1) {
      return;
    }

    float volume = soundInfo.getVolume();

    // Clamp to valid range [MIN_VOLUME, 1]
    linearPercentage = (float)Math.max(SoundMufflerClientMod.MIN_VOLUME, Math.min(volume * linearPercentage, 1.0F));

    // TODO: Muffle any sounds PLAYED near the block too, such that players can be far away from the block, but nearby sounds still get muffled
    soundInfo.setVolume(linearPercentage);
  }
}
