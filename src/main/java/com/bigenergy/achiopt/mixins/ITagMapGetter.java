package com.bigenergy.achiopt.mixins;

import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public interface ITagMapGetter {
    public CompoundTag getLastEqual();

    public Map<String, Tag> getTagMapNow();

    public void setLastEqual(CompoundTag var1);
}
