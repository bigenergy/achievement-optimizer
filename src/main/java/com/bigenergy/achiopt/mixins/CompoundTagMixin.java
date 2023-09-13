package com.bigenergy.achiopt.mixins;

import org.spongepowered.asm.mixin.Mixin;

import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={CompoundTag.class})
public class CompoundTagMixin
        implements ITagMapGetter {
    @Shadow
    @Final
    private Map<String, Tag> tags;
    private int hashCode;
    private boolean needRehash;
    private CompoundTag equalTo;
    private CompoundTag self;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int hashCode() {
        if (this.needRehash) {
            this.needRehash = false;
            this.hashCode = this.tags.hashCode();
        }
        return this.hashCode;
    }

    @Redirect(method={"put", "putByte", "putShort", "putInt", "putLong", "putUUID", "putFloat", "putDouble", "putString", "putByteArray", "putIntArray", "putLongArray", "putBoolean"}, at=@At(value="INVOKE", target="Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object onPut(Map map, Object key, Object value) {
        this.needRehash = true;
        this.equalTo = null;
        return map.put(key, value);
    }

    @Inject(method={"equals"}, at={@At(value="INVOKE", target="Ljava/util/Objects;equals(Ljava/lang/Object;Ljava/lang/Object;)Z")}, cancellable=true)
    public void befOreComparetest(Object compound2, CallbackInfoReturnable<Boolean> cir) {
        if (this == compound2) {
            cir.setReturnValue(true);
            return;
        }
        if (compound2 instanceof CompoundTag && this.hashCode() == compound2.hashCode()) {
            if (this.equalTo == compound2 && ((ITagMapGetter)compound2).getLastEqual() == this.self) {
                cir.setReturnValue(true);
            }
        } else {
            cir.setReturnValue(false);
        }
    }

    @Inject(method={"equals"}, at={@At(value="RETURN", target="Ljava/util/Objects;equals(Ljava/lang/Object;Ljava/lang/Object;)Z")})
    public void afterComparetest(Object compound2, CallbackInfoReturnable<Boolean> cir) {
        if (this == compound2) {
            return;
        }
        if (cir.getReturnValueZ()) {
            this.equalTo = (CompoundTag)compound2;
            ((ITagMapGetter)compound2).setLastEqual(this.self);
        }
    }

    @Override
    public void setLastEqual(CompoundTag compoundNBT) {
        this.equalTo = compoundNBT;
    }

    @Override
    public CompoundTag getLastEqual() {
        return this.equalTo;
    }

    @Override
    public Map<String, Tag> getTagMapNow() {
        return this.tags;
    }
}
