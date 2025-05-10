package com.maxwell.MiniBosses.Item.ArmorRenderer;

import com.maxwell.MiniBosses.Item.ArmorMain.Nullarmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public final class NullArmorRenderer extends GeoArmorRenderer<Nullarmor> {
    public NullArmorRenderer() {
        super(new DefaultedItemGeoModel(new ResourceLocation("miniboss", "armor/nullarmor")));
    }
}