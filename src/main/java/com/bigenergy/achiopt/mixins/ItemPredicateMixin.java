package com.bigenergy.achiopt.mixins;

import com.bigenergy.achiopt.interfaces.IItemPredicateMixin;
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
import org.spongepowered.asm.mixin.*;
import net.minecraft.advancements.critereon.MinMaxBounds;

import java.util.Map;
import java.util.Set;

@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin implements IItemPredicateMixin {

    @Mutable
    @Final
    @Shadow
    private final TagKey<Item> tag;
    @Mutable
    @Final
    @Shadow
    private final Set<Item> items;
    @Mutable
    @Final
    @Shadow
    private final MinMaxBounds.Ints count;
    @Mutable
    @Final
    @Shadow
    private final MinMaxBounds.Ints durability;
    @Mutable
    @Final
    @Shadow
    private final EnchantmentPredicate[] enchantments;
    @Mutable
    @Final
    @Shadow
    private final EnchantmentPredicate[] storedEnchantments;
    @Mutable
    @Final
    @Shadow
    private final Potion potion;
    @Mutable
    @Final
    @Shadow
    private final NbtPredicate nbt;

    protected ItemPredicateMixin(TagKey<Item> tag, Set<Item> items, MinMaxBounds.Ints count, MinMaxBounds.Ints durability, EnchantmentPredicate[] enchantments, EnchantmentPredicate[] storedEnchantments, Potion potion, NbtPredicate nbt) {
        this.tag = tag;
        this.items = items;
        this.count = count;
        this.durability = durability;
        this.enchantments = enchantments;
        this.storedEnchantments = storedEnchantments;
        this.potion = potion;
        this.nbt = nbt;
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

    @Shadow
    public abstract boolean matches(ItemStack itemStack);

    @Override
    public boolean achiopt$fastMatches(ItemStack itemStack) {
        if (this.equals(ItemPredicate.ANY)) {
            return true;
        }

        if (this.tag != null && !itemStack.is(this.tag)) {
            return false;
        }

        if (this.items != null && !this.items.contains(itemStack.getItem())) {
            return false;
        }

        if (!this.count.matches(itemStack.getCount())) {
            return false;
        }

        if (!this.durability.isAny()) {
            if (!itemStack.isDamageableItem() || !this.durability.matches(itemStack.getMaxDamage() - itemStack.getDamageValue())) {
                return false;
            }
        }

        if (!this.nbt.matches(itemStack)) {
            return false;
        }

        // Check enchantments
        if (this.enchantments.length > 0 && !achievementOptimizer$checkEnchantments(itemStack, this.enchantments)) {
            return false;
        }

        // Check stored enchantments
        if (this.storedEnchantments.length > 0 && !achievementOptimizer$checkStoredEnchantments(itemStack, this.storedEnchantments)) {
            return false;
        }

        // Check potion
        if (this.potion != null && this.potion != PotionUtils.getPotion(itemStack)) {
            return false;
        }

        return true;
    }
}