package com.maxwell.MiniBosses.entity.renderer;

import com.maxwell.MiniBosses.entity.custom.PhantomKing;
import com.maxwell.MiniBosses.entity.model.PhantomKingModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
@OnlyIn(Dist.CLIENT)
public class PhantomKingRenderer extends GeoEntityRenderer<PhantomKing> {
    public PhantomKingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PhantomKingModel());
        this.shadowRadius = 0f; // 影のサイズを設定
    }
}
