package net.arathain.charter.block.entity.render.model;

import net.arathain.charter.Charter;
import net.arathain.charter.block.WaystoneBlock;
import net.arathain.charter.block.entity.WaystoneEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import java.util.Objects;

public class WaystoneMarksModel extends GeoModel<WaystoneEntity> {
private static final Identifier TEXTURE_IDENTIFIER = new Identifier(Charter.MODID, "textures/block/waystone.png");
private static final Identifier MODEL_IDENTIFIER = new Identifier(Charter.MODID, "geo/waystone_marks.geo.json");
private static final Identifier ANIMATION_IDENTIFIER = new Identifier(Charter.MODID, "animations/waystone.animation.json");

@Override
public Identifier getModelResource(WaystoneEntity object) {
        return MODEL_IDENTIFIER;
        }

@Override
public Identifier getTextureResource(WaystoneEntity object) {
        return TEXTURE_IDENTIFIER;
        }

@Override
public Identifier getAnimationResource(WaystoneEntity animatable) {
        return ANIMATION_IDENTIFIER;
        }

        public void handleAnimation(WaystoneEntity entity, Integer uniqueID, AnimationState customPredicate) {
                CoreGeoBone marks4 = this.getAnimationProcessor().getBone("marks4");
                CoreGeoBone marks5 = this.getAnimationProcessor().getBone("marks5");

                if(entity.getWorld() != null && entity.getWorld().getBlockState(entity.getPos()) != null && entity.getWorld().getBlockState(entity.getPos()).getBlock() instanceof WaystoneBlock && entity.getWorld().getBlockState(entity.getPos()).get(Properties.LIT)) {
                        super.handleAnimations(entity, uniqueID, customPredicate);
                        marks4.setHidden(false);
                        marks5.setHidden(false);
                } else {
                        marks4.setHidden(true);
                        marks5.setHidden(true);
                }
        }
}
