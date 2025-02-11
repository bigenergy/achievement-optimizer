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

    private static final ModConfigSpec.BooleanValue IGNORE_EMPTY_STACKS = BUILDER
            .comment("Ignore empty stacks for inventory checks")
            .define("ignoreEmptyStacks", true);


    public static final ModConfigSpec SPEC = BUILDER.build();
    public static int skipTicksAdvancements;
    public static Boolean ignoreEmptyStacks;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        skipTicksAdvancements = SKIP_TICKS_ADVANCEMENTS.get();
        ignoreEmptyStacks = IGNORE_EMPTY_STACKS.get();
    }
}
