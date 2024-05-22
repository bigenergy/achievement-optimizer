package com.bigenergy.achiopt.mixins;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.advancements.critereon.MinMaxBounds;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Set;

@Mixin(value = ItemPredicate.class, remap = false)
public class ItemPredicateMixin {

    @Shadow
    public static final ItemPredicate ANY = null;
    @Shadow
    private final TagKey<Item> tag;
    @Shadow
    private final Set<Item> items;
    @Shadow
    private final MinMaxBounds.Ints count;
    @Shadow
    private final MinMaxBounds.Ints durability;
    @Shadow
    private final EnchantmentPredicate[] enchantments;
    @Shadow
    private final EnchantmentPredicate[] storedEnchantments;
    @Shadow
    private final Potion potion;
    @Shadow
    private final NbtPredicate nbt;

    public ItemPredicateMixin() {
        this.tag = null;
        this.items = null;
        this.potion = null;
        this.count = MinMaxBounds.Ints.ANY;
        this.durability = MinMaxBounds.Ints.ANY;
        this.enchantments = EnchantmentPredicate.NONE;
        this.storedEnchantments = EnchantmentPredicate.NONE;
        this.nbt = NbtPredicate.ANY;
    }

    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    private void onMatches(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (this.equals(ItemPredicate.ANY)) {
            cir.setReturnValue(true);
            return;
        }

        if (this.tag != null && !itemStack.is(this.tag)) {
            cir.setReturnValue(false);
            return;
        }

        if (this.items != null && !this.items.contains(itemStack.getItem())) {
            cir.setReturnValue(false);
            return;
        }

        if (!this.count.matches(itemStack.getCount())) {
            cir.setReturnValue(false);
            return;
        }

        if (!this.durability.isAny()) {
            if (!itemStack.isDamageableItem() || !this.durability.matches(itemStack.getMaxDamage() - itemStack.getDamageValue())) {
                cir.setReturnValue(false);
                return;
            }
        }

        if (!this.nbt.matches(itemStack)) {
            cir.setReturnValue(false);
            return;
        }

        // Check enchantments
        if (this.enchantments.length > 0 && !achievementOptimizer$checkEnchantments(itemStack, this.enchantments)) {
            cir.setReturnValue(false);
            return;
        }

        // Check stored enchantments
        if (this.storedEnchantments.length > 0 && !achievementOptimizer$checkStoredEnchantments(itemStack, this.storedEnchantments)) {
            cir.setReturnValue(false);
            return;
        }

        // Check potion
        if (this.potion != null && this.potion != PotionUtils.getPotion(itemStack)) {
            cir.setReturnValue(false);
            return;
        }

        cir.setReturnValue(true);
    }

    @Unique
    private boolean achievementOptimizer$checkEnchantments(ItemStack itemStack, EnchantmentPredicate[] enchantments) {
        Map<Enchantment, Integer> enchantmentsMap = itemStack.getAllEnchantments();
        for (EnchantmentPredicate predicate : enchantments) {
            if (!predicate.containedIn(enchantmentsMap)) {
                return false;
            }
        }
        return true;
    }

    @Unique
    private boolean achievementOptimizer$checkStoredEnchantments(ItemStack itemStack, EnchantmentPredicate[] storedEnchantments) {
        Map<Enchantment, Integer> storedEnchantmentsMap = EnchantmentHelper.deserializeEnchantments(EnchantedBookItem.getEnchantments(itemStack));
        for (EnchantmentPredicate predicate : storedEnchantments) {
            if (!predicate.containedIn(storedEnchantmentsMap)) {
                return false;
            }
        }
        return true;
    }
}