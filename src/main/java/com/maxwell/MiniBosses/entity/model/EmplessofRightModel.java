package com.maxwell.MiniBosses.entity.model;

import com.maxwell.MiniBosses.entity.custom.EmplessofRight;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EmplessofRightModel extends GeoModel<EmplessofRight> {
    @Override
    public ResourceLocation getModelResource(EmplessofRight EmplessofRight) {
        return new ResourceLocation("miniboss", "geo/emplessofright.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EmplessofRight EmplessofRight) {
        return new ResourceLocation("miniboss", "textures/entity/emplessofright.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EmplessofRight EmplessofRight) {
        return new ResourceLocation("miniboss", "animations/emplessofright.animation.json");
    }
}

