package com.tropicbliss.soundmuffler.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return null;
        }

        return parent -> {
            SoundMufflerConfig config = SoundMufflerConfig.getInstance();
            SoundMufflerConfig configDefault = new SoundMufflerConfig();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("soundmuffler.title"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            ConfigCategory cat = builder.getOrCreateCategory(Text.translatable("soundmuffler.config.category.main"));

            // Minimum volume
            cat.addEntry(entryBuilder.startFloatField(Text.translatable("soundmuffler.config.min_volume"), config.minVolume)
                    .setTooltip(Text.translatable("soundmuffler.config.min_volume.tooltip"))
                    .setDefaultValue(configDefault.minVolume)
                    .setSaveConsumer(value -> config.minVolume = value)
                    .build());

            // Muffler range
            cat.addEntry(entryBuilder.startDoubleField(Text.translatable("soundmuffler.config.muffler_range"), config.mufflerRange)
                    .setTooltip(Text.translatable("soundmuffler.config.muffler_range.tooltip"))
                    .setDefaultValue(configDefault.mufflerRange)
                    .setSaveConsumer(value -> config.mufflerRange = value)
                    .build());

            // Falloff range
            cat.addEntry(entryBuilder.startDoubleField(Text.translatable("soundmuffler.config.falloff_range"), config.falloffRange)
                    .setTooltip(Text.translatable("soundmuffler.config.falloff_range.tooltip"))
                    .setDefaultValue(configDefault.falloffRange)
                    .setSaveConsumer(value -> config.falloffRange = value)
                    .build());

            // Falloff method
            cat.addEntry(entryBuilder.startEnumSelector(Text.translatable("soundmuffler.config.falloff_method"), SoundMufflerConfig.FalloffMethod.class, config.falloffMethod)
                    .setTooltip(Text.translatable("soundmuffler.config.falloff_method.tooltip"))
                    .setDefaultValue(configDefault.falloffMethod)
                    .setSaveConsumer(value -> config.falloffMethod = value)
                    .build());

            SoundMufflerConfig.setInstance(config);
            builder.setSavingRunnable(SoundMufflerConfig::save);
            return builder.build();
        };
    }
}
