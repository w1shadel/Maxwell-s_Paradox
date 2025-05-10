package com.maxwell.MiniBosses.entity.model;

import com.maxwell.MiniBosses.entity.custom.PhantomKing;
import com.maxwell.MiniBosses.entity.custom.nullguy;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PhantomKingModel extends GeoModel<PhantomKing> {
    @Override
    public ResourceLocation getModelResource(PhantomKing PhantomKing) {
        return new ResourceLocation("miniboss", "geo/omega_remnant.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PhantomKing PhantomKing) {
        return new ResourceLocation("miniboss", "textures/entity/omega_remnant.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PhantomKing PhantomKing) {
        return new ResourceLocation("miniboss", "animations/omega_remnant.animation.json");
    }
}
