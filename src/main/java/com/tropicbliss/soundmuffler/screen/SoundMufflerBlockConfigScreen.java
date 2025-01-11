package com.tropicbliss.soundmuffler.screen;

import com.tropicbliss.soundmuffler.SoundMufflerMod;
import com.tropicbliss.soundmuffler.block.SoundMufflerBlockEntity;
import com.tropicbliss.soundmuffler.falloff.FalloffMethod;
import com.tropicbliss.soundmuffler.network.SoundMufflerNetworkingConstants;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;

public class SoundMufflerBlockConfigScreen extends BaseUIModelScreen<FlowLayout> {
    private final SoundMufflerBlockEntity soundMufflerBlockEntity;

    public SoundMufflerBlockConfigScreen(SoundMufflerBlockEntity soundMufflerBlockEntity) {
        super(FlowLayout.class, DataSource.asset(new Identifier(SoundMufflerMod.MOD_ID, "soundmuffler_ui_model")));
        this.soundMufflerBlockEntity = soundMufflerBlockEntity;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        TextBoxComponent volumeComponent = rootComponent.childById(TextBoxComponent.class, "min-volume-text-box");
        TextBoxComponent rangeComponent = rootComponent.childById(TextBoxComponent.class, "range-text-box");
        TextBoxComponent falloffRangeComponent = rootComponent.childById(TextBoxComponent.class, "falloff-range-text-box");

        ButtonComponent falloffMethodButton = rootComponent.childById(ButtonComponent.class, "falloff-method-button");
        ButtonComponent cancelButton = rootComponent.childById(ButtonComponent.class, "button-cancel");
        ButtonComponent doneButton = rootComponent.childById(ButtonComponent.class, "button-done");

        // None can be null, of course
        assert volumeComponent != null;
        assert rangeComponent != null;
        assert falloffRangeComponent != null;
        assert falloffMethodButton != null;
        assert cancelButton != null;
        assert doneButton != null;

        // Set the values based on the block entities
        volumeComponent.text(String.valueOf(soundMufflerBlockEntity.getMinVolume())).setTextPredicate(this::volumePredicate);
        rangeComponent.text(String.valueOf(soundMufflerBlockEntity.getMufflerRange())).setTextPredicate(this::rangePredicate);
        falloffRangeComponent.text(String.valueOf(soundMufflerBlockEntity.getFalloffRange())).setTextPredicate(this::falloffRangePredicate);

        falloffMethodButton.setMessage(Text.of(soundMufflerBlockEntity.falloffMethod.toString()));

        falloffMethodButton.onPress(button -> {
            FalloffMethod currentMethod = FalloffMethod.valueOf(button.getMessage().getString().trim().toUpperCase());

            FalloffMethod[] methods = FalloffMethod.values();
            int nextOrdinal = (currentMethod.ordinal() + 1) % methods.length;
            FalloffMethod nextMethod = methods[nextOrdinal];

            button.setMessage(Text.of(nextMethod.toString()));
        });

        cancelButton.onPress(button -> {
            close();
        });

        doneButton.onPress(button -> {
            // Sends the modification packet
            PacketByteBuf buf = PacketByteBufs.create();

            // Put the data in the packet
            buf.writeBlockPos(soundMufflerBlockEntity.getPos());                  // Write the position of the block entity we want to modify
            buf.writeFloat(Float.parseFloat(volumeComponent.getText()));          // Write the volume
            buf.writeDouble(Double.parseDouble(rangeComponent.getText()));        // Write the range
            buf.writeDouble(Double.parseDouble(falloffRangeComponent.getText())); // Write the falloff range

            buf.writeString(falloffMethodButton.getMessage().getString());        // Write method

            ClientPlayNetworking.send(SoundMufflerNetworkingConstants.MODIFY_MUFFLER_PACKET_ID, buf);
            close();
        });
    }

    public boolean rangePredicate(String value) {
        if (value == null || value.trim().isEmpty()) {
            value = "0";
        }

        try {
            double v = Double.parseDouble(value);
            return !(v > SoundMufflerMod.CONFIG.maxMufflerRange()) && !(v < SoundMufflerMod.CONFIG.minMufflerRange());
        } catch(Exception e) {
            return false;
        }
    }

    public boolean falloffRangePredicate(String value) {
        if (value == null || value.trim().isEmpty()) {
            value = "0";
        }

        try {
            double v = Double.parseDouble(value);
            return !(v > SoundMufflerMod.CONFIG.maxFalloffRange()) && !(v < SoundMufflerMod.CONFIG.minFalloffRange());
        } catch(Exception e) {
            return false;
        }
    }

    public boolean volumePredicate(String value) {
        if (value == null || value.trim().isEmpty()) {
            value = "0";
        }

        try {
            float v = Float.parseFloat(value);
            return !(v > SoundMufflerMod.CONFIG.maxMinVolume()) && !(v < SoundMufflerMod.CONFIG.minMinVolume());
        } catch(Exception e) {
            return false;
        }
    }
}
