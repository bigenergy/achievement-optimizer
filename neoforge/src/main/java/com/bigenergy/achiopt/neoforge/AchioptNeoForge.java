package com.bigenergy.achiopt.neoforge;

import com.bigenergy.achiopt.Achiopt;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Achiopt.MOD_ID)
public final class AchioptNeoForge {
    public AchioptNeoForge(IEventBus eBuss, ModContainer container) {
        // Run our common setup.
        new Achiopt();
        Achiopt.init();
        container.registerConfig(ModConfig.Type.COMMON, Achiopt.CONFIG_SPEC);
    }
}
