package com.maxwell.MiniBosses.Item.ItemRenderer;

import com.maxwell.MiniBosses.Item.ItemMain.TerraBlade;
import com.maxwell.MiniBosses.Item.ItemMain.terraprism;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
@OnlyIn(Dist.CLIENT)
public class terraprismRenderer extends GeoItemRenderer<terraprism> {
    public terraprismRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation("miniboss", "terraprism_minecraft")));
    }
}
