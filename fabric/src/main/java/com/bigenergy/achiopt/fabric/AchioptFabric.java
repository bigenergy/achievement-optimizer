package com.bigenergy.achiopt.fabric;

import com.bigenergy.achiopt.Achiopt;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;

public final class AchioptFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        new Achiopt();
        Achiopt.init();
        NeoForgeConfigRegistry.INSTANCE.register(Achiopt.MOD_ID, ModConfig.Type.COMMON, Achiopt.CONFIG_SPEC);
    }
}
