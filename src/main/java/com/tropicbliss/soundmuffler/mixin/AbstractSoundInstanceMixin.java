package com.tropicbliss.soundmuffler.mixin;

import com.tropicbliss.soundmuffler.events.SoundPlayingEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.Sound;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(AbstractSoundInstance.class)
public class AbstractSoundInstanceMixin {
    @Shadow protected Sound sound;

    @Shadow protected float volume;

    @Shadow protected double x;
    @Shadow protected double y;
    @Shadow protected double z;

    @Shadow protected Random field_38800;

    @Inject(method = "getVolume()F", at = @At("HEAD"), cancellable = true)
    private void getVolumeMixin(CallbackInfoReturnable<Float> cir) {
        SoundPlayingEvents.SoundInfo soundInfo = new SoundPlayingEvents.SoundInfo(volume, x, y, z);
        SoundPlayingEvents.SOUND_VOLUME_EVENT.invoker().onSoundVolume(soundInfo);
        cir.setReturnValue(soundInfo.getVolume() * this.sound.getVolume().get(this.field_38800));
    }
}
