package com.maxwell.MiniBosses.entity.renderer;

import com.maxwell.MiniBosses.entity.custom.EmplessofRight;
import com.maxwell.MiniBosses.entity.model.EmplessofRightModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EmplessofRightRenderer extends GeoEntityRenderer<EmplessofRight> {
    @OnlyIn(Dist.CLIENT)
    public EmplessofRightRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EmplessofRightModel());
        this.shadowRadius = 0f; // 影のサイズを設定
    }
}
