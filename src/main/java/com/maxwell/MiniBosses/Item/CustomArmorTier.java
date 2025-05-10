package com.maxwell.MiniBosses.Item;

import com.maxwell.MiniBosses.register.ItemRegister_Maxwell;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.EnumMap;
import java.util.function.Supplier;

public class CustomArmorTier {
    public enum ArmorMaterials implements StringRepresentable, ArmorMaterial {
        INF("inf", 999999999, (EnumMap)Util.make(new EnumMap(ArmorItem.Type.class), (p_266655_) -> {
            p_266655_.put(ArmorItem.Type.BOOTS, 6);
            p_266655_.put(ArmorItem.Type.LEGGINGS, 9);
            p_266655_.put(ArmorItem.Type.CHESTPLATE, 11);
            p_266655_.put(ArmorItem.Type.HELMET, 6);
        }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, () -> Ingredient.of(new ItemLike[]{ItemRegister_Maxwell.NULLUPGREAD_INGOT.get()})),
        PHANTOM("phantom", 999999999, (EnumMap)Util.make(new EnumMap(ArmorItem.Type.class), (p_266655_) -> {
            p_266655_.put(ArmorItem.Type.BOOTS, 4);
            p_266655_.put(ArmorItem.Type.LEGGINGS, 7);
            p_266655_.put(ArmorItem.Type.CHESTPLATE, 9);
            p_266655_.put(ArmorItem.Type.HELMET, 5);
        }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, () -> Ingredient.of(new ItemLike[]{ItemRegister_Maxwell.PHANTOM_INGOT.get()}));
        public static final StringRepresentable.EnumCodec<net.minecraft.world.item.ArmorMaterials> CODEC = StringRepresentable.fromEnum(net.minecraft.world.item.ArmorMaterials::values);
        private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = (EnumMap)Util.make(new EnumMap(ArmorItem.Type.class), (p_266653_) -> {
            p_266653_.put(ArmorItem.Type.BOOTS, 13);
            p_266653_.put(ArmorItem.Type.LEGGINGS, 15);
            p_266653_.put(ArmorItem.Type.CHESTPLATE, 16);
            p_266653_.put(ArmorItem.Type.HELMET, 11);
        });
        private final String name;
        private final int durabilityMultiplier;
        private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
        private final int enchantmentValue;
        private final SoundEvent sound;
        private final float toughness;
        private final float knockbackResistance;
        private final LazyLoadedValue<Ingredient> repairIngredient;

        private ArmorMaterials(String pName, int pDurabilityMultiplier, EnumMap<ArmorItem.Type, Integer> pProtectionFunctionForType, int pEnchantmentValue, SoundEvent pSound, float pToughness, float pKnockbackResistance, Supplier<Ingredient> pRepairIngredient) {
            this.name = pName;
            this.durabilityMultiplier = pDurabilityMultiplier;
            this.protectionFunctionForType = pProtectionFunctionForType;
            this.enchantmentValue = pEnchantmentValue;
            this.sound = pSound;
            this.toughness = pToughness;
            this.knockbackResistance = pKnockbackResistance;
            this.repairIngredient = new LazyLoadedValue(pRepairIngredient);
        }

        public int getDurabilityForType(ArmorItem.Type pType) {
            return (Integer)HEALTH_FUNCTION_FOR_TYPE.get(pType) * this.durabilityMultiplier;
        }

        public int getDefenseForType(ArmorItem.Type pType) {
            return (Integer)this.protectionFunctionForType.get(pType);
        }

        public int getEnchantmentValue() {
            return this.enchantmentValue;
        }

        public SoundEvent getEquipSound() {
            return this.sound;
        }

        public Ingredient getRepairIngredient() {
            return (Ingredient)this.repairIngredient.get();
        }

        public String getName() {
            return this.name;
        }

        public float getToughness() {
            return this.toughness;
        }

        public float getKnockbackResistance() {
            return this.knockbackResistance;
        }

        public String getSerializedName() {
            return this.name;
        }
    }
}
