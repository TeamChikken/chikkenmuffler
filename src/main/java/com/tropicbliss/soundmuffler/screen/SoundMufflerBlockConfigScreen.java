package com.tropicbliss.soundmuffler.screen;

import com.tropicbliss.soundmuffler.SoundMufflerMod;
import com.tropicbliss.soundmuffler.block.SoundMufflerBlockEntity;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.util.Identifier;

public class SoundMufflerBlockConfigScreen extends BaseUIModelScreen<FlowLayout> {
    private SoundMufflerBlockEntity soundMufflerBlockEntity;

    public SoundMufflerBlockConfigScreen(SoundMufflerBlockEntity soundMufflerBlockEntity) {
        super(FlowLayout.class, DataSource.asset(new Identifier(SoundMufflerMod.MOD_ID, "soundmuffler_ui_model")));
        this.soundMufflerBlockEntity = soundMufflerBlockEntity;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.childById(ButtonComponent.class, "button-cancel").onPress(button -> {
            close();
        });

        // TODO: Send packet requesting for muffler update?
        rootComponent.childById(ButtonComponent.class, "button-done").onPress(button -> {
            soundMufflerBlockEntity.setMinVolume(Float.parseFloat(rootComponent.childById(TextBoxComponent.class, "min-volume-text-box").getText()));
            close();
        });

        // Set the values based on the block entities
        rootComponent.childById(TextBoxComponent.class, "min-volume-text-box").text(String.valueOf(soundMufflerBlockEntity.getMinVolume())).setTextPredicate(this::volumePredicate);
        rootComponent.childById(TextBoxComponent.class, "range-text-box").text(String.valueOf(soundMufflerBlockEntity.getMufflerRange())).setTextPredicate(this::rangePredicate);
        rootComponent.childById(TextBoxComponent.class, "falloff-range-text-box").text(String.valueOf(soundMufflerBlockEntity.getFalloffRange())).setTextPredicate(this::falloffRangePredicate);
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
