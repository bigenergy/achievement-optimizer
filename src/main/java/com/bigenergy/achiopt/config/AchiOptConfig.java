package com.bigenergy.achiopt.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AchiOptConfig {
    public static final General general;
    public static final ForgeConfigSpec COMMON_CONFIG;
    private static final ForgeConfigSpec.Builder COMMON_BUILDER;

    // Don't judge me! It's because of auto formatting moving the order around!
    static {
        COMMON_BUILDER = new ForgeConfigSpec.Builder();

        general = new General();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static class General {
        public final ForgeConfigSpec.IntValue skipTicksAdvancements;

        General() {
            COMMON_BUILDER.push("general");

            this.skipTicksAdvancements = COMMON_BUILDER
                    .comment("Number of ticks to skip to check achievements [0 for disable skip]")
                    .defineInRange("skipTicksAdvancements", 5, 0, Integer.MAX_VALUE);


            COMMON_BUILDER.pop();
        }
    }



}