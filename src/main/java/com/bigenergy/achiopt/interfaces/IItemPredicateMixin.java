package com.bigenergy.achiopt.interfaces;

import net.minecraft.world.item.ItemStack;

/**
 * Interface from accessing optimized match check
 */
public interface IItemPredicateMixin {
    /**
     * Call the optimized match check
     * @param itemStack stack to check
     * @return match result
     */
    boolean achiopt$fastMatches(ItemStack itemStack);
}
