package com.maxwell.MiniBosses.entity.renderer;


import com.maxwell.MiniBosses.entity.custom.gulitchguy;
import com.maxwell.MiniBosses.entity.model.glitchguyModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
@OnlyIn(Dist.CLIENT)
public class glitchguyRenderer extends GeoEntityRenderer<gulitchguy> {
        public glitchguyRenderer(EntityRendererProvider.Context renderManager) {
            super(renderManager, new glitchguyModel());
            this.shadowRadius = 0f; // 影のサイズを設定
        }
}
