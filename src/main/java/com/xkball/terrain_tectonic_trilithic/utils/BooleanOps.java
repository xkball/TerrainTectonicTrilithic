package com.xkball.terrain_tectonic_trilithic.utils;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.shapes.BooleanOp;
import org.jetbrains.annotations.NotNull;

public enum BooleanOps implements StringRepresentable  {
    FALSE(BooleanOp.FALSE),
    NOT_OR(BooleanOp.NOT_OR),
    ONLY_SECOND(BooleanOp.ONLY_SECOND),
    NOT_FIRST(BooleanOp.NOT_FIRST),
    ONLY_FIRST(BooleanOp.ONLY_FIRST),
    NOT_SECOND(BooleanOp.NOT_SECOND),
    NOT_SAME(BooleanOp.NOT_SAME),
    NOT_AND(BooleanOp.NOT_AND),
    AND(BooleanOp.AND),
    SAME(BooleanOp.SAME),
    SECOND(BooleanOp.SECOND),
    CAUSES(BooleanOp.CAUSES),
    FIRST(BooleanOp.FIRST),
    CAUSED_BY(BooleanOp.CAUSED_BY),
    OR(BooleanOp.OR),
    TRUE(BooleanOp.TRUE);
    
    public static final Codec<BooleanOps> CODEC = StringRepresentable.fromEnum(BooleanOps::values);
    
    private final BooleanOp booleanOp;
    
    BooleanOps(BooleanOp booleanOp) {
        this.booleanOp = booleanOp;
    }
    
    public boolean apply(boolean primaryBool, boolean secondaryBool){
        return booleanOp.apply(primaryBool, secondaryBool);
    }
    
    @Override
    @NotNull
    public String getSerializedName() {
        return name().toLowerCase();
    }
    
}
