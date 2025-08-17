package com.bigenergy.achiopt.mixin;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Speeds up the items.size() > 1 branch:
 * instead of creating an ObjectArrayList and removeIf, we use a single pass
 * over the inventory and boolean[] matched (without additional allocations/removals).
 */
@Mixin(InventoryChangeTrigger.TriggerInstance.class)
public abstract class InventoryChangeTrigger_TriggerInstanceMixin {

    @Shadow
    public abstract InventoryChangeTrigger.TriggerInstance.Slots slots();
    @Shadow public abstract List<ItemPredicate> items();

    @Inject(
            method = "matches(Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void achiopt$fastMatch(Inventory inventory, ItemStack changed, int full, int empty, int occupied, CallbackInfoReturnable<Boolean> cir) {
        // default slot checks
        if (!this.slots().matches(full, empty, occupied)) {
            cir.setReturnValue(false);
            return;
        }

        List<ItemPredicate> req = this.items();
        int size = req.size();

        // No requirements - passes
        if (size == 0) {
            cir.setReturnValue(true);
            return;
        }

        // One check - we use the passed "changed" as vanilla
        if (size == 1) {
            cir.setReturnValue(!changed.isEmpty() && req.get(0).test(changed));
            return;
        }

        // Fast Track: Multiple Requirements - One inventory pass, no allocations/deletions
        boolean[] matched = new boolean[size];
        int remaining = size;

        final int len = inventory.getContainerSize();
        for (int i = 0; i < len && remaining > 0; i++) {
            ItemStack s = inventory.getItem(i);
            if (s.isEmpty()) continue;

            for (int idx = 0; idx < size; idx++) {
                if (!matched[idx] && req.get(idx).test(s)) {
                    matched[idx] = true;
                    if (--remaining == 0) break;
                }
            }
        }

        cir.setReturnValue(remaining == 0);
    }
}
