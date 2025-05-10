package com.maxwell.MiniBosses.Item;

import com.maxwell.MiniBosses.register.ItemRegister_Maxwell;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

    public class CustomTiered {
        public enum Tiers implements Tier {
            PHANTOM(9999, 999999999, 1, 1, 12, () -> Ingredient.of(new ItemLike[]{ItemRegister_Maxwell.PHANTOM_INGOT.get()})),
            INF(9999, 999999999, 1, 1, 12, () -> Ingredient.of(new ItemLike[]{ItemRegister_Maxwell.NULLUPGREAD_INGOT.get()}));


            private final int level;
            private final int uses;
            private final float speed;
            private final float damage;
            private final int enchantmentValue;
            private final LazyLoadedValue<Ingredient> repairIngredient;

            private Tiers(int pLevel, int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
                this.level = pLevel;
                this.uses = pUses;
                this.speed = pSpeed;
                this.damage = pDamage;
                this.enchantmentValue = pEnchantmentValue;
                this.repairIngredient = new LazyLoadedValue(pRepairIngredient);
            }

            public int getUses() {
                return this.uses;
            }

            public float getSpeed() {
                return this.speed;
            }

            public float getAttackDamageBonus() {
                return this.damage;
            }

            public int getLevel() {
                return this.level;
            }

            public int getEnchantmentValue() {
                return this.enchantmentValue;
            }

            public Ingredient getRepairIngredient() {
                return (Ingredient) this.repairIngredient.get();
            }
        }

    }
