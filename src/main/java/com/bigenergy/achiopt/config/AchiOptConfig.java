package com.bigenergy.achiopt.config;

import com.bigenergy.achiopt.AchievementOptimizer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = AchievementOptimizer.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AchiOptConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue SKIP_TICKS_ADVANCEMENTS = BUILDER
            .comment("Number of ticks to skip to check achievements [0 for disable skip]")
            .defineInRange("skipTicksAdvancements", 5, 0, Integer.MAX_VALUE);


    public static final ModConfigSpec SPEC = BUILDER.build();
    public static int skipTicksAdvancements;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        skipTicksAdvancements = SKIP_TICKS_ADVANCEMENTS.get();

    }
}
