package com.bigenergy.achiopt.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class AchiOptConfig {
    public final ModConfigSpec.ConfigValue<Integer> skipTicksAdvancements;
    public final ModConfigSpec.ConfigValue<Boolean> ignoreEmptyStacks;

    public AchiOptConfig(ModConfigSpec.Builder builder) {
        skipTicksAdvancements = builder.comment("Number of ticks to skip to check achievements [0 for disable skip]")
                .defineInRange("skipTicksAdvancements", 5, 0, Integer.MAX_VALUE);

        ignoreEmptyStacks = builder.comment("Ignore empty stacks for inventory checks")
                .define("ignoreEmptyStacks", true);

    }
}
