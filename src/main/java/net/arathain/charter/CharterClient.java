package net.arathain.charter;

import com.mojang.serialization.Codec;
import net.arathain.charter.block.entity.render.CharterStoneRenderer;
import net.arathain.charter.block.entity.render.WaystoneRenderer;
import net.arathain.charter.block.particle.BindingAmbienceParticle;
import net.arathain.charter.block.particle.BindingAmbienceParticleEffect;
import net.arathain.charter.item.BigItemRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemConvertible;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class CharterClient implements ClientModInitializer {
    public static ParticleType<BindingAmbienceParticleEffect> BINDING;

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(Charter.CHARTER_STONE_ENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new CharterStoneRenderer());
        BlockEntityRendererRegistry.register(Charter.WAYSTONE_ENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new WaystoneRenderer());
        BINDING = Registry.register(Registries.PARTICLE_TYPE, "charter:bound", new ParticleType<BindingAmbienceParticleEffect>(true, BindingAmbienceParticleEffect.PARAMETERS_FACTORY) {
            @Override
            public Codec<BindingAmbienceParticleEffect> getCodec() {
                return BindingAmbienceParticleEffect.CODEC;
            }
        });

        registerBigRenderer(Charter.DAWNBREAKER);



        ParticleFactoryRegistry.getInstance().register(BINDING, BindingAmbienceParticle.Factory::new);
        ClientTickEvents.END_WORLD_TICK.register(world -> {
        });

    }
    private void registerBigRenderer(ItemConvertible item) {
        Identifier bigId = Registries.ITEM.getId(item.asItem());
        BigItemRenderer bigItemRenderer = new BigItemRenderer(bigId);
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(bigItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(item, bigItemRenderer);
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ModelIdentifier(new Identifier(bigId.getNamespace(), bigId.getPath() + "_gui"), "inventory"));
            out.accept(new ModelIdentifier(new Identifier(bigId.getNamespace(), bigId.getPath() + "_handheld"), "inventory"));
        });
    }

}
