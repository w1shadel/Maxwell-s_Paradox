package com.maxwell.MiniBosses.entity.model;

import com.maxwell.MiniBosses.entity.custom.button;
import com.maxwell.MiniBosses.entity.custom.gulitchguy;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class buttonModel extends GeoModel<button> {
    @Override
    public ResourceLocation getModelResource(button button) {
        return new ResourceLocation("miniboss", "geo/button.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(button button) {
        return new ResourceLocation("miniboss", "textures/entity/button.png");
    }

    @Override
    public ResourceLocation getAnimationResource(button button) {
        return new ResourceLocation("miniboss", "animations/glitchguy.animation.json");
    }
}
