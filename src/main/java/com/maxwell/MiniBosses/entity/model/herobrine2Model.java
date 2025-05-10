package com.maxwell.MiniBosses.entity.model;

import com.maxwell.MiniBosses.entity.custom.herobrine;
import com.maxwell.MiniBosses.entity.custom.herobrine2;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class herobrine2Model extends GeoModel<herobrine2> {
    @Override
    public ResourceLocation getModelResource(herobrine2 herobrine) {
        return new ResourceLocation("miniboss", "geo/herobrine2.geo.json");
    }
    @Override
    public ResourceLocation getTextureResource(herobrine2 herobrine) {
        return new ResourceLocation("miniboss", "textures/entity/herobrine.png");
    }
    @Override
    public ResourceLocation getAnimationResource(herobrine2 herobrine) {
        return new ResourceLocation("miniboss", "animations/herobrine2.animation.json");
    }
}
