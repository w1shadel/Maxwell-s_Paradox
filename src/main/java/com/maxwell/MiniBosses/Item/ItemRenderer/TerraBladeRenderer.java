package com.maxwell.MiniBosses.Item.ItemRenderer;

import com.maxwell.MiniBosses.Item.ItemMain.StardastSword;
import com.maxwell.MiniBosses.Item.ItemMain.TerraBlade;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@OnlyIn(Dist.CLIENT)
public class TerraBladeRenderer extends GeoItemRenderer<TerraBlade> {
    public TerraBladeRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation("miniboss", "terrablade_minecraft")));
    }
}
