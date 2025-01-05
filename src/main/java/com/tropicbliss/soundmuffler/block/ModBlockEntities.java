package com.tropicbliss.soundmuffler.block;

import com.tropicbliss.soundmuffler.SoundMufflerMod;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<SoundMufflerBlockEntity> SOUND_MUFFLER;

    public static void register() {
        SOUND_MUFFLER = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new Identifier(SoundMufflerMod.MOD_ID, "sound_muffler_entity"),
                BlockEntityType.Builder.create(SoundMufflerBlockEntity::new, ModBlocks.SOUND_MUFFLER).build(null)
        );
    }
}
