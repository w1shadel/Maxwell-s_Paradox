package com.maxwell.MiniBosses.entity.renderer;

import com.maxwell.MiniBosses.entity.custom.CustomPhantom;
import com.maxwell.MiniBosses.entity.model.CustomPhantomModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CustomPhantomRenderer  extends GeoEntityRenderer<CustomPhantom> {
    @OnlyIn(Dist.CLIENT)
        public CustomPhantomRenderer(EntityRendererProvider.Context renderManager) {
            super(renderManager, new CustomPhantomModel());
            this.shadowRadius = 0f; // 影のサイズを設定
        }
}
