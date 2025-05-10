package com.maxwell.MiniBosses.entity.model;

import com.maxwell.MiniBosses.entity.custom.nullguy;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class nullguyModel extends GeoModel<nullguy> {
    @Override
    public ResourceLocation getModelResource(nullguy nullguy) {
        return new ResourceLocation("miniboss", "geo/null.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(nullguy nullguy) {
        return new ResourceLocation("miniboss", "textures/entity/nullmain.png");
    }

    @Override
    public ResourceLocation getAnimationResource(nullguy nullguy) {
        return new ResourceLocation("miniboss", "animations/null.animation.json");
    }
}
