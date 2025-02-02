package net.arathain.charter.mixin;

import net.arathain.client.model.Existent;
import net.minecraft.client.model.ModelPart;
import net.minecraft.util.thread.AtomicStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin({AtomicStack.class})
public abstract class EntityModelMixin implements Existent {
    public EntityModelMixin() {
    }

    public List<ModelPart> getModelParts() {
        return List.of();
    }
}

