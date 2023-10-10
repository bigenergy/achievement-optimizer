package com.bigenergy.achiopt;

import com.bigenergy.achiopt.config.AchiOptConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import java.io.File;

@Mod(AchievementOptimizer.MODID)
public class AchievementOptimizer {

    public static final String MODID = "achiopt";
    public static final Logger LOGGER = LogManager.getLogger("Achievements Optimizer");

    public AchievementOptimizer() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AchiOptConfig.COMMON_CONFIG, "achievement_optimizer" + File.separator + "Achievement-Optimizer.toml");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Enabling Achievement Optimizer");
    }

}
