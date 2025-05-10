package com.maxwell.MiniBosses.entity.renderer;


import com.maxwell.MiniBosses.entity.custom.herobrine;
import com.maxwell.MiniBosses.entity.custom.herobrine2;
import com.maxwell.MiniBosses.entity.model.herobrine2Model;
import com.maxwell.MiniBosses.entity.model.herobrineModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
@OnlyIn(Dist.CLIENT)
public class herobrine2Renderer extends GeoEntityRenderer<herobrine2> {
        public herobrine2Renderer(EntityRendererProvider.Context renderManager) {
            super(renderManager, new herobrine2Model());
            this.shadowRadius = 0f; // 影のサイズを設定
        }
}
