package com.maxwell.MiniBosses.event;

import com.maxwell.MiniBosses.MiniBossse;
import com.maxwell.MiniBosses.entity.custom.*;
import com.maxwell.MiniBosses.register.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MiniBossse.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MaxwellsEventBusEvent {
    @SubscribeEvent
    public static void registerAttributes_null(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.NULL.get(), nullguy.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributes_glith(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.GLITHGUY.get(), gulitchguy.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributes_miniglith(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.MINIGLITHGUY.get(), Minigulitchguy.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributes_boundingbox(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.BOUNDINGBOX.get(), boundingbox.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributes_button(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.BUTTON.get(), button.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributes_notch(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.NOTCH.get(),notch.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributes_herobrine(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.HEROBRINE.get(), herobrine.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributes_herobrine2(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.HEROBRINE2.get(), herobrine2.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributes_omega(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.PHANTOMKING.get(), PhantomKing.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributes_phantom(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.CUSTOMPHANTOM.get(), CustomPhantom.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributes_right(
            EntityAttributeCreationEvent event) {
        event.put(ModEntities.EMPLESSOFRIGHT.get(), EmplessofRight.createAttributes().build());
    }
}
