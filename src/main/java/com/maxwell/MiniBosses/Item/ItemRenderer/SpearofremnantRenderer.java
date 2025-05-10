package com.maxwell.MiniBosses.Item.ItemRenderer;

import com.maxwell.MiniBosses.Item.ItemMain.HerobrineSword;
import com.maxwell.MiniBosses.Item.ItemMain.SpearofRemnannt;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@OnlyIn(Dist.CLIENT)
public class SpearofremnantRenderer extends GeoItemRenderer<SpearofRemnannt> {
    public SpearofremnantRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation("miniboss", "spearofremannt")));
    }
}