package net.arathain.charter.block.entity.render;


import net.arathain.charter.block.entity.WaystoneEntity;
import net.arathain.charter.block.entity.render.model.WaystoneMarksModel;
import net.arathain.charter.block.entity.render.model.WaystoneModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.RenderUtils;


public class WaystoneRenderer extends GeoBlockRenderer<WaystoneEntity> {
    public WaystoneRenderer() {
        super(new WaystoneMarksModel());
    }


    @Override
    public void render(WaystoneEntity tile, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(tile, partialTicks, stack, bufferIn, packedLightIn, 15728880);

        BakedGeoModel model = this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(tile));

        stack.push();
        stack.translate(0, 0.01f, 0);
        stack.translate(0.5, 0, 0.5);


        Identifier textureResource = getTextureResource(tile);
        RenderUtils.bindTexture(textureResource);


        RenderLayer renderType = getRenderType(tile, partialTicks, stack, bufferIn, null, packedLightIn, textureResource);
        VertexConsumer vertexConsumer = bufferIn.getBuffer(renderType);


        // Render the model
        renderModel(model, tile, partialTicks, renderType, stack, bufferIn, vertexConsumer,
                packedLightIn, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);


        stack.pop();
    }


    @Override
    public RenderLayer getRenderType(WaystoneEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder,
                                     int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(textureLocation);
    }


    // If you need a custom texture resource method
    @Override
    public Identifier getTextureResource(WaystoneEntity instance) {
        return new WaystoneModel().getTextureResource(instance);
    }
}