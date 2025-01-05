package com.tropicbliss.soundmuffler.block;

import com.tropicbliss.soundmuffler.SoundMufflerMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

  public static final SoundMufflerBlock SOUND_MUFFLER = new SoundMufflerBlock(
      FabricBlockSettings.copyOf(Blocks.WHITE_WOOL));

  public static void register() {
    Registry.register(Registry.BLOCK, new Identifier(SoundMufflerMod.MOD_ID, "sound_muffler"),
        SOUND_MUFFLER);
    Registry.register(Registry.ITEM, new Identifier(SoundMufflerMod.MOD_ID, "sound_muffler"),
        new BlockItem(SOUND_MUFFLER, new FabricItemSettings().group(ItemGroup.MISC)));
  }
}
