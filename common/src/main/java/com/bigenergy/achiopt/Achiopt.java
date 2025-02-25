package com.bigenergy.achiopt;

import com.bigenergy.achiopt.config.AchiOptConfig;
import com.mojang.logging.LogUtils;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

public final class Achiopt {
    public static final String MOD_ID = "achiopt";
    public static final AchiOptConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;
    public static final Logger LOGGER = LogUtils.getLogger();

    static {
        Pair<AchiOptConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(AchiOptConfig::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    public static void init() {
        LOGGER.info("Enabling Achievement Optimizer");
    }
}
