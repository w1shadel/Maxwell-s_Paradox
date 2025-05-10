package com.maxwell.MiniBosses;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;

import java.util.HashMap;
import java.util.Map;

public class CustomBossBar {
    public static Map<Integer, CustomBossBar> customBossBars = new HashMap<>();
    static {
        customBossBars.put(0, new CustomBossBar(
                new ResourceLocation(MiniBossse.MODID, "textures/gui/boss_bar/null_bar_base.png"),
                new ResourceLocation(MiniBossse.MODID, "textures/gui/boss_bar/null_bar_overlay.png"),
                5, 16, 1,1, -4, -3, 256, 16, 25, 182,ChatFormatting.DARK_PURPLE));
        customBossBars.put(1, new CustomBossBar(
                new ResourceLocation(MiniBossse.MODID, "textures/gui/boss_bar/herobrine_bar_base.png"),
                new ResourceLocation(MiniBossse.MODID, "textures/gui/boss_bar/herobrine_bar_overlay.png"),
                5, 16, 1,1, -4, -3, 256, 16, 25, 182,ChatFormatting.DARK_PURPLE));
    }

    private final ResourceLocation baseTexture;
    private final ResourceLocation overlayTexture;
    private final boolean hasOverlay;

    private final int baseHeight;
    private final int baseTextureHeight;
    private final int baseOffsetX;
    private final int baseOffsetY;
    private final int overlayOffsetX;
    private final int overlayOffsetY;
    private final int overlayWidth;
    private final int overlayHeight;

    private final int verticalIncrement;

    private final int getProgress;

    private final ChatFormatting textColor;

    public CustomBossBar(ResourceLocation baseTexture, ResourceLocation overlayTexture, int baseHeight, int baseTextureHeight,int baseOffsetX, int baseOffsetY, int overlayOffsetX, int overlayOffsetY, int overlayWidth, int overlayHeight, int verticalIncrement,int getProgress, ChatFormatting textColor) {
        this.baseTexture = baseTexture;
        this.overlayTexture = overlayTexture;
        this.hasOverlay = overlayTexture != null;
        this.baseHeight = baseHeight;
        this.baseTextureHeight = baseTextureHeight;
        this.baseOffsetX = baseOffsetX;
        this.baseOffsetY = baseOffsetY;
        this.overlayOffsetX = overlayOffsetX;
        this.overlayOffsetY = overlayOffsetY;
        this.overlayWidth = overlayWidth;
        this.overlayHeight = overlayHeight;
        this.verticalIncrement = verticalIncrement;
        this.getProgress = getProgress;
        this.textColor = textColor;
    }

    public ResourceLocation getBaseTexture() {
        return baseTexture;
    }

    public ResourceLocation getOverlayTexture() {
        return overlayTexture;
    }

    public boolean hasOverlay() {
        return hasOverlay;
    }

    public int getBaseHeight() {
        return baseHeight;
    }

    public int getBaseTextureHeight() {
        return baseTextureHeight;
    }

    public int getBaseOffsetX() {
        return baseOffsetX;
    }

    public int getBaseOffsetY() {
        return baseOffsetY;
    }

    public int getOverlayOffsetX() {
        return overlayOffsetX;
    }

    public int getOverlayOffsetY() {
        return overlayOffsetY;
    }

    public int getOverlayWidth() {
        return overlayWidth;
    }

    public int getOverlayHeight() {
        return overlayHeight;
    }

    public int getProgress() {
        return getProgress;
    }

    public ChatFormatting getTextColor() {
        return textColor;
    }


    private void drawBar(GuiGraphics guiGraphics, int x, int y, BossEvent event) {
        guiGraphics.blit(getBaseTexture(), x, y, 0, 0, getProgress(), getBaseHeight(), 256, getBaseTextureHeight());
        int i = (int)(event.getProgress() * (getProgress() + 1));
        if (i > 0) {
            guiGraphics.blit(getBaseTexture(), x, y, 0, getBaseHeight(), i, getBaseHeight(), 256, getBaseTextureHeight());
        }
    }
}
