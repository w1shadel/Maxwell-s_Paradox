package com.maxwell.MiniBosses.entity.renderer;


import com.maxwell.MiniBosses.entity.custom.herobrine;
import com.maxwell.MiniBosses.entity.model.herobrineModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
@OnlyIn(Dist.CLIENT)
public class herobrineRenderer extends GeoEntityRenderer<herobrine> {
        public herobrineRenderer(EntityRendererProvider.Context renderManager) {
            super(renderManager, new herobrineModel());
            this.shadowRadius = 0f; // 影のサイズを設定
        }
}
