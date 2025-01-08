package com.tropicbliss.soundmuffler.block;

import com.tropicbliss.soundmuffler.SoundMufflerClientMod;
import com.tropicbliss.soundmuffler.SoundMufflerMod;
import com.tropicbliss.soundmuffler.falloff.FalloffMethod;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoundMufflerBlockEntity extends BlockEntity {
    private float minVolume = 0;
    private double mufflerRange = 7;
    private double falloffRange = 10;

    public FalloffMethod falloffMethod = FalloffMethod.LINEAR;

    public SoundMufflerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOUND_MUFFLER, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putFloat("minVolume", minVolume);
        nbt.putDouble("mufflerRange", mufflerRange);
        nbt.putDouble("falloffRange", falloffRange);
        nbt.putString("falloffMethod", falloffMethod.toString());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        minVolume = nbt.getFloat("minVolume");
        mufflerRange = nbt.getDouble("mufflerRange");
        falloffRange = nbt.getDouble("falloffRange");

        // Can go wrong in case string has been modified to invalid value, thus change to default
        try {
            falloffMethod = FalloffMethod.valueOf(nbt.getString("falloffMethod"));
        } catch (Exception e) {
            falloffMethod = FalloffMethod.LINEAR;
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, SoundMufflerBlockEntity entity) {
        // If the muffler is already in the list, ignore
        if (SoundMufflerClientMod.mufflers.contains(entity) || world.getBlockState(pos).get(Properties.POWERED)) return;

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

    public void setMinVolume(float volume) {
        minVolume = Math.max(Math.min(volume, SoundMufflerMod.CONFIG.maxMinVolume()), SoundMufflerMod.CONFIG.minMinVolume());
        markDirty();
    }

    public void setMufflerRange(double range) {
        mufflerRange = Math.max(Math.min(range, SoundMufflerMod.CONFIG.maxMufflerRange()), SoundMufflerMod.CONFIG.minMufflerRange());
        markDirty();
    }

    public void setFalloffRange(double range) {
        falloffRange = Math.max(Math.min(range, SoundMufflerMod.CONFIG.maxFalloffRange()), SoundMufflerMod.CONFIG.minFalloffRange());
        markDirty();
    }

    public void setFalloffMethod(FalloffMethod method) {
        falloffMethod = method;
        markDirty();
    }

    public float getMinVolume() {
        return minVolume;
    }

    public double getFalloffRange() {
        return falloffRange;
    }

    public double getMufflerRange() {
        return mufflerRange;
    }

    public FalloffMethod getFalloffMethod() {
        return falloffMethod;
    }
}
