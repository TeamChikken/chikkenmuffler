package com.tropicbliss.soundmuffler;

import com.tropicbliss.soundmuffler.block.ModBlockEntities;
import com.tropicbliss.soundmuffler.block.ModBlocks;
import net.fabricmc.api.ModInitializer;

public class SoundMufflerMod implements ModInitializer {
  public static final String MOD_ID = "soundmuffler";

  @Override
  public void onInitialize() {
    ModBlocks.register();
    ModBlockEntities.register();
  }
}
