package com.maxwell.MiniBosses.entity.renderer;

import com.maxwell.MiniBosses.entity.custom.boundingbox;
import com.maxwell.MiniBosses.entity.model.boundingboxModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
@OnlyIn(Dist.CLIENT)
public class boundingboxRenderer extends GeoEntityRenderer<boundingbox>{
        public boundingboxRenderer(EntityRendererProvider.Context renderManager) {
            super(renderManager, new boundingboxModel());
            this.shadowRadius = 0f; // 影のサイズを設定
        }
}
