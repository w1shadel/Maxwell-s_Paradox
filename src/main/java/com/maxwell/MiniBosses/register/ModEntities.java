package com.maxwell.MiniBosses.register;


import com.maxwell.MiniBosses.MiniBossse;
import com.maxwell.MiniBosses.entity.custom.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MiniBossse.MODID);
    public static final RegistryObject<EntityType<nullguy>> NULL = ENTITY_TYPES.register("null", () -> EntityType.Builder.of(nullguy::new, MobCategory.MONSTER).sized(1, 2.5f).clientTrackingRange(8).build(new ResourceLocation(MiniBossse.MODID, "null").toString()));
    public static final RegistryObject<EntityType<gulitchguy>> GLITHGUY = ENTITY_TYPES.register("glith", () -> EntityType.Builder.of(gulitchguy::new, MobCategory.MONSTER).sized(1, 2f).clientTrackingRange(8).build(new ResourceLocation(MiniBossse.MODID, "glith").toString()));
    public static final RegistryObject<EntityType<Minigulitchguy>> MINIGLITHGUY = ENTITY_TYPES.register("miniglith", () -> EntityType.Builder.of(Minigulitchguy::new, MobCategory.MONSTER).sized(1, 1f).clientTrackingRange(8).build(new ResourceLocation(MiniBossse.MODID, "miniglith").toString()));
    public static final RegistryObject<EntityType<boundingbox>> BOUNDINGBOX = ENTITY_TYPES.register("boundingbox", () -> EntityType.Builder.of(boundingbox::new, MobCategory.MONSTER).sized(4.5f, 4f).clientTrackingRange(8).build(new ResourceLocation(MiniBossse.MODID, "boundingbox").toString()));
    public static final RegistryObject<EntityType<button>> BUTTON = ENTITY_TYPES.register("button", () -> EntityType.Builder.of(button::new, MobCategory.MONSTER).sized(1f, 1f).clientTrackingRange(8).build(new ResourceLocation(MiniBossse.MODID, "button").toString()));
    public static final RegistryObject<EntityType<notch>> NOTCH = ENTITY_TYPES.register("notch", () -> EntityType.Builder.of(notch::new, MobCategory.CREATURE).sized(1f, 2f).clientTrackingRange(8).build(new ResourceLocation(MiniBossse.MODID, "notch").toString()));
    public static final RegistryObject<EntityType<herobrine>> HEROBRINE = ENTITY_TYPES.register("herobrine", () -> EntityType.Builder.of(herobrine::new, MobCategory.MONSTER).sized(1f, 2f).clientTrackingRange(8).build(new ResourceLocation(MiniBossse.MODID, "herobrine").toString()));
    public static final RegistryObject<EntityType<herobrine2>> HEROBRINE2 = ENTITY_TYPES.register("herobrine2", () -> EntityType.Builder.of(herobrine2::new, MobCategory.MONSTER).sized(1f, 2f).clientTrackingRange(8).build(new ResourceLocation(MiniBossse.MODID, "herobrine2").toString()));
    public static final RegistryObject<EntityType<PhantomKing>> PHANTOMKING = ENTITY_TYPES.register("omega_remnant", () -> EntityType.Builder.of(PhantomKing::new, MobCategory.MONSTER).sized(1f, 2f).clientTrackingRange(8).build(new ResourceLocation(MiniBossse.MODID, "omega_remnant").toString()));
    public static final RegistryObject<EntityType<CustomPhantom>> CUSTOMPHANTOM = ENTITY_TYPES.register("customphantom", () -> EntityType.Builder.of(CustomPhantom::new, MobCategory.CREATURE).sized(1f, 1f).clientTrackingRange(8).build(new ResourceLocation(MiniBossse.MODID, "customphantom").toString()));
    public static final RegistryObject<EntityType<EmplessofRight>> EMPLESSOFRIGHT = ENTITY_TYPES.register("emplessofright", () -> EntityType.Builder.of(EmplessofRight::new, MobCategory.MONSTER).sized(1f, 2.5f).clientTrackingRange(8).build(new ResourceLocation(MiniBossse.MODID, "emplessofright").toString()));
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}

