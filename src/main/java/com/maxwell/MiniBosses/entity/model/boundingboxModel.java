package com.maxwell.MiniBosses.entity.model;

import com.maxwell.MiniBosses.entity.custom.boundingbox;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;
@OnlyIn(Dist.CLIENT)
public class boundingboxModel extends GeoModel<boundingbox> {
    @Override
    public ResourceLocation getModelResource(boundingbox boundingbox) {
        return new ResourceLocation("miniboss", "geo/boundingbox.geo.json");
    }
    @Override
    public ResourceLocation getTextureResource(boundingbox boundingbox) {
        return new ResourceLocation("miniboss", "textures/entity/boundingbox.png");
    }
    @Override
    public ResourceLocation getAnimationResource(boundingbox boundingbox) {
        return new ResourceLocation("miniboss", "animations/null.animation.json");
    }
}
