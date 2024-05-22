package com.bigenergy.achiopt.mixins;

import com.bigenergy.achiopt.interfaces.IItemPredicateMixin;
import net.minecraft.advancements.critereon.*;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryChangeTrigger.TriggerInstance.class)
abstract class InventoryChangeTriggerInstanceMixin {

    /**
     * Use optimized itemPredicate match
     */
    @Redirect(method = "matches(Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;III)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/ItemPredicate;matches(Lnet/minecraft/world/item/ItemStack;)Z"))
    public boolean itemPredicateMatches(ItemPredicate itemPredicate, ItemStack itemStack) {
        return ((IItemPredicateMixin) (Object) itemPredicate).achiopt$fastMatches(itemStack);
    }
}
