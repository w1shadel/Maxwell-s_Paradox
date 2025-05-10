package com.maxwell.MiniBosses.entity.renderer;


import com.maxwell.MiniBosses.entity.custom.Minigulitchguy;
import com.maxwell.MiniBosses.entity.model.MiniglitchguyModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
@OnlyIn(Dist.CLIENT)
public class MiniglitchguyRenderer extends GeoEntityRenderer<Minigulitchguy> {
        public MiniglitchguyRenderer(EntityRendererProvider.Context renderManager) {
            super(renderManager, new MiniglitchguyModel());
            this.shadowRadius = 0f; // 影のサイズを設定
        }
    @Override
    protected void applyRotations(Minigulitchguy entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        super.applyRotations(entity, poseStack, ageInTicks, rotationYaw, partialTick);
        float scaleFactor = entity.getCustomScale(); // エンティティからスケール値を取得
        poseStack.scale(scaleFactor, scaleFactor, scaleFactor); // X, Y, Z軸にスケーリング適用
    }
}
