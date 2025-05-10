package com.maxwell.MiniBosses.Item.ItemRenderer;

import com.maxwell.MiniBosses.Item.ItemMain.NotchSword;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.example.item.JackInTheBoxItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
@OnlyIn(Dist.CLIENT)
public class NotchSwordRenderer extends GeoItemRenderer<NotchSword> {
    public NotchSwordRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation("miniboss", "notchsword")));
    }
}