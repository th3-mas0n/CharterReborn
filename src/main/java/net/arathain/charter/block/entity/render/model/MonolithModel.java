package net.arathain.charter.block.entity.render.model;

import net.arathain.charter.Charter;
import net.arathain.charter.block.entity.CharterStoneEntity;
import net.arathain.charter.block.entity.MonolithEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MonolithModel extends AnimatedGeoModel<MonolithEntity> {
    private static final Identifier TEXTURE_IDENTIFIER = new Identifier(Charter.MODID, "textures/block/monolith.png");
    private static final Identifier MODEL_IDENTIFIER = new Identifier(Charter.MODID, "geo/monolith.geo.json");
    private static final Identifier ANIMATION_IDENTIFIER = new Identifier(Charter.MODID, "animations/monolith_marks.animation.json");

    @Override
    public Identifier getModelResource(MonolithEntity object) {

        return MODEL_IDENTIFIER;
    }

    @Override
    public Identifier getTextureResource(MonolithEntity object) {

        return TEXTURE_IDENTIFIER;
    }

    @Override
    public Identifier getAnimationResource(MonolithEntity animatable) {

        return ANIMATION_IDENTIFIER;
    }

}
