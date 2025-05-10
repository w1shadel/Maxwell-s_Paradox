package com.maxwell.MiniBosses.entity.model;

import com.maxwell.MiniBosses.entity.custom.gulitchguy;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class glitchguyModel extends GeoModel<gulitchguy> {
    @Override
    public ResourceLocation getModelResource(gulitchguy gulitchguy) {
        return new ResourceLocation("miniboss", "geo/glitchguy.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(gulitchguy gulitchguy) {
        return new ResourceLocation("miniboss", "textures/entity/glichguymain.png");
    }

    @Override
    public ResourceLocation getAnimationResource(gulitchguy gulitchguy) {
        return new ResourceLocation("miniboss", "animations/glitchguy.animation.json");
    }
}
