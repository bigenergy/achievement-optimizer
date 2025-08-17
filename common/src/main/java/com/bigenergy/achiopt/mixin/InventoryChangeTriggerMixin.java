package com.bigenergy.achiopt.mixin;

import com.bigenergy.achiopt.Achiopt;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Input optimization:
 * - optionally ignore empty stacks (less pointless passes);
 * - process not every tick, but once every N ticks (before calculating full/empty/occupied).
 */
@Mixin(InventoryChangeTrigger.class)
public class InventoryChangeTriggerMixin {

    @Inject(
            method = "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void achiopt$gate(ServerPlayer player, Inventory inv, ItemStack changed, CallbackInfo ci) {
        // 1) ignore empty stacks
        if (Achiopt.CONFIG.ignoreEmptyStacks.get() && changed.isEmpty()) {
            ci.cancel();
            return;
        }

        // 2) skipping ticks BEFORE heavy i/j/k counting
        int skip = Achiopt.CONFIG.skipTicksAdvancements.get();
        if (skip > 0) {
            int now = player.getServer().getTickCount();
            if ((now % skip) != 0) {
                ci.cancel();
            }
        }
    }
}