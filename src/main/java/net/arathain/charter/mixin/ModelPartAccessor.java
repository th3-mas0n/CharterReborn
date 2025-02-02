package net.arathain.charter.mixin;

import java.util.Map;

import net.minecraft.client.model.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ModelPart.class})
public interface ModelPartAccessor {
    @Accessor("children")
    Map<String, ModelPart> getChildren();
}
