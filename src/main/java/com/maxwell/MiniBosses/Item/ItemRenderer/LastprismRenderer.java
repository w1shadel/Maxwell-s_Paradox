package com.maxwell.MiniBosses.Item.ItemRenderer;

import com.maxwell.MiniBosses.Item.ItemMain.HerobrineSword;
import com.maxwell.MiniBosses.Item.ItemMain.Lastprism;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class LastprismRenderer extends GeoItemRenderer<Lastprism> {
    public LastprismRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation("miniboss", "lastprism")));
    }
}
