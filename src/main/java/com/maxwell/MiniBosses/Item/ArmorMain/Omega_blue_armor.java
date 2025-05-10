package com.maxwell.MiniBosses.Item.ArmorMain;

import com.maxwell.MiniBosses.Item.ArmorRenderer.OmegaBlueArmorRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class Omega_blue_armor extends ArmorItem implements GeoItem {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public Omega_blue_armor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                private GeoArmorRenderer<?> renderer;

                public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                    if (this.renderer == null) {
                        this.renderer = new OmegaBlueArmorRenderer();
                    }

                    this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                    return this.renderer;
                }
            });
        }

        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

        }
        @Override
        public boolean isDamageable(ItemStack stack) {
            return false; // 耐久値が減らない
        }

        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return this.cache;
        }
        @Override
        public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
            tooltip.add(Component.translatable("item.miniboss.omega_armor.desc").withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(Component.translatable("item.miniboss.omega_armor.desc2").withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }
