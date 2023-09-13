package com.bigenergy.achiopt.mixins;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value=ItemStack.class, priority=998)
public class ItemStackMixin {
    @Inject(method={"tagMatches"}, at={@At(value="HEAD")}, cancellable=true)
    private static void OnAreItemStackTagsEqual(ItemStack stackA, ItemStack stackB, CallbackInfoReturnable<Boolean> re) {
        if (stackA == stackB) {
            re.setReturnValue(true);
        }
    }

    @Inject(method={"matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private static void OnAreItemStacksEqual(ItemStack stackA, ItemStack stackB, CallbackInfoReturnable<Boolean> re) {
        if (stackA == stackB) {
            re.setReturnValue(true);
        }
    }

    @Inject(method={"matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private static void OnAreItemStacksEqualTag(ItemStack stackA, ItemStack stackB, CallbackInfoReturnable<Boolean> re) {
        if (stackA == stackB) {
            re.setReturnValue(true);
        }
    }
    @Redirect(method={"matches(Lnet/minecraft/world/item/ItemStack;)Z"}, at=@At(value="INVOKE", target="Lnet/minecraft/nbt/CompoundTag;equals(Ljava/lang/Object;)Z"))
    private boolean onTagCompare(CompoundTag compoundNBT, Object other) {
        if (compoundNBT == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (compoundNBT.hashCode() != other.hashCode()) {
            return false;
        }
        return compoundNBT.equals(other);
    }
}
