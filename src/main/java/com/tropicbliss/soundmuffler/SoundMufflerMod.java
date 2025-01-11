package com.tropicbliss.soundmuffler;

import com.tropicbliss.soundmuffler.block.ModBlockEntities;
import com.tropicbliss.soundmuffler.block.ModBlocks;
import com.tropicbliss.soundmuffler.block.SoundMufflerBlockEntity;
import com.tropicbliss.soundmuffler.config.SoundMufflerConfig;
import com.tropicbliss.soundmuffler.falloff.FalloffMethod;
import com.tropicbliss.soundmuffler.network.SoundMufflerNetworkingConstants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class SoundMufflerMod implements ModInitializer {
  public static final String MOD_ID = "soundmuffler";

  public static final SoundMufflerConfig CONFIG = SoundMufflerConfig.createAndLoad();

  @Override
  public void onInitialize() {
    ModBlocks.register();
    ModBlockEntities.register();

    // Handle incoming network packet for modifying a muffler
    ServerPlayNetworking.registerGlobalReceiver(SoundMufflerNetworkingConstants.MODIFY_MUFFLER_PACKET_ID, (server, player, handler, buf, responseSender) -> {
      try {
        // TODO: properly define a packet
        BlockPos pos = buf.readBlockPos();
        float volume = buf.readFloat();
        double range = buf.readDouble();
        double falloffRange = buf.readDouble();
        FalloffMethod method = FalloffMethod.valueOf(buf.readString().trim().toUpperCase());

        // TODO: move this handling logic elsewhere?
        BlockEntity be = player.getWorld().getWorldChunk(pos).getBlockEntity(pos);

        // No muffler? Player lied (◕`╭╮´◕) (or something else happen lel), also player must be in range
        if (be instanceof SoundMufflerBlockEntity muffler && player.canModifyAt(player.getWorld(), be.getPos()) && Math.sqrt(be.getPos().getSquaredDistance(player.getPos())) < 64) {
          muffler.setAll(volume, range, falloffRange, method);
        }
      } catch (Exception e) {
        // TODO: properly log this with own logging system
        System.out.println(e);
      }
    });
  }
}
