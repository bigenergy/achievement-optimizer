package com.bigenergy.achiopt.mixin;

import com.bigenergy.achiopt.config.AchiOptConfig;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryChangeTrigger.class)
public class InventoryChangeTriggerMixin extends SimpleCriterionTrigger<InventoryChangeTrigger.TriggerInstance> {


    @Unique
    private int achievementOptimizer$ticksSkipped;

    private boolean tryTick()
    {
        int skipTicksAmount = AchiOptConfig.skipTicksAdvancements;
        if (skipTicksAmount <= 0)
            return true;

        this.achievementOptimizer$ticksSkipped++;
        if (this.achievementOptimizer$ticksSkipped > skipTicksAmount)
        {
            this.achievementOptimizer$ticksSkipped = 0;
            return true;
        }

        return false;
    }


    @Inject(method = "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void trigger(ServerPlayer p_43150_, Inventory p_43151_, ItemStack p_43152_, CallbackInfo ci) {
        if (p_43152_.isEmpty() && AchiOptConfig.ignoreEmptyStacks) {
            ci.cancel();
        }
        if (!this.tryTick()) {
            ci.cancel();
        }
    }

    @Shadow
    public Codec<InventoryChangeTrigger.TriggerInstance> codec() {
        return null;
    }
}
