package com.bigenergy.achiopt.mixins;

import com.bigenergy.achiopt.config.AchiOptConfig;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(InventoryChangeTrigger.class)
public class InventoryChangeTriggerMixin extends SimpleCriterionTrigger<InventoryChangeTrigger.TriggerInstance> {


    private int ticksSkipped;

    private boolean tryTick()
    {
        int skipTicksAmount = AchiOptConfig.general.skipTicksAdvancements.get();
        if (skipTicksAmount <= 0)
            return true;

        this.ticksSkipped++;
        if (this.ticksSkipped > skipTicksAmount)
        {
            this.ticksSkipped = 0;
            return true;
        }

        return false;
    }

    public void trigger(ServerPlayer p_43150_, Inventory p_43151_, ItemStack p_43152_) {
        if (!this.tryTick())
            return;

        int i = 0;
        int j = 0;
        int k = 0;

        for(int l = 0; l < p_43151_.getContainerSize(); ++l) {
            ItemStack itemstack = p_43151_.getItem(l);
            if (itemstack.isEmpty()) {
                ++j;
            } else {
                ++k;
                if (itemstack.getCount() >= itemstack.getMaxStackSize()) {
                    ++i;
                }
            }
        }

        this.trigger(p_43150_, p_43151_, p_43152_, i, j, k);
    }

    private void trigger(ServerPlayer p_43154_, Inventory p_43155_, ItemStack p_43156_, int p_43157_, int p_43158_, int p_43159_) {
        this.trigger(p_43154_, (p_43166_) -> {
            return p_43166_.matches(p_43155_, p_43156_, p_43157_, p_43158_, p_43159_);
        });
    }


    @Shadow
    @Override
    protected InventoryChangeTrigger.TriggerInstance createInstance(JsonObject p_66248_, Optional<ContextAwarePredicate> p_297533_, DeserializationContext p_66250_) {
        return null;
    }
}
