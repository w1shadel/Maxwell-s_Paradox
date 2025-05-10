package com.maxwell.MiniBosses.Item.ArmorRenderer;

import com.maxwell.MiniBosses.Item.ArmorMain.Omega_blue_armor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public final class OmegaBlueArmorRenderer extends GeoArmorRenderer<Omega_blue_armor> {
    public OmegaBlueArmorRenderer() {
        super(new DefaultedItemGeoModel(new ResourceLocation("miniboss", "armor/omega_armor")));
    }
}