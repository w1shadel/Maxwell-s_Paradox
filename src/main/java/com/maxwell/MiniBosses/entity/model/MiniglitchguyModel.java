package com.maxwell.MiniBosses.entity.model;

import com.maxwell.MiniBosses.entity.custom.Minigulitchguy;
import com.maxwell.MiniBosses.entity.custom.gulitchguy;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MiniglitchguyModel extends GeoModel<Minigulitchguy> {
    @Override
    public ResourceLocation getModelResource(Minigulitchguy Minigulitchguy) {
        return new ResourceLocation("miniboss", "geo/glitchguy.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Minigulitchguy Minigulitchguy) {
        return new ResourceLocation("miniboss", "textures/entity/glichguymain.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Minigulitchguy Minigulitchguy) {
        return new ResourceLocation("miniboss", "animations/glitchguy.animation.json");
    }
}
