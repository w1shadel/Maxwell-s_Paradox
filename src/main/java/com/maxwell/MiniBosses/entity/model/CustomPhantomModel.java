package com.maxwell.MiniBosses.entity.model;

import com.maxwell.MiniBosses.entity.custom.CustomPhantom;
import com.maxwell.MiniBosses.entity.custom.gulitchguy;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CustomPhantomModel extends GeoModel<CustomPhantom> {

    @Override
    public ResourceLocation getModelResource(CustomPhantom CustomPhantom) {
        return new ResourceLocation("miniboss", "geo/cusomphantom.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CustomPhantom CustomPhantom) {
        return new ResourceLocation("miniboss", "textures/entity/cusomphantom.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CustomPhantom CustomPhantom) {
        return new ResourceLocation("miniboss", "animations/cusomphantom.animation.json");
    }
}
