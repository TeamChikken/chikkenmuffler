package com.tropicbliss.soundmuffler.block;

import com.tropicbliss.soundmuffler.SoundMufflerClientMod;
import com.tropicbliss.soundmuffler.SoundMufflerMod;
import com.tropicbliss.soundmuffler.falloff.FalloffMethod;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

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

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
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
        markToUpdate();
    }

    public void setMufflerRange(double range) {
        mufflerRange = Math.max(Math.min(range, SoundMufflerMod.CONFIG.maxMufflerRange()), SoundMufflerMod.CONFIG.minMufflerRange());
        markToUpdate();
    }

    public void setFalloffRange(double range) {
        falloffRange = Math.max(Math.min(range, SoundMufflerMod.CONFIG.maxFalloffRange()), SoundMufflerMod.CONFIG.minFalloffRange());
        markToUpdate();
    }

    public void setFalloffMethod(FalloffMethod method) {
        falloffMethod = method;
        markToUpdate();
    }

    // Present to prevent sending too many packets
    public void setAll(float volume, double range, double falloffRange, FalloffMethod method) {
        this.minVolume = volume;
        this.mufflerRange = range;
        this.falloffRange = falloffRange;
        this.falloffMethod = method;

        markToUpdate();
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

    public void markToUpdate() {
        markDirty();

        if (this.getWorld() != null) {
            this.getWorld().updateListeners(pos, getCachedState(), getCachedState(), 0);
        }
    }
}
