package com.tropicbliss.soundmuffler.block;

import com.tropicbliss.soundmuffler.SoundMufflerClientMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoundMufflerBlockEntity extends BlockEntity {
    public SoundMufflerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOUND_MUFFLER, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, SoundMufflerBlockEntity entity) {
        // If the muffler is already in the list, ignore
        if (SoundMufflerClientMod.mufflers.contains(entity)) return;

        // Get the current client
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        // Get the current client player
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        // Return if they aren't in the same dimension
        if (!player.getWorld().getRegistryKey().equals(world.getRegistryKey())) return;

        SoundMufflerClientMod.mufflers.add(entity);
    }
}
