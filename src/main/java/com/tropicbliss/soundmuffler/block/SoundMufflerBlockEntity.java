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
        // Get the current client
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        // Get the current client player
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        // Return if they aren't in the same dimension
        if (!player.getWorld().getRegistryKey().equals(world.getRegistryKey())) return;

        // Calculate the distance between the player and the block
        double distance = pos.getSquaredDistance(player.getPos());

        // Get the closest muffler if it exists
        SoundMufflerBlockEntity closestMuffler = SoundMufflerClientMod.ClosestMuffler;

        // No closest muffler? Well then set the new closest muffler
        if (closestMuffler == null) {
            SoundMufflerClientMod.ClosestMuffler = entity;
            SoundMufflerClientMod.ClosestRange = Math.sqrt(distance);
            return;
        }

        // Get the distance to the closest muffler
        double closestDistance = SoundMufflerClientMod.ClosestMuffler.getPos().getSquaredDistance(player.getPos());

        // Set the closest muffler range to the distance if it's closer
        // TODO: replace with in-audio play distance fetching
        if (distance < closestDistance || entity.getPos() == SoundMufflerClientMod.ClosestMuffler.getPos()) {
            SoundMufflerClientMod.ClosestMuffler = entity;
            SoundMufflerClientMod.ClosestRange = Math.sqrt(distance);
        }
    }
}
