package com.maxwell.MiniBosses.entity.renderer;

import com.maxwell.MiniBosses.entity.custom.nullguy;
import com.maxwell.MiniBosses.entity.model.nullguyModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
@OnlyIn(Dist.CLIENT)
    public class nullguyRederer extends GeoEntityRenderer<nullguy> {
        public nullguyRederer(EntityRendererProvider.Context renderManager) {
            super(renderManager, new nullguyModel());
            this.shadowRadius = 0f; // 影のサイズを設定
        }
    }
