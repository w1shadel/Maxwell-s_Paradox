package com.maxwell.MiniBosses.Item.ItemRenderer;

import com.maxwell.MiniBosses.Item.ItemMain.Muramasa;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MuramasaRenderer extends GeoItemRenderer<Muramasa> {
    public MuramasaRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation("miniboss", "murasama")));
    }
}
