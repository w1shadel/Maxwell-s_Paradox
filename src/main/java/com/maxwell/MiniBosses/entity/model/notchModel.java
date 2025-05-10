package com.maxwell.MiniBosses.entity.model;

import com.maxwell.MiniBosses.entity.custom.boundingbox;
import com.maxwell.MiniBosses.entity.custom.notch;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class notchModel extends GeoModel<notch> {
    @Override
    public ResourceLocation getModelResource(notch notch) {
        return new ResourceLocation("miniboss", "geo/notch.geo.json");
    }
    @Override
    public ResourceLocation getTextureResource(notch notch) {
        return new ResourceLocation("miniboss", "textures/entity/notch.png");
    }
    @Override
    public ResourceLocation getAnimationResource(notch notch) {
        return new ResourceLocation("miniboss", "animations/notch.animation.json");
    }
}
