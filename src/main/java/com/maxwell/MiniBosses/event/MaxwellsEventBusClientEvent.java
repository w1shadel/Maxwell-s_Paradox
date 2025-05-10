package com.maxwell.MiniBosses.event;

import com.maxwell.MiniBosses.entity.renderer.*;
import com.maxwell.MiniBosses.MiniBossse;
import com.maxwell.MiniBosses.entity.renderer.nullguyRederer;
import com.maxwell.MiniBosses.register.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MiniBossse.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public class MaxwellsEventBusClientEvent {
    @SubscribeEvent
    public static void onRegisterRenderers_null(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.NULL.get(), nullguyRederer::new);
    }
    @SubscribeEvent
    public static void onRegisterRenderers_glith(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.GLITHGUY.get(), glitchguyRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterRenderers_miniglith(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.MINIGLITHGUY.get(), MiniglitchguyRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterRenderers_boundingbox(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BOUNDINGBOX.get(), boundingboxRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterRenderers_button(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BUTTON.get(), buttonRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterRenderers_notch(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.NOTCH.get(), notchRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterRenderers_herobrine(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.HEROBRINE.get(), herobrineRenderer::new);
    }    @SubscribeEvent
    public static void onRegisterRenderers_herobrine2(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.HEROBRINE2.get(),herobrine2Renderer::new);
    }
    @SubscribeEvent
    public static void onRegisterRenderers_omega(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.PHANTOMKING.get(),PhantomKingRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterRenderers_phantom(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CUSTOMPHANTOM.get(),CustomPhantomRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterRenderers_right(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.EMPLESSOFRIGHT.get(),EmplessofRightRenderer::new);
    }
}

