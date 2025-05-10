package com.maxwell.MiniBosses.Item.ItemRenderer;

import com.maxwell.MiniBosses.Item.ItemMain.Murasama_notawakend;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class Murasama_notawakendRenderer extends GeoItemRenderer<Murasama_notawakend> {
    public Murasama_notawakendRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation("miniboss", "murasama_notawakend")));
    }
}