package com.maxwell.MiniBosses.entity.model;

import com.maxwell.MiniBosses.entity.custom.boundingbox;
import com.maxwell.MiniBosses.entity.custom.herobrine;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class herobrineModel extends GeoModel<herobrine> {
    @Override
    public ResourceLocation getModelResource(herobrine herobrine) {
        return new ResourceLocation("miniboss", "geo/herobrine.geo.json");
    }
    @Override
    public ResourceLocation getTextureResource(herobrine herobrine) {
        return new ResourceLocation("miniboss", "textures/entity/herobrine.png");
    }
    @Override
    public ResourceLocation getAnimationResource(herobrine herobrine) {
        return new ResourceLocation("miniboss", "animations/herobrine.animation.json");
    }
}
